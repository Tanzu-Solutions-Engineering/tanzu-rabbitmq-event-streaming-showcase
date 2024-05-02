# LAB 03 - 


**Prerequisite**

- Dotnet SDK 7.0.401 or higher
- Docker version 4.29 or higher

Create the docker network (if not existing)
```shell
docker network create tanzu
```

- Run RabbviitMQ (if not running)

```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```
- Open Management Console with credentials *user/bitnami*
```shell
open http://localhost:15672
```

# 1 - RabbitMQ Streaming 

## Start Consumer
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=ReceiveStream1 --queue=app.receive.stream --queueType=stream --autoAck=false
```


## Start Publisher

Publish

```shell
 dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive.stream --message="Testing app.receive STREAMING DATA 1"
```

- Hit Enter/Return

Send another message

```shell
 dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive.stream --message="Testing app.receive STREAMING DATA 2"
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


Stop Publisher and Consumer

---------------------------
# 2 - Stream Offsets


## Start Consumer

Replay all messages
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=ReceiveStream1 --queue=app.receive.stream --queueType=stream --streamOffset=first --autoAck=false 
```

Reading last chunk
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=ReceiveStream1 --queue=app.receive.stream --queueType=stream --streamOffset=last --autoAck=false
```

Reading next message
```shell
dotnet run  --project  applications/dotnet/Receive  --clientName=ReceiveStream1 --queue=app.receive.stream --autoAck=false --queueType=stream --streamOffset=next 
```

Send another message

```shell
dotnet run  --project  applications/dotnet/Send/ --routingKey=app.receive.stream --message="NEXT MESSAGE"
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