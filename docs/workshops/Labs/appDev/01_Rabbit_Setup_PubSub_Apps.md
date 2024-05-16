# LAB 01 - Rabbit setup w Pub/Sub Apps

**Prerequisite**

- Dotnet SDK 7.0.401 or higher 
- Docker version 4.29 or higher

Create the docker network (if not existing)
```shell
docker network create tanzu
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
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```

- View Logs (wait for message: started TCP listener on [::]:5672)

```shell
docker logs rabbitmq01 -f
```


- Open Management Console with credentials *user/bitnami*
```shell
open http://localhost:15672
```
# 2 - Run Consumer

```shell
dotnet run  --project  applications/dotnet/Receive
```

Review  Management Console

- Click Overview
- Click Connections
- Click Queues and Streams


# 3 - Run Publisher

In new terminal
```shell
 dotnet run  --project  applications/dotnet/Send/
```

Check if Receive app consumed message 


Review  Management Console 

- Click Overview
- Click Connections
- Click Queues and Streams


Stop all applications