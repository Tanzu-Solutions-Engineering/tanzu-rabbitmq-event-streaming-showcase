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
app.tanzu-sql.app.insertSql="INSERT INTO insurance.claims (id,payload) VALUES(:id, to_json(:payload::json))"
deployer.tanzu-sql.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
```


```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\", \"policyId\": \"11\", \"claimType\": \"auto\", \"description\" : \"\", \"notes\" : \"\", \"claimAmount\": 2323.22, \"dateOfLoss\": \"3/3/20243\", \"insured\": { \"name\": \"Josiah Imani\", \"homeAddress\" : { \"street\" : \"1 Straight\", \"city\" : \"JC\", \"state\" : \"JC\", \"zip\" : \"02323\" } }, \"lossType\": \"Collision\" }"
```


