# tanzu-rabbitmq-event-streaming-showcase


https://docs.spring.io/spring-cloud-stream-binder-rabbit/docs/3.2.0-SNAPSHOT/reference/html/spring-cloud-stream-binder-rabbit.html#rabbitmq-stream

Running Geode Sink with Stream

In gfsh

```shell
create region --name=AccountStream --type=PARTITION
```

```shell
java -jar applications/account-geode-sink/target/account-geode-sink-0.0.1-SNAPSHOT.jar --spring.profiles.active=stream --gemfire.region.name=AccountStream --server.port=8083 
```


# Lab Outline


Lab 1 - Setup RabbitMQ /Topology Operation on K8 
Lab 2 - Create a RabbitMQ Cluster with HA
Lab 3 - Provision RabbitMQ Topology Operation
- Users, Permissions, Queues, Vhost, etc.

Lab 4 - Quorum
Lab 5 - Streams


Lab 6 - Spring DataFlow
