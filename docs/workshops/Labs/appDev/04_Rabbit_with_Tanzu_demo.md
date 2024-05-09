# LAB 04 - 


**Prerequisite**

- Kubernetes cluster
- RabbitMQ Operator
- brew install qemu

```shell
minikube start  --memory='5g' --cpus='4'  --driver=qemu
```

Setup GemFire Operator

```shell
./deployment/cloud/k8/data-services/gemfire/gf-k8-setup.sh
```

Create GemFire Cluster

```shell
kubectl apply -f deployment/cloud/k8/data-services/gemfire/gemfire.yml
```

```shell
kubectl  exec -it gemfire1-locator-0 -- gfsh -e "connect --locator=gemfire1-locator-0.gemfire1-locator.default.svc.cluster.local[10334]" -e "create region --name=Account --type=PARTITION"
```

Deploy Gideon Console

```shell
kubectl apply -f deployment/cloud/k8/data-services/gemfire/gideonConsole/gemfire-management-console.yml
```



Install Postgres Operator

```shell
./deployment/cloud/k8/data-services/postgres/tanzu-postgres-operator-setup.sh
```

Create Postgres HA cluster

```shell
kubectl apply -f deployment/cloud/k8/data-services/postgres/postgres.yml
```

Install RabbitMQ Operators

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

Install RabbitMQ

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

Install SCDF

```shell
./deployment/cloud/k8/data-services/scdf/install_scdf.sh
```
------------------------------------------


# 1 - Stream Data into GemFire


Create Applications -> Add Applications


```properties
app.gemfire-management-console=docker:gemfire/gemfire-management-console:1.2
sink.gemfire-sink-rabbit=docker:gemfire/gemfire-sink-rabbit:1.0.1
source.gemfire-source-rabbit=docker:gemfire/gemfire-source-rabbit:1.0.1
sink.event-account-gemfire-sink=docker:cloudnativedata/event-account-gemfire-sink:0.0.2-SNAPSHOT
sink.event-log-sink=docker:cloudnativedata/event-log-sink:0.0.1-SNAPSHOT
source.event-account-http-source=docker:cloudnativedata/event-account-http-source:0.0.1-SNAPSHOT

```

Stream

```shell
http-gemfire=event-account-http-source | event-account-gemfire-sink
```


```properties
deployer.event-account-gemfire-sink.kubernetes.configMapKeyRefs=[{envVarName: 'spring.data.gemfire.pool.locators', configMapName: 'gemfire1-config', dataKey: 'locators'}]
deployer.event-account-gemfire-sink.bootVersion=3
deployer.event-account-http-source.bootVersion=3
deployer.event-account-gemfire-sink.kubernetes.environmentVariables=spring.profiles.active=superStream,spring.rabbitmq.stream.host=rabbitmq,server.port=8080
deployer.event-account-gemfire-sink.kubernetes.imagePullPolicy=Always
deployer.event-account-http-source.kubernetes.environmentVariables=spring.profiles.active=superStream,spring.rabbitmq.stream.host=rabbitmq,server.port=8080
deployer.event-account-http-source.kubernetes.createLoadBalancer=true
deployer.event-account-http-source.kubernetes.imagePullPolicy=Always

```


```shell
deployer.account-global-service.kubernetes.secretKeyRefs=[{envVarName: 'POSTGRES_DB', secretName: 'postgres-db-app-user-db-secret', dataKey: 'database'}]
deployer.account-global-service.kubernetes.environmentVariables=spring.profiles.active=redis,spring.data.redis.cluster.nodes=gemfire-server-0:6379,rabbitmq.streaming.replay=true,spring.application.name=bank-account-gemfire-sink,spring.cloud.stream.rabbit.bindings.input.consumer.container-type=stream,spring.cloud.stream.binder.connection-name-prefix=bank-account-gemfire-sink,spring.rabbitmq.stream.host=rabbitmq,spring.data.gemfire.pool.default.locators=gemfire-locator-0.gemfire-locator.accounting.svc.cluster.local[10334]
```

# - Deploy GemFire console


```shell
gemfireconsole=gemfire-management-console
```


# Cleanup

```shell
helm uninstall gemfire-operator --namespace gemfire-system
helm uninstall gemfire-crd --namespace gemfire-system
```