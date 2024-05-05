
Run application with Stream

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.1-SNAPSHOT.jar --spring.profiles.active=stream   --spring.cloud.stream.bindings.output.destination=event.stream
```

Run application with Super Stream

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.1-SNAPSHOT.jar --spring.profiles.active=superStream   --spring.cloud.stream.bindings.output.destination=event.super.stream
```

Run application with Stream with filter

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.1-SNAPSHOT.jar --spring.profiles.active=stream --rabbitmq.streaming.use.filter=true   --spring.cloud.stream.bindings.output.destination=event.stream
```

open http://localhost:8095

## Docker building image

```shell
mvn install
cd applications/event-account-http-source
mvn package

docker build  --platform linux/amd64,linux/arm64 -t event-account-http-source:0.0.1-SNAPSHOT .

```

```shell
docker tag event-account-http-source:0.0.1-SNAPSHOT cloudnativedata/event-account-http-source:0.0.1-SNAPSHOT
docker push cloudnativedata/event-account-http-source:0.0.1-SNAPSHOT
```
