```bash

curl --header "Content-Type: application/json" --request POST --data '{"foo":"bar"}' http://795f-176-221-178-161.ngrok.io/hello

curl --header "Content-Type: application/json" --request POST --data '{"number":"447840111222"}' http://795f-176-221-178-161.ngrok.io/init

# then follow the check_url
curl -v 'https://eu.api.tru.id/phone_check/v0.2/checks/cec3ea00-ae17-4bfb-bac1-6fdbea0d1fae/redirect' 

# then follo the redirect link
curl -v https://sandbox.redirect.m-auth.com/callback/sandbox\?id\=cec3ea00-ae17-4bfb-bac1-6fdbea0d1fae

# host for the domain from above:
$ host sandbox.redirect.m-auth.com
sandbox.redirect.m-auth.com is an alias for d31kp7jv0v66ik.cloudfront.net.
d31kp7jv0v66ik.cloudfront.net has address 52.85.5.59
d31kp7jv0v66ik.cloudfront.net has address 52.85.5.112
d31kp7jv0v66ik.cloudfront.net has address 52.85.5.68
d31kp7jv0v66ik.cloudfront.net has address 52.85.5.6

```
curl --header "Content-Type: application/json" --request POST --data '{"check_id":"fda02117-3aee-48cc-be17-9835b6949f1c"}' http://795f-176-221-178-161.ngrok.io/complete
f1928033c0

