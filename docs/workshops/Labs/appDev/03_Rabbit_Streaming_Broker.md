# LAB 03 - 


**Prerequisite**

- Dotnet SDK 7.0.401 or higher
- Docker version 4.29 or higher

Create the docker network (if not existing)
```shell
docker network create tanzu
```

- Run RabbitMQ (if not running)

```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS='-rabbitmq_stream advertised_host localhost' -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```

- View Logs (wait for message: started TCP listener on [::]:5672)

```shell
docker logs rabbitmq01
```

- Enable Streams

```shell
docker run -it --rm --network tanzu bitnami/rabbitmq:3.13.1 rabbitmq-plugins  -n rabbit@rabbitmq01   enable rabbitmq_stream rabbitmq_stream_management 
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
# 2 - Stream Offsets .NET AMQP Client


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


Stop Customer and Publisher


Stop RabbitMQ

```shell
docker rm -f rabbitmq01
```

---------------------------
# 3 - Spring Stream Single Active Consumer (Kubernetes)


Start Minikube (if not started)

```shell
minikube start  --memory='5g' --cpus='4'
```

# 1 - Create RabbitMQ Broker


View PODS in rabbitmq-system

```shell
kubectl get pods -n rabbitmq-system
```

Install RabbitMQ Cluster Operator (if pods not running)

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```


Start Minikube Tunnel

```shell
minikube tunnel
```

Create 1 Node RabbitMQ 

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/rabbitmq-1-node.yml
```

Wait for server to start

```shell
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq --for=condition=Ready --timeout=160s
```

Start Event Log

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink.yml
```


```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source.yml
```

Open Sources
Submit account
```shell
open http://localhost:8080/swagger-ui/index.html
```

Kist 
```shell
kubectl get pods
```

Example output

```
NAME                                         READY   STATUS    RESTARTS   AGE
event-account-http-source-694b87f746-5x9sb   1/1     Running   0          18m
event-log-sink-cd7fbc47b-djt2v               1/1     Running   0          2m53s
event-log-sink-cd7fbc47b-qls7g               1/1     Running   0          127m
rabbitmq-server-0                            1/1     Running   0          130m

```

Review Logs for each

Example pod 1
```shell
kubectl logs -f event-log-sink-cd7fbc47b-djt2v
```

Example pod 2 (run in new terminal)
```shell
kubectl logs -f event-log-sink-cd7fbc47b-qls7g
```

Note only application get the logs events


Delete the active consumer

Example
```shell
kubectl delete pod event-log-sink-cd7fbc47b-qls7g
```




Open Sources  Submit account 
```shell
open http://localhost:8080/swagger-ui/index.html
```

Note the previous consumer will become the active consumer

---------------------------
# 4 - Cleanup

Delete Apps
```shell
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink.yml
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source.yml
```

Delete RabbitMQ

```shell
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/rabbitmq-1-node.yml
```