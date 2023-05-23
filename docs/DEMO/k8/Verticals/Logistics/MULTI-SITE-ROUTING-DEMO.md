
export DEMO_WAVEFRONT_TOKEN=fbc2c8c4-a871-44ee-a825-5dad6834535d

```shell
kubectl create secret generic wavefront-secret --type=string  --from-literal=management.wavefront.api-token=$DEMO_WAVEFRONT_TOKEN
```


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

- Install in hub, site2 and site3 


```shell
kubectl apply -f deployment/cloud/k8/data-services/gemfire/verticals/logistics/gemfire.yml
kubectl wait pod -l=app.kubernetes.io/component=gemfire-locator --for=condition=Ready --timeout=160s
kubectl wait pod -l=app.kubernetes.io/component=gemfire-server --for=condition=Ready --timeout=160s
```



Create Region "Event"

Start Gfsh

```shell
kubectl exec -it gemfire1-locator-0 -- gfsh -e "connect --locator=gemfire1-locator-0.gemfire1-locator.default.svc.cluster.local[10334]" -e "create region --name=Event --type=PARTITION_REDUNDANT"
```

Exploring sink application


Hub

geode-hub-rabbitmq-sink

```shell
k  apply -f deployment/cloud/k8/apps/verticals/transporation-logistics/spring-apps/sinks/gemfire-hub-rabbitmq-sink.yaml
```

geode-hub-rabbitmq-source

```shell
k apply -f deployment/cloud/k8/apps/verticals/transporation-logistics/spring-apps/sources/geode-hub-rabbitmq-source.yaml
```

Site 2

```shell
k apply -f deployment/cloud/k8/apps/verticals/transporation-logistics/spring-apps/sinks/gemfire-site2-rabbitmq-sink.yaml
```


Site 3

```shell
k apply -f deployment/cloud/k8/apps/verticals/transporation-logistics/spring-apps/sinks/gemfire-site3-rabbitmq-sink.yaml
```

----------------

# Testing

Pulse

```shell
http://hub-gemfire-pulse:7070/pulse
```

```shell
open http://34.136.22.58
```

```shell
open http://hub-rabbit:15672
```


exchange=event-exchange
routingKey=orange.GroupA


```json
{
  "id" : "S1|S3|01",
  "chat": {
    "userId" : "user1",
    "message" : {
      "text": "Hello Team",
      "title": "Orange Goes to Site3",
      "timestamp" :
      {
        "date" :
        {
          "month" : 1,
          "day" : 1,
          "year" : 2013
        },
        "time" :
        {
          "hour24" : 13,
          "minute" : 59,
          "second" : 59
        }
      }
    }
  }
}
```


Site 2
```shell
curl -X 'POST' \
  'http://34.136.22.58/amqp/?exchange=event-exchange&routingKey=green.GroupB' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S2|01",
  "chat": {
    "userId" : "user1",
    "messages" : [
      {
        "text": "Hello Team Green",
        "title": "Green Goes to Site2",
        "time": 12345677
      } 
    ]
  }
}'
```

Site 3

```shell
curl -X 'POST' \
  'http://34.136.22.58/amqp/?exchange=event-exchange&routingKey=orange.GroupA' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S3|01",
  "chat": {
    "userId" : "user1",
    "message" : {
        "text": "Hello Team",
        "title": "Orange Goes to Site3",
        "timestamp" :
        {
            "date" :
            {
              "month" : 1,
              "day" : 1,
              "year" : 2013
            },
            "time" : 
            {
                "hour24" : 13,
                "minute" : 59,
                "second" : 59
            }
        } 
      } 
  }
}'
```





URGENT 

```shell
curl -X 'POST' \
  'http://34.136.22.58/amqp/?exchange=event-exchange&routingKey=orange.GroupA' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S3|U03",
  "chat": {
    "userId" : "user1",
    "message" : {
        "text": "URGENT after normal hours",
        "title": "Orange Goes to Site3",
        "eventTimeStamp" :
        {
            "eventDate" :
            {
              "month" : 1,
              "day" : 1,
              "year" : 2013
            },
            "eventTime" : 
            {
                "hour24" : 13,
                "minute" : 59,
                "second" : 59
            }
        } 
      } 
  }
}'
```

###


rabbitmq-upgrade drain

rabbitmq-upgrade revive


--------------------

#### 

# Observability Links


RabbitMQ
- https://demo.wavefront.com/u/tCvW4RTnLD?t=demo

Spring
- https://demo.wavefront.com/dashboards/Spring-Boot-gnehal#_v01(g:(d:7200,ls:!t,s:1684157342))
- 

GemFire
- https://demo.wavefront.com/u/7hbfgWn1CP?t=demo


Logs

- https://demo.wavefront.com/logs#_v01(g:(d:172800,ls:!f,s:1684336050),logs:(e:!((o:%7C=,v:error)),t:!((n:cluster,o:%7C=,v:rmq-site-1-cluster),(n:pod_name,o:%7C=,v:'http-amqp-source*'))))


Tracing

- https://demo.wavefront.com/tracing/search?traceID=fe1e4674-c93a-fab0-3b93-78a7c52916a6#_v01(fs:!n,g:(c:(d:7200,s:1684339171,w:'2h'),d:7200,s:1684339171,w:'2h'),tf:!((filterType:Operation,id:0,value:!(!(defaultApplication.,'*')))))
- 

-----------------------

# Clean up 

remove --region=/Event --key="S1|S3|U01"
remove --region=/Event --key="S1|S2|01"
remove --region=/Event --key="S1|S3|01"
remove --region=/Event --key="S1|S3|U03"


---------------------------


```shell
curl -X 'POST' \
  'http://35.192.37.46/amqp/?exchange=event-exchange&routingKey=orange.GroupA' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S3|U01",
  "chat": {
    "userId" : "user1",
    "message" : {
        "text": "Help please URGENT",
        "title": "Orange Goes to Site3",
        "eventTimeStamp" :
        {
            "eventDate" :
            {
              "month" : 1,
              "day" : 1,
              "year" : 2013
            },
            "eventTime" : 
            {
                "hour24" : 13,
                "minute" : 59,
                "second" : 59
            }
        } 
      } 
  }
}'
```


```shell
curl -X 'POST' \
  'http://localhost:8080/amqp/?exchange=amq.topic&routingKey=orange.GroupA' \
  -H 'accept: */*' \
  -H 'rabbitContentType: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" : "S1|S3|U01",
  "chat": {
    "userId" : "user1",
    "message" : {
        "text": "Help please URGENT",
        "title": "Orange Goes to Site3",
        "eventTimeStamp" :
        {
            "eventDate" :
            {
              "month" : 1,
              "day" : 1,
              "year" : 2013
            },
            "eventTime" : 
            {
                "hour24" : 13,
                "minute" : 59,
                "second" : 59
            }
        } 
      } 
  }
}'
```