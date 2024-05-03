#  event-log-sink

Example command to run the consumer

```shell
java -jar applications/event-log-sink/target/event-log-sink-0.0.1-SNAPSHOT.jar --spring.cloud.stream.bindings.input.destination=showcase.event.streaming.accounts --spring.cloud.stream.rabbit.bindings.input.consumer.containerType=stream --spring.cloud.stream.bindings.input.group=showcase.event.streaming.accounts --spring.cloud.stream.rabbit.bindings.input.consumer.queueNameGroupOnly=true --rabbitmq.streaming.offset=last 
```


Singe Active Consumer

```shell
java -jar applications/event-log-sink/target/event-log-sink-0.0.1-SNAPSHOT.jar --spring.cloud.stream.bindings.input.destination=showcase.event.streaming.accounts --spring.cloud.stream.rabbit.bindings.input.consumer.containerType=stream --spring.cloud.stream.bindings.input.group=showcase.event.streaming.accounts --spring.cloud.stream.rabbit.bindings.input.consumer.queueNameGroupOnly=true --rabbitmq.streaming.offset=last  --spring.cloud.stream.rabbit.bindings.input.consumer.singleActiveConsumer=true

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
mvn spring-boot:build-image
```

```shell
docker tag event-log-sink:0.0.1-SNAPSHOT cloudnativedata/event-log-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/event-log-sink:0.0.1-SNAPSHOT
```

