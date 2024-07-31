#  event-log-sink

Example command to run the consumer last

```shell
java -jar applications/event-log-sink/target/event-log-sink-0.0.2-SNAPSHOT.jar --spring.cloud.stream.bindings.input.destination=event.stream --spring.profiles.active=stream --rabbitmq.streaming.offset=last 
```

Replay stream
```shell
java -jar applications/event-log-sink/target/event-log-sink-0.0.2-SNAPSHOT.jar --spring.cloud.stream.bindings.input.destination=event.stream --spring.profiles.active=stream --rabbitmq.streaming.offset=first 
```

```shell
rabbitmqctl -n rabbit enable_feature_flag stream_filtering
```

Filtering
```shell
java -jar applications/event-log-sink/target/event-log-sink-0.0.2-SNAPSHOT.jar --spring.cloud.stream.bindings.input.destination=event.stream --spring.profiles.active=stream --rabbitmq.streaming.offset=last --rabbitmq.streaming.filter.values="NY"
```


Singe Active Consumer

```shell
java -jar applications/event-log-sink/target/event-log-sink-0.0.2-SNAPSHOT.jar --spring.cloud.stream.bindings.input.destination=showcase.event.super.streaming.accounts --spring.profiles.active=superStream --rabbitmq.streaming.offset=last --rabbitmq.streaming.partitions=2  --spring.cloud.stream.rabbit.bindings.input.consumer.singleActiveConsumer=true

```


---

# RabbitMQ CLI Tip

Review stream offsets
```shell
rabbitmq-streams -n rabbit stream_status showcase.event.streaming.accounts --tracking
```

## Docker building image

```shell
mvn install
cd applications/event-log-sink
mvn package

docker build  --platform linux/amd64,linux/arm64 -t event-log-sink:0.0.2-SNAPSHOT .

#mvn spring-boot:build-image
```

linux/arm/v7

```shell
docker tag event-log-sink:0.0.2-SNAPSHOT cloudnativedata/event-log-sink:0.0.2-SNAPSHOT
docker push cloudnativedata/event-log-sink:0.0.2-SNAPSHOT
```

docker run cloudnativedata/event-log-sink:0.0.2-SNAPSHOT