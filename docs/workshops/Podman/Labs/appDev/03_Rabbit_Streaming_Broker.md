# LAB 03 - 


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


- Run RabbitMQ (if not running)

```shell
podman run --name rabbitmq01  --network tanzu --rm -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS='-rabbitmq_stream advertised_host localhost' -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```

- View Logs (wait for message: started TCP listener on [::]:5672)


- Open Management Console with credentials *user/bitnami*
```shell
open http://localhost:15672
```

# 1 - RabbitMQ Streaming 

## Start Consumer

```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar  --clientName=ReceiveStream1 --queue=app.receive.stream --queueType=stream --autoAck=false
```


## Start Publisher

Publish

```shell
 java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar  --routingKey=app.receive.stream --message="Testing app.receive STREAMING DATA 1"
```

- Hit Enter/Return to stop Publisher

Send another message

```shell
java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar  --routingKey=app.receive.stream --message="Testing app.receive STREAMING DATA 2"
```

## Review  Management Console (user/bitnami)

```shell
open http://localhost:15672
```

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
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar   --clientName=ReceiveStream1 --queue=app.receive.stream --queueType=stream --streamOffset=first --autoAck=false 
```

Hit Enter

Reading last chunk
```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar   --clientName=ReceiveStream1 --queue=app.receive.stream --queueType=stream --streamOffset=last --autoAck=false
```

Reading next message
```shell
java -jar applications/rabbit-consumer/target/rabbit-consumer-0.0.1-SNAPSHOT.jar   --clientName=ReceiveStream1 --queue=app.receive.stream --autoAck=false --queueType=stream --streamOffset=next 
```

Send another message

```shell
java -jar applications/rabbit-publisher/target/rabbit-publisher-0.0.1-SNAPSHOT.jar --routingKey=app.receive.stream --message="NEXT MESSAGE"
```


Stop Customer and Publisher


Stop RabbitMQ

```shell
podman rm -f rabbitmq01
```

---------------------------
# 3 - Spring Filter Single Active Consumer (Kubernetes)


Start Kind



Install RabbitMQ Cluster Operator (if pods not running)

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

View PODS in rabbitmq-system

```shell
kubectl get pods -n rabbitmq-system
```

Waited for PODS to be in Running status



Create 1 Node RabbitMQ 

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/rabbitmq-1-node.yml
```

View PODs

```shell
kubectl get pods
```

Wait for server to be in running state

```shell
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq --for=condition=Ready --timeout=160s
```

Deploy Event Log Application

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink.yml
```

Deploy Http Source App

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source.yml
```

Check if all pods in running state

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

Open Sources in brows

Submit account
```shell
kubectl port-forward service/event-account-http-source 8080:8080
open http://localhost:8080/swagger-ui/index.html
```

```json
{
  "id": "001",
  "name": "Event Demo 1",
  "accountType": "test",
  "status": "IN-PROGRESS",
  "notes": "Testing 123",
  "location": {
    "id": "001.001",
    "address": "1 Straight Stree",
    "cityTown": "Wayne",
    "stateProvince": "NJ",
    "zipPostalCode": "55555",
    "countryCode": "US"
  }
}
```


Example CLI

```shell
curl -X 'POST' \
  'http://localhost:8080/accounts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "001",
  "name": "Event Demo 1",
  "accountType": "test",
  "status": "IN-PROGRESS",
  "notes": "Testing 123",
  "location": {
    "id": "001.001",
    "address": "1 Straight Stree",
    "cityTown": "Wayne",
    "stateProvince": "NJ",
    "zipPostalCode": "55555",
    "countryCode": "US"
  }
}'
```

Review Logs for each

Example pod 1 -  REPLACE event-log-sink-cd7fbc47b-djt2v ACTUAL POD NAME
```shell
kubectl logs -f -lname=event-log-sink-cd7fbc47b-djt2v
```

Example pod 2 (run in new terminal) - REPLACE event-log-sink-cd7fbc47b-qls7g ACTUAL POD NAME
```shell
kubectl logs -f event-log-sink-cd7fbc47b-qls7g
```

Note message are routed by account id application get the logs events

Change Id to test routing

Example Json

Open Sources  Submit account
```shell
open http://localhost:8080/swagger-ui/index.html
```

```json
{
  "id": "002",
  "name": "Event Demo 2",
  "accountType": "test",
  "status": "IN-PROGRESS",
  "notes": "Testing 222",
  "location": {
    "id": "002.002",
    "address": "2 Straight Stree",
    "cityTown": "JamesTown",
    "stateProvince": "NY",
    "zipPostalCode": "45555",
    "countryCode": "US"
  }
}
```
Example CLI

```shell
curl -X 'POST' \
  'http://localhost:8080/accounts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "002",
  "name": "Event Demo 2",
  "accountType": "test",
  "status": "IN-PROGRESS",
  "notes": "Testing 222",
  "location": {
    "id": "002.002",
    "address": "2 Straight Stree",
    "cityTown": "JamesTown",
    "stateProvince": "NY",
    "zipPostalCode": "45555",
    "countryCode": "US"
  }
}'
```

Delete Apps
```shell
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink.yml
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source.yml
```


---------------------------
# 4 - RabbitMQ Stream Filter

enable filtering

```shell
kubectl exec  rabbitmq-server-0 -- rabbitmqctl enable_feature_flag stream_filtering
```


Create Consumer NY 

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink-filter-NY.yml
```


Create Consumer NJ

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink-filter-NJ.yml
```


Create Filtering Source

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source-filter.yml
```

Testings


View NJ filter accounts

```shell
kubectl logs deployment/event-log-sink-nj -f
```

Watch for "Started" message

View NY filter accounts (new terminal)

```shell
kubectl logs deployment/event-log-sink-ny -f
```


Open Source App

Example
```shell
open http://localhost:8090/
```

Test NY
```json
{
  "id": "NY1",
  "name": "Event NY Filtering",
  "accountType": "test",
  "status": "IN-PROGRESS",
  "notes": "Testing 222",
  "location": {
    "id": "002.002",
    "address": "2 Straight Street",
    "cityTown": "JamesTown",
    "stateProvince": "NY",
    "zipPostalCode": "45555",
    "countryCode": "US"
  }
}
```

Test NJ

```json
{
  "id": "001",
  "name": "Event Demo 1",
  "accountType": "test",
  "status": "IN-PROGRESS",
  "notes": "Testing 123",
  "location": {
    "id": "001.001",
    "address": "1 Straight Stree",
    "cityTown": "Wayne",
    "stateProvince": "NJ",
    "zipPostalCode": "55555",
    "countryCode": "US"
  }
}
```


---------------------------
# 5 - Cleanup

Delete Apps
```shell
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink.yml
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source.yml
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink-filter-NY.yml
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-log-sink/event-log-sink-filter-NJ.yml
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/apps/event-account-http-source/event-account-http-source-filter.yml
```

Delete RabbitMQ

```shell
kubectl delete -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/rabbitmq-1-node.yml
```
