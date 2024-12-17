## JDBC SQL PRocessors

Uses Spring Data select data based [RabbitMQ](https://www.rabbitmq.com/) JSON topic payloads. 


For example,

```json
{
  "id"   : "1",
  "name" :  "Josiah Imani"
}
``` 

The configuration properties are *app.updateSql* and *app.insertSql*.


| Configuration  | Notes                                                       |
|----------------|-------------------------------------------------------------| 
| jdbc.sql.query | select name, phone, email from members where MEMBER_ID =:id |



## Pushing to a docker repository

Login to docker HUB/repository

```shell script
docker login -u <user>
```

## Docker building image

```shell
mvn install
cd applications/jdbc-sql-processor
mvn package
docker build  --platform linux/amd64,linux/arm64 -t jdbc-sql-processor:0.0.1-SNAPSHOT .
```

```shell
docker tag jdbc-sql-processor:0.0.1-SNAPSHOT cloudnativedata/jdbc-sql-processor:0.0.1-SNAPSHOT
docker push cloudnativedata/jdbc-sql-processor:0.0.1-SNAPSHOT
```


## Docker Registry Notes

```shell script
kubectl create secret generic regcred --from-file=.dockerconfigjson=/Users/ggreen/.docker/config.json     --type=kubernetes.io/dockerconfigjson
```


```shell script
kubectl create secret docker-registry regcred --docker-server=registry.pivotal.io --docker-username=<your-name> --docker-password=<your-pword> --docker-email=<your-email>
```

```shell script
kubectl get secret regcred --output=yaml



From the directory with the Dockerfile

```shell script
docker build --file=Dockerfile --tag=cloudnativedata/jdbc-sql-processor:latest --rm=true .
docker ps
docker login
docker push cloudnativedata/jdbc-sql-processor:latest 
docker push cloudnativedata/jdbc-sql-processor:latest
```


## Example

## Register/Create

```shell script
app register --name jdbc-sql-processor --type processor  --uri docker:cloudnativedata/jdbc-sql-processor:latest
```


From properties

```properties
processor.jdbc-sql-processor=file:///Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/processors/jdbc-sql-processor/target/jdbc-sql-processor-0.0.1-SNAPSHOT.jar
processor.jdbc-sql-processor.metadata=file:///Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/processors/jdbc-sql-processor/target/jdbc-sql-processor-0.0.1-SNAPSHOT-metadata.jar
processor.jdbc-sql-processor.bootVersion=3
```

```shell script
jdbc-postgres-select= http | tanzu-sql-select: jdbc-sql-processor | log
```


```properties
app.http.server.port=9993
app.tanzu-sql-select.spring.datasource.driver-class-name=org.postgresql.Driver
app.tanzu-sql-select.spring.datasource.username=postgres
app.tanzu-sql-select.spring.datasource.url="jdbc:postgresql://localhost:5432/postgres"
app.tanzu-sql-select.jdbc.sql.query="select id, payload->> 'lossType' as lossType, payload-> 'insured' ->> 'name' as name, concat( payload->'insured'->'homeAddress' ->> 'street', ', ', payload->'insured'->'homeAddress' ->> 'city', ', ', payload ->'insured'->'homeAddress' ->> 'state', ' ', payload -> 'insured'->'homeAddress' ->> 'zip') as homeAddress from insurance.claims WHERE id= :id"
deployer.tanzu-sql-select.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
app.log.spring.cloud.stream.rabbit.binder.connection-name-prefix=log
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
```

```shell script
jdbc-postgres-enrichment-valkey=http | tanzu-sql-select: jdbc-sql-processor | valkey: redis --key-expression=payload.id
```

Get Data for Valkey

```shell
 LRANGE 1 0 0
```




```properties
app.http.server.port=9994
app.tanzu-sql-select.spring.datasource.driver-class-name=org.postgresql.Driver
app.tanzu-sql-select.spring.datasource.username=postgres
app.tanzu-sql-select.spring.datasource.url="jdbc:postgresql://localhost:5432/postgres"
app.tanzu-sql-select.jdbc.sql.query="select id, payload->> 'lossType' as lossType, payload-> 'insured' ->> 'name' as name, concat( payload->'insured'->'homeAddress' ->> 'street', ', ', payload->'insured'->'homeAddress' ->> 'city', ', ', payload ->'insured'->'homeAddress' ->> 'state', ' ', payload -> 'insured'->'homeAddress' ->> 'zip') as homeAddress from insurance.claims WHERE id= :id"
deployer.tanzu-sql-select.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
app.log.spring.cloud.stream.rabbit.binder.connection-name-prefix=log
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
app.valkey.spring.cloud.stream.rabbit.binder.connection-name-prefix=valkey
```

HTTP POST

```shell
curl http://localhost:9994 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\" }"
```


Local only

```shell
app register --type sink --name jdbc-sql-processor --uri file:///Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/processors/jdbc-sql-processor/target/jdbc-sql-processor-0.0.1-SNAPSHOT.jar
```




### HTTP Testing


HTTP POST

```shell
curl http://localhost:9993 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\" }"
```


```json

	{ 
		"id"	: "1"
	}

```


Output

```json

	{
		"id"			:	"1",
		"losstype"		:	"Collision",
		"name"			:	"Josiah Imani",
		"homeaddress"	:	"1 Straight, JC, JC 02323"
	 }

```


# Trouble Shooting

## Postgres issue

See https://stackoverflow.com/questions/25641047/org-postgresql-util-psqlexception-fatal-no-pg-hba-conf-entry-for-host

    sudo vi ../12/data/pg_hba.conf

Add

    host    all             all             192.168.1.84/32         trust

020-06-26 19:46:26.249 ERROR 1 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Exception during pool initialization.

org.postgresql.util.PSQLException: FATAL: no pg_hba.conf entry for host "192.168.1.84", user "postgres", database "postgres", SSL off
	at org.postgresql.core.v3.ConnectionFactoryImpl.doAuthentication(ConnectionFactoryImpl.java:525) ~[postgresql-42.2.14.jar!/:42.2.14]
	at org.postgresql.core.v3.ConnectionFactoryImpl.tryConnect(ConnectionFactoryImpl.java:146) ~[postgresql-42.2.14.jar!/:42.2.14]
	at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:197) ~[postgresql-42.2.14.jar!/:42.2.14]
	at org.postgresql.core.ConnectionFactory.openConnection(ConnectionFactory.java:49) ~[postgresql-42.2.14.jar!/:42.2.14]
	at org.postgresql.jdbc.PgConnection.<init>(PgConnection.java:217) ~[postgresql-42.2.14.jar!/:42.2.14]
	



