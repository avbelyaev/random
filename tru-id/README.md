# TruID 2FA

flow: https://developer.tru.id/docs/phone-check/integration

steps: https://developer.tru.id/

api: https://developer.tru.id/docs/reference/products#section/Authentication

---

Async/await in swift: https://matteomanferdini.com/swift-async-await/

---

1. run ngrok
```bash
ngrok http 5000
```

2. run backend on 0.0.0.0:5000 (ngrok should be forwarding requests to :5000)
```bash
source venv/bin/activate
python app.py
```

3. copy HTTPS (S!) address published by Ngrok into iOS app

4. build iOS app in Xcode, run on iPhone
    - tested on iPhone 12 mini, iOS 15.5