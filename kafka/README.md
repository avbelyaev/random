# Kafka

Commands (utils from kafka/bin):
```bash
# list topics
kafka-topics --list --zookeeper localhost:2181

# create topic
kafka-topics --zookeeper localhost:2181 --create --topic requests --partitions 1 --replication-factor 1

# write to topic from console
kafka-console-producer --bootstrap-server localhost:9092 --topic requests

# read from topic 
kafka-console-consumer --bootstrap-server localhost:9092 --topic requests --from-beginning
```


Send json to kafka - see produce.py
```bash
virtualenv -p python3.8 ./venv
source venv/bin/activate
pip install -r requirements.txt
```
