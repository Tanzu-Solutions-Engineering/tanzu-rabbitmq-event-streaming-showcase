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

## Register Custom Apps

### sink.jdbc-upsert 

From properties

```properties
sink.jdbc-upsert=https://tanzu-scdf-data.s3.us-east-1.amazonaws.com/jdbc-upsert-0.2.0-SNAPSHOT.jar
sink.jdbc-upsert.metadata=https://tanzu-scdf-data.s3.us-east-1.amazonaws.com/jdbc-upsert-0.2.0-SNAPSHOT-metadata.jar
sink.jdbc-upsert.bootVersion=3
```

#### processor.jdbc-sql-processor

From properties

```properties
processor.jdbc-sql-processor=https://tanzu-scdf-data.s3.us-east-1.amazonaws.com/jdbc-sql-processor-0.0.1-SNAPSHOT.jar
processor.jdbc-sql-processor.metadata=https://tanzu-scdf-data.s3.us-east-1.amazonaws.com/jdbc-sql-processor-0.0.1-SNAPSHOT-metadata.jar
processor.jdbc-sql-processor.bootVersion=3
```

----------------------------


# Protocols Supported

# SCDF Pre-built connectors


# Create Table



```shell
export JDBC_CONSOLE_APP=`cf apps | grep jdbc-sql-console-app  | awk  '{print $5}'`
echo $JDBC_CONSOLE_APP

open http://$JDBC_CONSOLE_APP
```



```sqlite-sql
create schema if not exists insurance;

CREATE TABLE insurance.claims(
   id varchar(255) PRIMARY KEY,
   payload JSONB NOT NULL
);
```
--------------------------------------

# Create HTTP Json JDBC API

--spring.datasource.url=jdbc:postgresql://localhost:5432/postgres

```shell
claims-http=http --port=9991 | tanzu-sql: jdbc-upsert --insert-sql="UPDATE insurance.claims SET payload=to_json(:payload::json) WHERE id= :id" --update-sql="INSERT INTO insurance.claims (id,payload) VALUES(:id, to_json(:payload::json))"
```
app.http.server.port=80

```properties
deployer.tanzu-sql.cloudfoundry.services=postgres
app.tanzu-sql.jdbc.upsert.insert-sql=UPDATE insurance.claims SET payload=to_json(:payload::json) WHERE id= :id
app.tanzu-sql.jdbc.upsert.update-sql=INSERT INTO insurance.claims (id,payload) VALUES(:id, to_json(:payload::json))
deployer.tanzu-sql.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
```

Save URIs for API
```shell
export CLAIMS_HOST=`cf apps | grep claims-http-http  | awk  '{print $4}'`
export CLAIMS_URI="http://$CLAIMS_HOST"
echo $CLAIMS_URI
```

```shell

curl $CLAIMS_URI -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\", \"policyId\": \"11\", \"claimType\": \"auto\", \"description\" : \"\", \"notes\" : \"\", \"claimAmount\": 2323.22, \"dateOfLoss\": \"3/3/20243\", \"insured\": { \"name\": \"Josiah Imani\", \"homeAddress\" : { \"street\" : \"1 Straight\", \"city\" : \"JC\", \"state\" : \"JC\", \"zip\" : \"02323\" } }, \"lossType\": \"Collision\" }"
```


```shell
for i in {2..20}
do
  claimJson='{ "id": "';
  claimJson+=$i;
  claimJson+='", "policyId": "';
  claimJson+=$i;
  claimJson+='",  "claimType": "auto", "description" : "", "notes" : "", "claimAmount": ';
  claimJson+=$i; 
  claimJson+=', "dateOfLoss": "3/3/20243", "insured": { "name": "User ';
  claimJson+=$i;
  claimJson+='", "homeAddress" : { ';
  claimJson+=' "street" : "';
  claimJson+=$i;
  claimJson+=' Straight", "city" : "JC", "state" : "NJ", "zip" : "02323" } }, "lossType": "Collision" }';

  echo '========' POSTING $claimJson;
  
  curl $CLAIMS_URI -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d $claimJson
  echo
done
```


```sql
select * from insurance.claims
```

--------------------------------------

#  HTTP ValKey JDBC Caching Enrichment API

Create SCDF stream

```shell
claims-caching-http=http | tanzu-sql-select: jdbc-sql-processor --query="select id, payload->> 'lossType' as lossType, payload-> 'insured' ->> 'name' as name, concat( payload->'insured'->'homeAddress' ->> 'street', ', ', payload->'insured'->'homeAddress' ->> 'city', ', ', payload ->'insured'->'homeAddress' ->> 'state', ' ', payload -> 'insured'->'homeAddress' ->> 'zip') as homeAddress from insurance.claims WHERE id= :id" | valkey: redis --key-expression=payload.id
```

```properties
deployer.tanzu-sql-select.cloudfoundry.services=postgres
app.tanzu-sql-select.jdbc.sql.query="select id, payload->> 'lossType' as lossType, payload-> 'insured' ->> 'name' as name, concat( payload->'insured'->'homeAddress' ->> 'street', ', ', payload->'insured'->'homeAddress' ->> 'city', ', ', payload ->'insured'->'homeAddress' ->> 'state', ' ', payload -> 'insured'->'homeAddress' ->> 'zip') as homeAddress from insurance.claims WHERE id= :id"
deployer.tanzu-sql-select.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
app.valkey.spring.cloud.stream.rabbit.binder.connection-name-prefix=valkey
app.valkey.redis.consumer.key-expression=payload.id
deployer.valkey.cloudfoundry.services=valkey
deployer.http.memory=1400
deployer.valkey.memory=1400
deployer.tanzu-sql-select.memory=1400
```
Get Data for Valkey


HTTP POST


Get Environment

```shell
export HTTP_CACHE_CLAIMS_HOST=`cf apps | grep claims-caching-http-http  | awk  '{print $4}'`
export HTTP_CACHE_CLAIMS_HOST_URI="http://$HTTP_CACHE_CLAIMS_HOST"
echo $HTTP_CACHE_CLAIMS_HOST_URI
```

VaKey Access App

```shell
export VALKEY_HOST=`cf apps | grep valkey-console-app  | awk  '{print $5}'`
export VALKEY_HOST_URI="http://$VALKEY_HOST"
open $VALKEY_HOST_URI
```




Get using Curl

```shell
 LRANGE 1 0 0
```

No Data
```shell
curl -X 'GET' \
  "$VALKEY_HOST_URI/valKey/lrange?key=1&start=0&end=0" \
  -H 'accept: */*'
```



Cache First Claim

```shell
curl $HTTP_CACHE_CLAIMS_HOST_URI -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\" }"
```

Get claim

```shell
curl -X 'GET' \
  "$VALKEY_HOST_URI/valKey/lrange?key=1&start=0&end=0" \
  -H 'accept: */*'
```


Second Claim - No Data

```shell
curl -X 'GET' \
  "$VALKEY_HOST_URI/valKey/lrange?key=2&start=0&end=0" \
  -H 'accept: */*'
```


Cache Second Claim in ValKey

```shell
curl $HTTP_CACHE_CLAIMS_HOST_URI -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"2\" }"
```


Get Cache 2n claim

```shell
curl -X 'GET' \
  "$VALKEY_HOST_URI/valKey/lrange?key=2&start=0&end=0" \
  -H 'accept: */*'
```


-------------

# Clean Up

ValKey Clean up

```shell
 DEL 1 2
```

```shell
curl -X 'DELETE' \
  "$VALKEY_HOST_URI/valKey/del?keys=1&keys=2" \
  -H 'accept: */*'
```



```shell
open http://$JDBC_CONSOLE_APP
```


```sql
delete from insurance.claims
```
