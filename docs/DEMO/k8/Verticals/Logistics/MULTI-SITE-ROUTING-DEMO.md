

## Hub
```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-hub.yml
```

User
```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/users/rabbitmq-hub-user.yml
```

Topology

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/hub-topology.yaml
```

```shell
k port-forward  service/rabbitmq-hub 9990:15672
```

-------------------

## Site 1

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-1.yml
```

Add user

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/users/rabbitmq-site-1-user.yml
```

Add Topology 

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site1-topology.yaml
```

```shell
kubectl port-forward  service/rabbitmq-site1 9991:15672
```

USer site1/site1

--------------------

Site 2

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-2.yml
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site2 --for=condition=Ready --timeout=160s
```

User
```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-2-user.yml
```

Topology

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site2-topology.yaml
```

```shell
kubectl port-forward  service/rabbitmq-site2 9992:15672
```
----------------

Site 3

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-3.yml
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site3 --for=condition=Ready --timeout=160s
```

User
```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-3-user.yml
```

Topology

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site3-topology.yaml
```
```shell
kubectl port-forward  service/rabbitmq-site3 9993:15672
```

-------------

# Shovels

From site 1
Secret 

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/site-replication/secret/site1-hub-replication-secret.yaml
```

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/site-replication/site1-hub-replication.yaml
```


From site 2

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/site-replication/secret/site2-hub-replication-secret.yaml
```

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/site-replication/site2-hub-replication.yaml
```


From site 3

Create secret
```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/site-replication/secret/site3-hub-replication-secret.yaml
```

Create shovel
```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/site-replication/site3-hub-replication.yaml
```


-----------------

# Deploy app


```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/apps/site1/http-amqp-source
```


---------------------------------------------

GemFire 


```shell
k apply -f deployment/cloud/k8/data-services/gemfire/verticals/logistics/gemfire.yml
```



Create Region "Event"

Start Gfsh

```shell
kubectl exec -it gemfire1-locator-0 -- gfsh -e "connect --locator=gemfire1-locator-0.gemfire1-locator.default.svc.cluster.local[10334]" -e "create region --name=Event --type=PARTITION_REDUNDANT"
```

Exploring sink application

```shell
k  apply -f deployment/cloud/k8/apps/verticals/transporation-logistics/spring-apps/geode-rabbitmq-sink.yaml
```


----------------

# Testing

Pulse

```shell
http://hub-gemfire-pulse:7070/pulse
```

```shell
open http://site1-amqp-source
```


```json
{
  "id" : "001",
  "chat": {
    "userId" : "user1",
    "messages" : [
      {
        "text": "Hello Team",
        "title": "Orange GroupA",
        "time": 12345677
      } 
    ]
  }
}
```

```shell
curl -X 'POST' \
  'http://site1-amqp-source/amqp/{exchange}/{routingKey}?exchange=event-exchange&routingKey=orange.GroupA' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S3|01",
  "chat": {
    "userId" : "user1",
    "messages" : [
      {
        "text": "Hello Team",
        "title": "Orange Goes to Site3",
        "time": 12345677
      } 
    ]
  }
}'
```

Site 4
```shell
curl -X 'POST' \
  'http://site1-amqp-source/amqp/{exchange}/{routingKey}?exchange=event-exchange&routingKey=green.GroupB' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S2|01",
  "chat": {
    "userId" : "user1",
    "messages" : [
      {
        "text": "Hello Team",
        "title": "Green Goes to Site2",
        "time": 12345677
      } 
    ]
  }
}'
```