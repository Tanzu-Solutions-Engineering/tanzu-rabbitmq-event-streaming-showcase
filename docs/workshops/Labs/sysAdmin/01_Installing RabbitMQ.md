# Installing RabbitMQ

This lab provides details to install RabbitMQ using docker.

**Prerequisite**

Create the docker network 

```shell
docker network create tanzu
```

# 1 - Install RabbitMQ Broker

- Run RabbitMQ
```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```
- Open Management Console with credentials *user/bitnami*
```shell
open http://localhost:15672
```

# 2-  Configure User in Management Console

- click Admin -> add a user -> app/changeme 
- Add Tag (ex: management)
- Create permissions (.* from configure, write and read)
- Try logging into management console


# 3 - SetUp Exchanges and Queues in Management Console

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


# 4 -  Publish & Consume Message with Management Console

- Click Exchanges ->  app.exchange
- Publish message -> payload
```json
{ "message": "Hello World" }
```
- Click Publish


Test Consuming M

- Click Queues and Streams -> app.consumer
- Click Get message
- Messages: 1
- Click Get Message(s)


# 5 - Perf Testing

Run Performance Test application

```shell
docker run -it -p 8080:8080 --hostname rabbitmqperftest --name rabbitmqperftest  --network tanzu --rm pivotalrabbitmq/perf-test:latest com.rabbitmq.perf.PerfTest -x 1  -y 1 -u "queue_test" -a --id "perftest" --uris amqp://user:bitnami@rabbitmq01 --use-millis --variable-size 2000:30  --rate 100  --metrics-prometheus
```


# 5 - Cleanup

```shell
docker rm -f rabbitmq01
```
