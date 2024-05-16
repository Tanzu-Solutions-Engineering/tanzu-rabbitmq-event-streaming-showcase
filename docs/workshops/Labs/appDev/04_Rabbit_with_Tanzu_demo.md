# LAB 04 - 


**Prerequisite**

- Kubernetes cluster
- RabbitMQ Operator
- Download Source Code

Example with git
```shell
git clone https://github.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase.git
cd tanzu-rabbitmq-event-streaming-showcase
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
# 1 - Stream Data From HTTP into GemFire


Open SCDF

Create Applications -> Add Applications


```properties
sink.gemfire-sink-rabbit=docker:gemfire/gemfire-sink-rabbit:1.0.1
source.gemfire-source-rabbit=docker:gemfire/gemfire-source-rabbit:1.0.1
sink.event-account-gemfire-sink=docker:cloudnativedata/event-account-gemfire-sink:0.0.2-SNAPSHOT
sink.event-log-sink=docker:cloudnativedata/event-log-sink:0.0.1-SNAPSHOT
source.event-account-http-source=docker:cloudnativedata/event-account-http-source:0.0.1-SNAPSHOT

```

Create Stream

```shell
http-gemfire=event-account-http-source | event-account-gemfire-sink
```

Set Deploy properties

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

Wait for PODs to deploy

```shell
kubectl get pods
```


Get HTTP Service IP

```shell
kubectl get services
```

Open URL

Example
```shell
http://57.151.30.253:8080
```


Example Account

```json
{
  "id": "1",
  "name": "Testing",
  "accountType": "STANDARD",
  "status": "OPEN",
  "notes": "Hello World",
  "location": {
    "id": "1.LOC",
    "address": "1 Straight Street",
    "cityTown": "Jersey",
    "stateProvince": "NJ",
    "zipPostalCode": "5551",
    "countryCode": "US"
  }
}
```

View Results in GemFire

Example URL
```shell
open http://4.255.98.80:8080
```

Destroy Stream after testing

--------------------------------------

# 2 - Orchestrate multiple streams with Postgres, GemFire and more

Register JDBC sql console

```shell
app.jdbc-sql-console-app=docker:cloudnativedata/jdbc-sql-console-app:0.0.2-SNAPSHOT
sink.event-account-jdbc-sink=docker:cloudnativedata/event-account-jdbc-sink:0.0.1-SNAPSHOT

```

Created Multiple Streams/Apps

```shell
http-gf=event-account-http-source | event-account-gemfire-sink
http-log=:http-gf.event-account-http-source > event-log-sink
http-jdbc=:http-gf.event-account-http-source > event-account-jdbc-sink
sql-console=jdbc-sql-console-app
```

Deploy streams

http-gf

```properties
deployer.event-account-gemfire-sink.kubernetes.configMapKeyRefs=[{envVarName: 'spring.data.gemfire.pool.locators', configMapName: 'gemfire1-config', dataKey: 'locators'}]
deployer.event-account-gemfire-sink.bootVersion=3
deployer.event-account-http-source.bootVersion=3
deployer.event-account-gemfire-sink.kubernetes.environmentVariables=spring.profiles.active=superStream,spring.rabbitmq.stream.host=rabbitmq,server.port=8080
deployer.event-account-gemfire-sink.kubernetes.imagePullPolicy=Always
deployer.event-account-http-source.kubernetes.environmentVariables=spring.profiles.active=superStream,spring.rabbitmq.stream.host=rabbitmq,server.port=8080
deployer.event-account-http-source.kubernetes.createLoadBalancer=true
deployer.event-account-http-source.kubernetes.imagePullPolicy=Always
````

http-jdbc

```properties
deployer.event-account-jdbc-sink.kubernetes.imagePullPolicy=Always
deployer.event-account-jdbc-sink.kubernetes.environmentVariables=spring.datasource.url=jdbc:postgresql://postgres-db:5432/postgres-db,spring.profiles.active=superStream,spring.rabbitmq.stream.host=rabbitmq,server.port=8080
deployer.event-account-jdbc-sink.kubernetes.secretKeyRefs=[{envVarName: 'spring.datasource.username', secretName: 'postgres-db-app-user-db-secret', dataKey: 'username'},{envVarName: 'spring.datasource.password', secretName: 'postgres-db-app-user-db-secret', dataKey: 'password'}]
deployer.event-account-jdbc-sink.bootVersion=3
```

http-log

```properties
deployer.event-log-sink.kubernetes.environmentVariables=spring.profiles.active=superStream,spring.rabbitmq.stream.host=rabbitmq,server.port=8080
deployer.event-log-sink.kubernetes.imagePullPolicy=Always
deployer.event-log-sink.bootVersion=3
```

sql-console

```properties
deployer.jdbc-sql-console-app.bootVersion=3
deployer.jdbc-sql-console-app.kubernetes.secretKeyRefs=[{envVarName: 'spring.datasource.username', secretName: 'postgres-db-app-user-db-secret', dataKey: 'username'},{envVarName: 'spring.datasource.password', secretName: 'postgres-db-app-user-db-secret', dataKey: 'password'}]
deployer.jdbc-sql-console-app.kubernetes.environmentVariables=spring.datasource.url=jdbc:postgresql://postgres-db:5432/postgres-db,server.port=8080
deployer.jdbc-sql-console-app.kubernetes.createLoadBalancer=true
deployer.jdbc-sql-console-app.kubernetes.imagePullPolicy=Always
```


Get JDBC Console and HTTP Source IP

```shell
kubectl get services
```

Example JSON

```shell
{
  "id": "2",
  "name": "Acct 2",
  "accountType": "STANDARD",
  "status": "OPEN",
  "notes": "Testing 124",
  "location": {
    "id": "2.LOC",
    "address": "2 Straight Street",
    "cityTown": "Jersey",
    "stateProvince": "NJ",
    "zipPostalCode": "5555",
    "countryCode": "NJ"
  }
}
```

Example SQL
```sql
select * from evt_showcase.evt_accounts
```


GemFire query

```shell
select * from /Account 
```
--------------------------------------
# Cleanup

```shell
helm uninstall gemfire-operator --namespace gemfire-system
helm uninstall gemfire-crd --namespace gemfire-system
```

```shell
./deployment/cloud/k8/data-services/scdf/uninstall_scdf.sh
```
--------------

```shell
deployer.account-global-service.kubernetes.secretKeyRefs=[{envVarName: 'POSTGRES_DB', secretName: 'postgres-db-app-user-db-secret', dataKey: 'database'}]
deployer.account-global-service.kubernetes.environmentVariables=spring.profiles.active=redis,spring.data.redis.cluster.nodes=gemfire-server-0:6379,rabbitmq.streaming.replay=true,spring.application.name=bank-account-gemfire-sink,spring.cloud.stream.rabbit.bindings.input.consumer.container-type=stream,spring.cloud.stream.binder.connection-name-prefix=bank-account-gemfire-sink,spring.rabbitmq.stream.host=rabbitmq,spring.data.gemfire.pool.default.locators=gemfire-locator-0.gemfire-locator.accounting.svc.cluster.local[10334]
```
