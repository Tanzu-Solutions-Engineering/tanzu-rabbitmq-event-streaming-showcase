```json

{
  "id": "1",
  "policyId": "11",
  "claimType": "auto",
  "description" : "",
  "notes" : "",
  "claimAmount": 2323.22,
  "dateOfLoss": "3/3/20243",
  "insured": {
    "name": "Josiah Imani",
    "homeAddress" : {
      "street" : "1 Straight",
      "city" : "JC",
      "state" : "JC",
      "zip" : "02323"
    }
  },
  "lossType": "Collision"
}
```



# Setup

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
./startSkipper.sh 
```


```shell
cd /Users/devtools/integration/scdf/
./startSCDF.sh 
```


```shell
cd /Users/devtools/integration/scdf/
./shell.sh 
```

----------------------------


# Protocols Supported

# SCDF PRe-built connectors

```shell
open http://localhost:9393/dashboard
```

# What protocols are supported? (OPC, CSV, JSON, HTTP(S), REST, etc…)

## Example JSON, HTTP(S), REST


```sqlite-sql
create schema if not exists insurance;

CREATE TABLE insurance.claims(
   id varchar(255) PRIMARY KEY,
   payload JSONB NOT NULL
);
```

## Example JDBC database

--spring.datasource.url=jdbc:postgresql://localhost:5432/postgres

```shell
claims-http=http --port=9991 | tanzu-sql:jdbc-upsert
```

```properties
app.http.server.port=9991
app.tanzu-sql.spring.datasource.driver-class-name=org.postgresql.Driver
app.tanzu-sql.spring.datasource.username=postgres
app.tanzu-sql.spring.datasource.url="jdbc:postgresql://localhost:5432/postgres"
app.tanzu-sql.app.updateSql="UPDATE insurance.claims SET payload=to_json(:payload::json) WHERE id= :id"
app.tanzu-sql.app.insertSql="INSERT INTO insurance.claims (id,payload) VALUES(:id, to_jsonb(:payload))"
deployer.tanzu-sql.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
```


```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\", \"policyId\": \"11\", \"claimType\": \"auto\", \"description\" : \"\", \"notes\" : \"\", \"claimAmount\": 2323.22, \"dateOfLoss\": \"3/3/20243\", \"insured\": { \"name\": \"Josiah Imani\", \"homeAddress\" : { \"street\" : \"1 Straight\", \"city\" : \"JC\", \"state\" : \"JC\", \"zip\" : \"02323\" } }, \"lossType\": \"Collision\" }"
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