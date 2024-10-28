## JDBC UPSERT stream Sink

This Spring Cloud Stream application uses Spring Data to insert or update records based [RabbitMQ](https://www.rabbitmq.com/) JSON topic payloads. 

Note that this application will first execute the update SQL
If the SQL update count is 0, then the SQL insert statement will be executed.

The application supports populating bind variables from a JSON message payload.

For example payload

```json
{
  "id"   : "1",
  "name" :  "Josiah Imani"
}
``` 

## Register/Create

```shell script
app register --name jdbc-upsert --type sink  --uri docker:cloudnativedata/jdbc-upsert:latest
```


From properties

```properties
sink.jdbc-upsert=file:///Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/sinks/jdbc-upsert/target/jdbc-upsert-0.2.0-SNAPSHOT.jar
sink.jdbc-upsert.metadata=file:///Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/sinks/jdbc-upsert/target/jdbc-upsert-0.2.0-SNAPSHOT-metadata.jar
sink.jdbc-upsert.bootVersion=3
```

The configuration properties are *jdbc.upsert.updateSql* and *jdbc.upsert.insertSql*.

| Configuration         | Notes 					                                                             |                             
|-----------------------|-------------------------------------------------------------------------| 
| jdbc.upsert.updateSql | EX: update dei_users.members set MEMBER_NM = :name where MEMBER_ID =:id |
| jdbc.upsert.insertSql | Ex:insert into dei_users.members(MEMBER_ID,MEMBER_NM) values(:id,:name) |

## Example SCDF Stream


```shell
claims-http=http --port=9991 | tanzu-sql:jdbc-upsert
```

Deploy Properties

```properties
app.http.server.port=9991
app.tanzu-sql.spring.datasource.driver-class-name=org.postgresql.Driver
app.tanzu-sql.spring.datasource.username=postgres
app.tanzu-sql.spring.datasource.url="jdbc:postgresql://localhost:5432/postgres"
app.tanzu-sql.jdbc.upsert.updateSql="UPDATE insurance.claims SET payload=to_json(:payload::json) WHERE id= :id"
app.tanzu-sql.jdbc.upsert.insertSql="INSERT INTO insurance.claims (id,payload) VALUES(:id, to_json(:payload::json))"
deployer.tanzu-sql.bootVersion=3
deployer.http.bootVersion=2
deployer.jdbc.bootVersion=3
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
```

Testing via HTTP

```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{ \"id\": \"1\", \"policyId\": \"11\", \"claimType\": \"auto\", \"description\" : \"\", \"notes\" : \"\", \"claimAmount\": 2323.22, \"dateOfLoss\": \"3/3/20243\", \"insured\": { \"name\": \"Josiah Imani\", \"homeAddress\" : { \"street\" : \"1 Straight\", \"city\" : \"JC\", \"state\" : \"JC\", \"zip\" : \"02323\" } }, \"lossType\": \"Collision\" }"
```

# Setup


## App Starters

For rabbit

```shell script
 app import https://dataflow.spring.io/rabbitmq-docker-latest
```



## Pushing to a docker repository

Login to docker HUB/repository

```shell script
docker login -u <user>
```

## Docker building image

```shell
mvn install
cd applications/jdbc-upsert
mvn package
docker build  --platform linux/amd64,linux/arm64 -t jdbc-upsert:0.2.0-SNAPSHOT .
```

```shell
docker tag jdbc-upsert:0.2.0-SNAPSHOT cloudnativedata/jdbc-upsert:0.2.0-SNAPSHOT
docker push cloudnativedata/jdbc-upsert:0.2.0-SNAPSHOT
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
docker build --file=Dockerfile --tag=cloudnativedata/jdbc-upsert:latest --rm=true .
docker ps
#docker commit 208570d9a383 cloudnativedata/jdbc-upsert:latest
##docker tag cloudnativedata/jdbc-upsert:latest jdbc-upsert:config-server
docker login
docker push cloudnativedata/jdbc-upsert:latest 
docker push cloudnativedata/jdbc-upsert:latest
```


## Example


See the following



```shell script
stream create --name "jdbc-postgres" --definition "http | jdbc-upsert"
```


Local only

```shell
app register --type sink --name jdbc-upsert --uri file:///Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/jdbc-upsert/target/jdbc-upsert-0.2.0-SNAPSHOT.jar
```



#### Docker Notes


```shell script
docker login -u <user> registry.pivotal.io
```


## Deploying

```shell script
stream deploy --name jdbc-postgres --properties "deployer.jdbc-upsert.kubernetes.imagePullPolicy=Always,deployer.jdbc-upsert.kubernetes.imagePullSecret=regcred"
```


## Environment 

Environment Variables
To influence the environment settings for a given application, you can use the spring.cloud.deployer.kubernetes.environmentVariables deployer property. For example, a common requirement in production settings is to influence the JVM memory arguments. You can do so by using the JAVA_TOOL_OPTIONS environment variable, as the following example shows:

    deployer.<app>.kubernetes.environmentVariables=JAVA_TOOL_OPTIONS='-Xmx1024m -Xms1024m'
	deployer.jdbc.kubernetes.environmentVariables=spring.datasource.driver-class-name=org.postgresql.Driver,spring.datasource.url=jdbc:postgresql://192.168.1.84:5432/postgres,spring.datasource.username=postgres,spring.datasource.password=security,jdbc.upsert.updateSql=update blackat.members set MEMBER_ID =:id','  MEMBER_NM = :name' --jdbc.upsert.insertSql='insert into blackat.members(MEMBER_ID'',''MEMBER_NM) values(:id'','':name)'
    
The environmentVariables property accepts a comma-delimited string. If an environment variable contains a value which is also a comma-delimited string, it must be enclosed in single quotation marks — for example, spring.cloud.deployer.kubernetes.environmentVariables=spring.cloud.stream.kafka.binder.brokers='somehost:9092, anotherhost:9093'
This overrides the JVM memory setting for the desired <app> (replace <app> with the name of your application).


### Config Map


```shell script
kubectl create configmap jdbc-upsert-config --from-env-file=src/test/resources/postgres-local.properties
```

```shell script
kubectl describe configmaps jdbc-upsert-config
```
```shell script
kubectl get configmap jdbc-upsert-config -o yaml
```


External Postgres
```shell script
stream deploy --name jdbc-postgres --properties "deployer.jdbc-upsert.kubernetes.imagePullSecret=regcred,deployer.jdbc-upsert.kubernetes.imagePullPolicy=Always,deployer.jdbc-upsert.kubernetes.configMapKeyRefs=[{envVarName: 'jdbc.upsert.insertSql', configMapName: 'jdbc-upsert-config', dataKey: 'jdbc.upsert.insertSql'},{envVarName: 'jdbc.upsert.updateSql', configMapName: 'jdbc-upsert-config', dataKey: 'jdbc.upsert.updateSql'}], deployer.jdbc-upsert.kubernetes.environmentVariables=jdbc.upsert.test=test,spring.datasource.driver-class-name=org.postgresql.Driver,spring.datasource.url=jdbc:postgresql://HOSTNAME:5432/postgres,spring.datasource.username=postgres,spring.datasource.password=PASSWD"
```


SCDF Postgres
```shell script
stream deploy --name jdbc-postgres --properties "deployer.jdbc-upsert.kubernetes.imagePullSecret=regcred,deployer.jdbc-upsert.kubernetes.imagePullPolicy=Always,deployer.jdbc-upsert.kubernetes.configMapKeyRefs=[{envVarName: 'jdbc.upsert.insertSql', configMapName: 'jdbc-upsert-config', dataKey: 'jdbc.upsert.insertSql'},{envVarName: 'jdbc.upsert.updateSql', configMapName: 'jdbc-upsert-config', dataKey: 'jdbc.upsert.updateSql'}], deployer.jdbc-upsert.kubernetes.environmentVariables=jdbc.upsert.test=test,spring.datasource.driver-class-name=org.postgresql.Driver,spring.datasource.url=jdbc:postgresql://postgresql:5432/postgres,spring.datasource.username=postgres,spring.datasource.password=CHANGEME"

```


### Config Map/Secret


```shell script
kubectl apply -k src/test/resources/secret
```

```shell script
stream deploy --name jdbc-postgres --properties "deployer.jdbc-upsert.kubernetes.imagePullSecret=regcred,deployer.jdbc-upsert.kubernetes.imagePullPolicy=Always,deployer.jdbc-upsert.kubernetes.configMapKeyRefs=[{envVarName: 'jdbc.upsert.insertSql', configMapName: 'jdbc-upsert-config', dataKey: 'jdbc.upsert.insertSql'},{envVarName: 'jdbc.upsert.updateSql', configMapName: 'jdbc-upsert-config', dataKey: 'jdbc.upsert.updateSql'}],deployer.jdbc-upsert.kubernetes.secretKeyRefs=[{envVarName: 'spring.datasource.username', secretName: 'db-connections-7hb4f5h5fb', dataKey: 'spring.datasource.username'}, {envVarName: 'spring.datasource.password', secretName: 'db-connections-7hb4f5h5fb', dataKey: 'spring.datasource.password'},{envVarName: 'spring.datasource.url', secretName: 'db-connections-7hb4f5h5fb', dataKey: 'spring.datasource.url'},{envVarName: 'spring.datasource.driver-class-name', secretName: 'db-connections-7hb4f5h5fb', dataKey: 'spring.datasource.driver-class-name'}]"
```


## Using a Spring Cloud Config Server


```shell script
stream create --name "jdbc-postgres" --definition "http | jdbc-upsert --spring.profiles.active=local-kubernetes --spring.cloud.config.uri=http://192.168.1.84:8888" 
```

```shell script
stream deploy --name jdbc-postgres --properties "deployer.jdbc-upsert.kubernetes.imagePullPolicy=Always" 
```


### HTTP Testing

http://localhost:8080

HTTP POST

```json
{
  "id":"1",
  "name": "Ms Haz"
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
	



