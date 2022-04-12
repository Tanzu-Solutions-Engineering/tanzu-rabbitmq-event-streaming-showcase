
# Setup 

## RabbitMQ

```shell
open http://localhost:15672
```

```shell
cd /Users/devtools/integration/messaging/rabbit/rabbit-devOps
./start.sh 
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
stream create --name csv-to-log --definition "file --filename-regex='.*.csv' --directory="/tmp/scdf" --mode=lines | log"
```

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
cp docs/Local/plant/input/measurements.csv /tmp/scdf
```


## Example JSON, HTTP(S), REST

```shell
stream create --name http-to-rabbitmq --definition "http --port=9990 | rabbit --exchange=test --routing-key-expression=''" --deploy
```

```shell
curl http://localhost:9990 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"name\": \"Foo\"}"
```


## Example CSV

```sqlite-sql
CREATE DATABASE test;
USE test;
CREATE TABLE names
(
name varchar(255)
);
```

```shell
mysql -h localhost -u $MYSQL_USER --password=$MYSQL_DB_PASSWORD
```

```shell
stream create --name file-to-rdbms --definition "file --filename-regex=.*.txt --directory='/tmp/scdf'   --mode=lines | jdbc  --tableName=names --columns=name --spring.application.name=jdbc-sink-mysql --spring.datasource.driver-class-name=org.mariadb.jdbc.Driver --spring.datasource.url='jdbc:mysql://localhost:3306/test'" --deploy
```


File 

name.txt

```json
{"name": "Spring Boot"}
```


```sqlite-sql
select * from names;
```

-------------

Programming languages

Is Docker or Kubernetes supported

Federation


Management Dashboard/API

Scaleup

--------------------------------


What pre-built connectors are available to communicate with other softwares? (Oracle, MySql, MSSQL, PI, Boomi, SAP, Ignition)
What programming languages are supported? (Python, Java, PHP, etc…)
Is Docker, Kubernetes or similair type container software supported?
Does the software have the ability to Federate and Centralize?
How is the configuration/software managed? (Centralized console on a server, web api, mobile, etc…)
How easy is it to configure/map data? (Adding new nodes, editing/map exsiting, etc...) 