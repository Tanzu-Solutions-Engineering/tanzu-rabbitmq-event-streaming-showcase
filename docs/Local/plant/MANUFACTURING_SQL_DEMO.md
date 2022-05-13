
# Setup

## Start Config service

```shell
cd /Users/Projects/VMware/Tanzu/SCDF/dev/scdf-extensions
java -jar applications/config-server/target/config-server-0.0.1-SNAPSHOT.jar --server.port=8888 --spring.cloud.config.server.git.uri=file://$HOME/config-repo
```


## RabbitMQ


```shell
cd /Users/devtools/integration/messaging/rabbit/rabbit-devOps
./start.sh 
```

```shell
open http://localhost:15672
```


## SCDF

```shell
cd /Users/devtools/integration/scdf/
./startSCDF.sh 
```

```shell
cd /Users/devtools/integration/scdf/
./startSkipper.sh 
```


```shell
cd /Users/devtools/integration/scdf/
./shell.sh 

```

----------------------------

# START DEMO
# Kubernetes

- Is Docker or Kubernetes supported
- Management Dashboard/API
- Scaleup


```shell
cd Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase$
kubectl apply -f cloud/k8/data-services/rabbitmq/rabbitmq-1-node.yml
```

```shell
k9s
```


```shell
export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo ""
echo "USER:" $ruser
echo "PASSWORD:" $rpwd
```


```shell
kubectl port-forward service/rabbitmq 35672:15672
```

```shell
open http://localhost:35672
```

Add user admin

- Demo permissions


### Scale up

```shell
kubectl edit rmq rabbitmq
```

- Increase  replicas: 1


### HA

Goto a nodes

```shell
ps -ef | grep beam
```

Kill a process



## Cleanup processing running in AWS

````shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
k delete -f cloud/k8/data-services/rabbitmq/rabbitmq-1-node.yml
````

------------------------------------

# Protocols Supported

# SCDF PRe-built connectors

```shell
open http://localhost:9393/dashboard
```

# What protocols are supported? (OPC, CSV, JSON, HTTP(S), REST, etc…)

## Example JDBC database

```shell
stream create --name http-to-rdbms --definition "http --port=9991 | jdbc  --tableName=measurements --columns=id,name,computer_nm:computerName,val1 --spring.application.name=jdbc-sink-mysql --spring.datasource.driver-class-name=org.mariadb.jdbc.Driver --spring.datasource.url='jdbc:mysql://localhost:3306/test'" --deploy
```


```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"1\",\"name\": \"ABC\",\"computerName\": \"Station456\",\"val1\": \"0.118\"}"
```


```sqlite-sql
use test;
select * from measurements;
```


## Example to file


```shell
stream create --name http-to-rdbms-to-file --definition ":http-to-rdbms.http > file --directory=/tmp/scdf/out/ --name=measurements-out.txt" --deploy
```

```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"2\",\"name\": \"ABC2\",\"computerName\": \"Station22\",\"val1\": \"0.218\"}"
```


```shell
tail -f /tmp/scdf/out/measurements-out.tx
```


```sqlite-sql
use test;
select * from measurements;
```
