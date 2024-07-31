# LAB 02 - Rabbit setup w Pub/Sub Apps

**Prerequisite**

- Dotnet SDK 7.0.401 or higher 
- Docker version 4.29 or higher
- Download Source Code

Example with git
```shell
git clone https://github.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase.git
cd tanzu-rabbitmq-event-streaming-showcase
```


Create the docker network (if not existing)
```shell
docker network create tanzu
```

- Run RabbitMQ (if not running)

```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```


- View Logs (wait for message: started TCP listener on [::]:5672)

```shell
docker logs rabbitmq01
```


- Open Management Console with credentials *user/bitnami*
```shell
open http://localhost:15672
```

# 1 - Work Queues

## Start Consumer
```shell
podman run --name event-log-sink-1 --rm --network tanzu  cloudnativedata/event-log-sink:0.0.2-SNAPSHOT --spring.cloud.stream.bindings.input.destination=showcase.event.streaming.accounts  spring.cloud.stream.bindings.input.group=event-log-sink --spring.rabbitmq.host=rabbitmq01 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami --spring.profiles.active=ampq
```
Start Another Consumer
```shell
podman run --name event-log-sink-2 --rm --network tanzu  cloudnativedata/event-log-sink:0.0.2-SNAPSHOT --spring.cloud.stream.bindings.input.destination=showcase.event.streaming.accounts  spring.cloud.stream.bindings.input.group=event-log-sink --spring.rabbitmq.host=rabbitmq01 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami --spring.profiles.active=ampq
```

## Start Publisher

Publish

```shell
podman run --name event-account-http-source --network tanzu  -p 8095:8095  --rm cloudnativedata/event-account-http-source:0.0.2-SNAPSHOT --spring.profiles.active=amqp --spring.rabbitmq.host=rabbitmq01 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami --server.port=8095  
```

Send another messages (round-robin)

```shell
curl -X 'POST' \
  'http://localhost:8095/accounts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "string",
  "name": "string",
  "accountType": "string",
  "status": "string",
  "notes": "string",
  "location": {
    "id": "string",
    "address": "string",
    "cityTown": "string",
    "stateProvince": "string",
    "zipPostalCode": "string",
    "countryCode": "string"
  }
}'
```


## Review  Management Console (user/bitnami)

```shell
open http://localhost:15672
```

- Click Overview
- Click Connections
- Click Queues and Streams


Review  Management Console

- Click Overview
- Click Connections
- Click Queues and Streams


Stop Publisher and Consumers

---------------------------
# 2 - Direct Exchange Routing


## Start Consumer
```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar  --clientName=Receive1 --queue=app.receive.1
```
Start Another Consumer
```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar --clientName=Receive2 --queue=app.receive.2
```


## Start Publisher

Publish

```shell
 java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar --routingKey=app.receive.1 --message="Testing app.receive.1"
```
Hit Enter

```shell
 java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar --routingKey=app.receive.2 --message="Testing app.receive.2"
```
Hit Enter


Stop Publisher and Consumers

---------------------------
# 2 - Topic Exchange Routing



## Start Consumer
```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar   --clientName=rahway --exchange="amq.topic" --routingKey="city.Rahway.*" --queue=app.receive.rahway
```
Start Another Consumer
```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar  --clientName=ny --exchange="amq.topic" --routingKey="city.NY.#" --queue=app.receive.ny
```


## Start Publisher

Publish

```shell
 java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar   --exchange="amq.topic" --routingKey=city.NY.uptown.store --message="Testing NY City"
```

Hit Enter


```shell
 java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar   --exchange="amq.topic" --routingKey=city.Rahway.office --message="Testing Rahway"
```

Hit Enter

Stop Publisher and Consumers