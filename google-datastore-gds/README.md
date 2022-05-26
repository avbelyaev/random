# Google Datastore (GDS)

- install `google-cloud-datastore` dependency
    - see https://cloud.google.com/appengine/docs/standard/python3/using-cloud-datastore
    
- setup creds
    - `export GOOGLE_APPLICATION_CREDENTIALS="google-datastore-gds/creds.json"`
    - see https://cloud.google.com/docs/authentication/getting-started
    
- query GDS
    - notice one needs a "project id"
    - "kind" of objects (aka object type)
    - to list what kind of namespaces/objects you have, see https://stackoverflow.com/questions/69453280/how-can-we-fetch-list-of-all-the-kinds-from-a-particular-namespace-in-google-dat
    - see gds.py
