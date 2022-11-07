# Step UP


Postgres

```shell
cd /Users/devtools/repositories/RDMS/PostgreSQL/kubernetes/VMware
./vmware-postgres-install.sh
```

RabbitMQ
```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/cloud/k8/data-services/rabbitmq/install_commericial
./rabbitmq-commericial.sh
````

GemFire

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/cloud/k8/data-services/gemfire
./gf-k8-setup.sh
```
-------------------------------------------------------

# DEMO 

## Provision

### RabbitMQ

```shell
kubectl apply -f cloud/k8/data-services/rabbitmq/vmware-rabbitmq-1-node.yml
```

```yaml
apiVersion: rabbitmq.com/v1beta1
kind: RabbitmqCluster
metadata:
  name: rabbitmq
  namespace: default
spec:
  replicas: 1
  service:
    type: LoadBalancer
  resources:
    requests:
      cpu: "1"
      memory: "1Gi"
    limits:
      cpu: "1"
      memory: "1Gi"
  imagePullSecrets:
    - name: tanzu-rabbitmq-registry-creds
  rabbitmq:
    additionalPlugins:
      - rabbitmq_stream
```

### GemFire

```shell
kubectl apply -f cloud/k8/data-services/gemfire/gemfire.yml
```


```yaml
apiVersion: rabbitmq.com/v1beta1
kind: RabbitmqCluster
metadata:
  name: rabbitmq
  namespace: default
spec:
  replicas: 1
  service:
    type: LoadBalancer
  resources:
    requests:
      cpu: "1"
      memory: "1Gi"
    limits:
      cpu: "1"
      memory: "1Gi"
  imagePullSecrets:
    - name: tanzu-rabbitmq-registry-creds
  rabbitmq:
    additionalPlugins:
      - rabbitmq_stream
```


### Postgres


```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
kubectl apply -f cloud/k8/data-services/postgres/postgres.yml
```


```yaml
apiVersion: sql.tanzu.vmware.com/v1
kind: Postgres
metadata:
  name: postgres-db
spec:
  memory: 800Mi
  cpu: "0.8"
  storageClassName: standard
  storageSize: 3G
  serviceType: LoadBalancer
  highAvailability:
    enabled: true
```

# Spring + RabbitMQ


```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
kubectl apply -f cloud/k8/apps/account-http-ampq-source/account-http-ampq-source.yml
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app.kubernetes.io/name: account-http-ampq-source
    run:  account-http-ampq-source
  name:  account-http-ampq-source
spec:
  replicas: 1
  selector:
    matchLabels:
      name:  account-http-ampq-source
  template:
    metadata:
      labels:
        name:  account-http-ampq-source
    spec:
      containers:
        - env:
            - name: spring.profiles.active
              value: stream
            - name: spring.rabbitmq.host
              value: rabbitmq
            - name: server.port
              value: "8080"
            - name: spring_rabbitmq_username
              valueFrom:
                secretKeyRef:
                  name: rabbitmq-default-user
                  key: username
            - name: spring.rabbitmq.password
              valueFrom:
                secretKeyRef:
                  name: rabbitmq-default-user
                  key: password
          image: cloudnativedata/account-http-ampq-source:0.0.1-SNAPSHOT
          name: account-http-ampq-source
---
apiVersion: v1
kind: Service
metadata:
  name: account-http-ampq-source
spec:
  selector:
    name: account-http-ampq-source
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```


```shell
kubectl get services
```

```shell
/Users/devtools/integration/messaging/rabbit/rabbit-devOps/kubernetes/getcredentials.sh
```


## Provision GemFire for Apps


```shell
./cloud/k8/data-services/gemfire/gf-app-setup.sh
```

Example create a region
```text
kubectl  exec -it gemfire1-locator-0 -- gfsh -e "connect --locator=gemfire1-locator-0.gemfire1-locator.default.svc.cluster.local[10334]" -e "create region --name=Account --type=PARTITION_PERSISTENT"
```


Deploy Application

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
kubectl apply -f cloud/k8/apps/account-gemfire-amqp-sink/account-gemfire-amqp-sink.yml
```


```json
{
  "id": "01",
  "name": "Acct1",
  "accountType": "Student",
  "status": "OPEN",
  "notes": "This is just a test",
  "location": {
    "id": "L01",
    "address": "123 Straight Street",
    "cityTown": "Emerald City",
    "stateProvince": "NJ",
    "zipPostalCode": "12345",
    "countryCode": "US"
  }
}
```



Open GemFire pulse

```shell
open http://<HOSTNAME>:7070/pulse
```


Data Browser

```sqlite-sql
select * from /Account
```

## JDBC

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
kubectl apply -f cloud/k8/apps/account-jdbc-amqp-sink
```

Connect to Postgres

```shell
psql  postgres-db
```

```json
{
  "id": "02",
  "name": "Acct2",
  "accountType": "Student",
  "status": "OPEN",
  "notes": "This is just a JDBC test",
  "location": {
    "id": "L02",
    "address": "321 Straight Street",
    "cityTown": "Oz",
    "stateProvince": "NJ",
    "zipPostalCode": "52345",
    "countryCode": "US"
  }
}
```


```sqlite-sql
select * from evt_accounts;
```

```sqlite-sql
select * from evt_locations;
```


## Scalings

Increase for 2 Sink Instances

```shell
kubectl edit deployment account-gemfire-amqp-sink
```


Increase RabbitMQ nodes

```shell
kubectl edit RabbitMQCluster rabbitmq
```


Increase GemFire data node servers

```shell
kubectl edit GemFireCluster gemfire1
```

# High Availability

Kill RabbitMQ Node


Delete Postgres

```shell
k delete pod postgres-db-0
```

Kill GemFire Server

```shell
kubectl delete pod gemfire1-server-0
```


# Cleanup

```shell
./cloud/k8/CLEANUP.sh
```
