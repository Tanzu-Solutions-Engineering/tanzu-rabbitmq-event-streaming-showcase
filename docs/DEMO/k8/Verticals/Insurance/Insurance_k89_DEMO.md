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
  name: account-http-ampq-source-service
spec:
  selector:
    run:  account-http-ampq-source
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
  type: LoadBalancer
```

```shell
/Users/devtools/integration/messaging/rabbit/rabbit-devOps/kubernetes/getcredentials.sh
```