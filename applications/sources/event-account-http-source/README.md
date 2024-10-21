
Run application with Stream

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.2-SNAPSHOT.jar --spring.profiles.active=stream   --spring.cloud.stream.bindings.output.destination=event.stream
```

Run application with Super Stream

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.2-SNAPSHOT.jar --spring.profiles.active=superStream   --spring.cloud.stream.bindings.output.destination=showcase.event.super.streaming.accounts
```

Run application with Stream with filter

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.2-SNAPSHOT.jar --spring.profiles.active=stream --rabbitmq.streaming.use.filter=true   --spring.cloud.stream.bindings.output.destination=event.stream
```

open http://localhost:8095

## Docker building image

```shell
mvn install
cd applications/event-account-http-source
mvn package

docker build  --platform linux/amd64,linux/arm64 -t event-account-http-source:0.0.2-SNAPSHOT .

```

```shell
docker tag event-account-http-source:0.0.2-SNAPSHOT cloudnativedata/event-account-http-source:0.0.2-SNAPSHOT
docker push cloudnativedata/event-account-http-source:0.0.2-SNAPSHOT
```

------------------

# Run Podman

```shell
podman run cloudnativedata/event-account-http-source:0.0.2-SNAPSHOT 
```