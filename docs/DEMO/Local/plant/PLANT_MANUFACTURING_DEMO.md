
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

## Example CSV to log


```shell

rm /tmp/scdf/*.csv
```

```shell
stream create --name csv-to-log --definition "file --filename-regex='.*.csv' --directory='/tmp/scdf' --mode=lines | log"
```

Deploy Stream

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
cp docs/Local/plant/input/measurements.csv /tmp/scdf
```


## Example JSON, HTTP(S), REST

```shell
stream create --name http-to-rabbitmq --definition "http --port=9990 | rabbit --exchange=test --routing-key-expression=''" --deploy
```

```shell
curl http://localhost:9990 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"1\",\"name\": \"ABC\",\"computerName\": \"Station456\",\"val1\": \"0.118\"}"
```



## Example CSV



```shell
mysql -h localhost -u $MYSQL_USER --password=$MYSQL_DB_PASSWORD
```

```sqlite-sql
CREATE DATABASE test;
USE test;

CREATE TABLE measurements
(
    id varchar(255),
    name varchar(255),
    computer_nm varchar(255),
    val1 varchar(255)
);
```

## Example JDBC database

```shell
stream create --name http-to-rdbms --definition "http --port=9991 | jdbc  --tableName=measurements --columns=id,name,computer_nm:computerName,val1 --spring.application.name=jdbc-sink-mysql --spring.datasource.driver-class-name=org.mariadb.jdbc.Driver --spring.datasource.url='jdbc:mysql://localhost:3306/test'" --deploy
```


```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"1\",\"name\": \"ABC\",\"computerName\": \"Station456\",\"val1\": \"0.118\"}"
```


```sqlite-sql
select * from measurements;
```


## Example to file


```shell
stream create --name http-to-rdbms-to-file --definition ":http-to-rdbms.http > file --directory=/tmp/scdf/out/ --name=measurements-out.txt" --deploy
```

```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"2\",\"name\": \"ABC2\",\"computerName\": \"Station22\",\"val1\": \"0.218\"}"
```

## MQTT Generator (simulate device)


```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -Dconfig.properties=docs/Local/plant/input/generator-template.properties -jar applications/generator-mqtt-source/target/generator-mqtt-source-0.0.1-SNAPSHOT.jar  
```

In Mysql 
```shell
select * from measurements;
```

Bind 

```yaml
exchange: amq.topic
routing_key: mqtt-generator 
queue: csv-to-log.file.csv-to-log
```


View Log In SCDF


--------------------------------


What pre-built connectors are available to communicate with other softwares? (Oracle, MySql, MSSQL, PI, Boomi, SAP, Ignition)
What programming languages are supported? (Python, Java, PHP, etc…)
Is Docker, Kubernetes or similair type container software supported?
Does the software have the ability to Federate and Centralize?
How is the configuration/software managed? (Centralized console on a server, web api, mobile, etc…)
How easy is it to configure/map data? (Adding new nodes, editing/map exsiting, etc...) 