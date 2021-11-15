
```shell
cd /Users/devtools/repositories/IMDG/geode/apache-geode-1.13.1/bin
./gfsh
```

```shell
start locator --name=locator
configure pdx --read-serialized=true --disk-store=DEFAULT
start server --name=server1
```

```shell
create region --name=Account --type=PARTITION
```


Start Publisher

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/stream-account-http-source/target/stream-account-http-source-0.0.1-SNAPSHOT.jar
```

Start Consumers Quorum
```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/stream-account-geode-sink/target/stream-account-geode-sink-0.0.1-SNAPSHOT.jar
```


```shell
query --query="select id, balance, bank_id, label from /Account"
```

Start Consumers Stream


```shell
create region --name=AccountStream --type=PARTITION
```

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/stream-account-geode-sink/target/stream-account-geode-sink-0.0.1-SNAPSHOT.jar --spring.profiles.active=stream --server.port=0 --gemfire.region.name=AccountStream
```

In Gfsh

```shell
query --query="select id, balance, bank_id, label from /AccountStream"
```

Reply

```shell
destroy region --name=/AccountStream
create region --name=AccountStream --type=PARTITION
query --query="select id, balance, bank_id, label from /AccountStream"
```

```shell
java -jar applications/stream-account-geode-sink/target/stream-account-geode-sink-0.0.1-SNAPSHOT.jar --spring.profiles.active=stream --server.port=0 --gemfire.region.name=AccountStream --rabbitmq.streaming.replay=true
```


```shell
query --query="select id, balance, bank_id, label from /AccountStream"
```


Adding routing binding

```
 Routing Key: *.vmware.*	
 Queue: test-quorum
```

Testing with bankId 1.vmware.1