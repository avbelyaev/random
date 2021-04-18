import json

# don't install package 'kafka'! see https://github.com/dpkp/kafka-python/issues/1906#issuecomment-640237049
from kafka import KafkaProducer


producer = KafkaProducer(bootstrap_servers='localhost:9092',
                         value_serializer=lambda v: json.dumps(v).encode('utf-8'))

filename = 'message-1.json'
topic = 'requests'

with open(filename) as f:
    data = json.load(f)
    producer.send(topic, data)
    producer.flush()

print('done')
