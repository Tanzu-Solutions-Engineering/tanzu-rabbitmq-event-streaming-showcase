# LAB 01 - Rabbit setup w Pub/Sub Apps

**Prerequisite**

- Dotnet SDK 7.0.401 or higher 
- Docker version 4.29 or higher

Create the docker network (if not existing)
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

# 2 - Run Publisher

```shell
 dotnet run  --project  applications/dotnet/Send/
```

Review  Management Console 

- Click Overview
- Click Connections
- Click Queues and Streams

# - Run Consumer

```shell
dotnet run  --project  applications/dotnet/Receive
```

Review  Management Console

- Click Overview
- Click Connections
- Click Queues and Streams

