# LAB 01 - Rabbit setup w Pub/Sub Apps

**Prerequisite**

- Dotnet SDK 7.0.401 or higher 
- Docker version 4.29 or higher

Create the podman network (if not existing)
```shell
podman network create tanzu
```

- Download Source Code

Example with git
```shell
git clone https://github.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase.git
cd tanzu-rabbitmq-event-streaming-showcase
```



# 1 - Install RabbitMQ Broker

- Run RabbitMQ
```shell
podman run --name rabbitmq01  --network tanzu --rm -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```


- Open Management Console with credentials *user/bitnami*

```shell
open http://localhost:15672
```
# 2 - Run Consumer

```shell
podman run --name event-log-sink --rm --network tanzu  cloudnativedata/event-log-sink:0.0.2-SNAPSHOT --spring.cloud.stream.bindings.input.destination=showcase.event.streaming.accounts  spring.cloud.stream.bindings.input.group=event-log-sink --spring.rabbitmq.host=rabbitmq01 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami --spring.profiles.active=ampq
```

Review  Management Console

- Click Overview
- Click Connections
- Click Queues and Streams


# 3 - Run Publisher

In new terminal
```shell
 podman run --name event-account-http-source --network tanzu  -p 8095:8095  --rm cloudnativedata/event-account-http-source:0.0.2-SNAPSHOT --spring.profiles.active=amqp --spring.rabbitmq.host=rabbitmq01 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami --server.port=8095  
```


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

Check if Receive app consumed message 


Review  Management Console 

- Click Overview
- Click Connections
- Click Queues and Streams


Stop all applications