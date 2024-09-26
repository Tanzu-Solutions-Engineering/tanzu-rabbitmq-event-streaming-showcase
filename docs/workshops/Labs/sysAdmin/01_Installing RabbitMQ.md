# LAB 01 - Installing RabbitMQ

This lab provides details to install RabbitMQ using docker.

**Prerequisite**

Create the docker network 

```shell
docker network create tanzu
```

#  Install RabbitMQ Broker

- Run RabbitMQ
```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```


- View Logs (wait for message: started TCP listener on [::]:5672)

```shell
docker logs rabbitmq01
```


# 2-  Configure User in Management Console

- Open Management Console with credentials *user/bitnami*
```shell
open http://localhost:15672
```

- click Admin -> add a user -> app/changeme
- Add Tag (ex: admin/administrator)
- Create permissions (.* from configure, write and read)
- Try logging into management console


# 3 - SetUp Exchanges and Queues in Management Console

Create an Exchange in the Management Console

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
- Click Bindings
- To queue = app.consumer
- Routing key = #



# 4 -  Publish & Consume Message with Management Console

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


# 5 - Perf Testing

Run Performance Test application

```shell
docker run -it -p 8080:8080 --hostname rabbitmqperftest --name rabbitmqperftest  --network tanzu --rm pivotalrabbitmq/perf-test:latest com.rabbitmq.perf.PerfTest -x 1  -y 1 -u "queue_test" -a --id "perftest" --uris amqp://user:bitnami@rabbitmq01 --use-millis --variable-size 2000:30  --rate 100  --metrics-prometheus
```

Control C to kill perftest


# 5 - Cleanup

```shell
docker rm -f rabbitmq01
```
