import os
import time
from threading import Thread

from flask import Flask, jsonify
from kafka import KafkaConsumer, KafkaProducer
from json import loads

messages = []


def start_consumer():
    time.sleep(10)

    kafka_host = os.environ.get('KAFKA_HOST', 'localhost:29092')
    kafka_topic = os.environ.get('KAFKA_TOPIC', 'mytopic')
    kafka_read_wait_seconds = int(os.environ.get('KAFKA_READ_WAIT_SECONDS', '1'))
    kafka_consumer_group = os.environ.get('KAFKA_CONSUMER_GROUP', 'mygroup')

    print(f"starting consumer with host={kafka_host}, topic={kafka_topic}, group={kafka_consumer_group}", flush=True)
    consumer = KafkaConsumer(kafka_topic,
                             bootstrap_servers=[kafka_host],
                             auto_offset_reset='latest',
                             enable_auto_commit=True,
                             group_id=kafka_consumer_group,
                             value_deserializer=lambda x: loads(x.decode('utf-8')))

    print("consuming", flush=True)
    for message in consumer:
        messages.append(message.value)
        print(message, flush=True)
        time.sleep(kafka_read_wait_seconds)


app = Flask(__name__)


@app.route('/messages', methods=['GET'])
def get_messages():
    return jsonify({'info': f'consumed: {messages}'}), 200


if __name__ == '__main__':
    # consume in separate thread
    thread = Thread(target=start_consumer)
    thread.start()

    app.run(host='0.0.0.0', port=5000)
