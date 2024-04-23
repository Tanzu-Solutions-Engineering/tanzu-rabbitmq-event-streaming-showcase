Prerequisite

```json
docker network create tanzu
```

# Install RabbitMQ Broker

```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```

- Open Management Console with user/bitnami

```shell
open http://localhost:15672
```


# Configure User

Explore created users

- click Admin -> add a user -> app/app 
- Add Tag (ex: management)
- Create permissions (.* from configure, write and read)

Try logging into management console


# SetUp Exchanges and Queues

Create Exchange

- Click Exchanges
- Add a new exchange 
- Name  app.exchange
- Type topic 
- Add exchange


Create Queue

- Click Queues and Streams
- Add a new queue
- Type = Quorum Queue
- Name = app.consumer
- Click Add queue


Add Bind Rule

- Click Exchanges ->  app.exchange
- To queue = app.consumer
- Routing key = #
- Click Bind


Test Publish

- Click Exchanges ->  app.exchange
- Publish message -> payload

```json
{ "message": "Hello World" }
```
- Click Publish


Test Consuming

- Click Queues and Streams -> app.consumer
- Click Get message
- Messages: 1
- Click Get Message(s)


# Perf Testing

```shell
docker run -it -p 8080:8080 --hostname rabbitmqperftest --name rabbitmqperftest  --network tanzu --rm pivotalrabbitmq/perf-test:latest com.rabbitmq.perf.PerfTest -x 1  -y 1 -u "queue_test" -a --id "perftest" --uris amqp://user:bitnami@rabbitmq01 --use-millis --variable-size 2000:30  --rate 100  --metrics-prometheus
```


# Cleanup

```shell
docker rm -f rabbitmq01
```
