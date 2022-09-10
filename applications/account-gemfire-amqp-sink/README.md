## Docker building image

```shell
mvn install
cd applications/account-gemfire-amqp-sink
mvn spring-boot:build-image
```

```shell
docker tag account-gemfire-amqp-sink:0.0.1-SNAPSHOT cloudnativedata/account-gemfire-amqp-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/account-gemfire-amqp-sink:0.0.1-SNAPSHOT
```

## Using Streams

See src/main/resources/application-stream.yml

Set runtime arguments

```shell
java -jar applications/account-geode-rabbit-streams-sink/target/account-geode-rabbit-streams-sink-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.replay=true --spring.profiles.active=stream
```

## Apache Geode/GemFire


In Gfsh


```shell
 cd /Users/devtools/repositories/IMDG/geode/apache-geode-1.13.7/bin/
```

```shell
start locator --name=locator
```

```shell
configure pdx --read-serialized=true --disk-store
```

```shell
start server --name=server --start-rest-api=true --http-service-port=7001
```


```shell
create region --name=Account --type=PARTITION
```
