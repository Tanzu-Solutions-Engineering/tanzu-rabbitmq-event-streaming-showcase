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
dotnet run  --project  applications/dotnet/Receive  --clientName=Receive1 --queue=app.receive
```
Start Another Consumer
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=Receive2 --queue=app.receive
```

## Start Publisher

Publish

```shell
dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive --message="Testing app.receive 1"
```

- Hit Enter/Return

Send another message (round-robin)

```shell
 dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive --message="Testing app.receive 2"
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
dotnet run  --project  applications/dotnet/Receive  --clientName=Receive1 --queue=app.receive.1
```
Start Another Consumer
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=Receive2 --queue=app.receive.2
```


## Start Publisher

Publish

```shell
 dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive.1 --message="Testing app.receive.1"
```
Hit Enter

```shell
 dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive.2 --message="Testing app.receive.2"
```
Hit Enter


Stop Publisher and Consumers

---------------------------
# 2 - Topic Exchange Routing



## Start Consumer
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=rahway --exchange="amq.topic" --routingKey="city.Rahway.*" --queue=app.receive.rahway
```
Start Another Consumer
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=ny --exchange="amq.topic" --routingKey="city.NY.#" --queue=app.receive.ny
```


## Start Publisher

Publish

```shell
 dotnet run  --project  applications/dotnet/Send/  --exchange="amq.topic" --routingKey=city.NY.uptown.store --message="Testing NY City"
```

Hit Enter


```shell
 dotnet run  --project  applications/dotnet/Send/  --exchange="amq.topic" --routingKey=city.Rahway.office --message="Testing Rahway"
```

Hit Enter

Stop Publisher and Consumers