import requests
from flask import Flask, jsonify
from flask import request
from requests.auth import HTTPBasicAuth

app = Flask(__name__)

# TODO change me
CLIENT_ID = "CHANGE_ME"
CLIENT_SECRET = "CHANGE_ME"

TOKEN_ENDPOINT = "https://eu.api.tru.id/oauth2/v1/token"
PHONE_CHECK_ENDPOINT = "https://eu.api.tru.id/phone_check/v0.2/checks"

TRU_ID_TOKEN = None


@app.route('/init', methods=['POST'])
def init_check():
    print(f'init check:{request.get_json()}\n')
    req_body = request.get_json()
    number = req_body['number']

    token = get_token()

    headers = {
        'Authorization': f'Bearer {token}',
        'Content-Type': 'application/json',
    }
    check_rsp = requests.post(PHONE_CHECK_ENDPOINT, json={
        "redirect_url": "https://foo.bar.com/redirect_url",
        "phone_number": str(number),
        "reference_id": "my-ref"
    }, headers=headers)
    check_rsp_body = check_rsp.json()
    print(f'check rsp: {check_rsp.status_code}, {check_rsp_body}')

    check_id = check_rsp_body['check_id']
    check_url = check_rsp_body['_links']['check_url']['href']

    return jsonify({
        'check_id': check_id,
        'check_url': check_url
    }), 200


@app.route('/complete', methods=['POST'])
def complete_check():
    print(f'completing check:{request.get_json()}\n')
    req_body = request.get_json()

    check_id = req_body['check_id']

    token = get_token()
    headers = {
        'Authorization': f'Bearer {token}'
    }
    comp_url = f'{PHONE_CHECK_ENDPOINT}/{check_id}'
    comp_rsp = requests.get(comp_url, headers=headers)

    comp_rsp_body = comp_rsp.json()
    print(f'check rsp: {comp_rsp.status_code}, {comp_rsp_body}')
    print(f'status: {comp_rsp_body["status"]}, match:{comp_rsp_body["match"]}')

    return jsonify({'foo': 'bar'}), 200


def get_token() -> str:
    global TRU_ID_TOKEN

    if TRU_ID_TOKEN:
        return TRU_ID_TOKEN

    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
    url_form_enc_data = {
        "grant_type": "client_credentials",
        "scope": "phone_check"
    }
    token_rsp = requests.post(TOKEN_ENDPOINT,
                              data=url_form_enc_data,
                              headers=headers,
                              auth=HTTPBasicAuth(CLIENT_ID, CLIENT_SECRET))

    token_rsp_body = token_rsp.json()
    TRU_ID_TOKEN = token_rsp_body['access_token']
    print(f'token: {TRU_ID_TOKEN}')
    return TRU_ID_TOKEN


@app.route('/reset')
def reset():
    global TRU_ID_TOKEN
    TRU_ID_TOKEN = None
    return jsonify({'reset': True}), 200


@app.route('/hello', methods=['POST'])
def hello():
    print(f'hello:{request.get_json()}\n')
    return jsonify({'hello': str(request.get_json())}), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
