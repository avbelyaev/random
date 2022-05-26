from google.cloud import datastore

# For help authenticating your client, visit
# https://cloud.google.com/docs/authentication/getting-started
client = datastore.Client("anthony-gds")


#
# query all namespaces available
#
query = client.query(kind="__namespace__")
query.keys_only()
namespaces = [entity.key.name for entity in query.fetch()]
print(f'namespaces available: {namespaces}')


#
# query all kinds of objects available in each namespace
#
kinds = []
for namespace in namespaces:
    print(f"====Processing namespace {namespace}====")

    query = client.query(namespace=namespace, kind="__kind__")
    query.keys_only()
    ks = [entity.key.id_or_name for entity in query.fetch()]
    print(f'object kinds available: {kinds}')


#
# list all objects of this kind
#
object_kind = 'gds_user'

# query = client.query(kind=object_kind)
# for obj in query.fetch():
#     oid = obj.key.id_or_name
#     print(f'ID: {oid}, {obj}')


#
# find object by ID, aka object of kind having `__key__ = foobar`
# see https://cloud.google.com/datastore/docs/concepts/queries#datastore-datastore-key-filter-python
#
object_key = 'df21a9272d23429db5631683b4fa9e46'

query = client.query(kind=object_kind)
my_key = client.key(object_kind, object_key)
query.key_filter(my_key, '=')

res = query.fetch()
for e in res:
    print(e)


#
# find objects having key=value
#
query = client.query(kind=object_kind)
my_key = client.key('status', '=', "EXPIRED")
res = query.fetch()
for e in res:
    print(e)
