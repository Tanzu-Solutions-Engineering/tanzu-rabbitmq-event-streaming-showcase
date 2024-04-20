# startup Rabbit


```shell
cd /tmp

export RABBIT_CONFIG_DIR=/Users/devtools/integration/messaging/rabbit/rabbit-devOps
export RABBITMQ_CONFIG_FILE=$RABBIT_CONFIG_DIR/config/rabbitmq.conf
export RABBITMQ_CONF_ENV_FILE=$RABBIT_CONFIG_DIR/config/rabbitmq-env.conf
export RABBITMQ_ADVANCED_CONFIG_FILE=$RABBIT_CONFIG_DIR/config/advanced.config

rm nohup.out
nohup rabbitmq-server&
sleep 3s
```

$RABBIT_CONFIG_DIR/config/rabbitmq.conf

```properties
log.file.rotation.date = $D0
log.file.rotation.size = 1073741824
log.file.rotation.count = 21
vm_memory_high_watermark.relative = 0.48
queue_master_locator = min-masters
collect_statistics_interval = 30000
cluster_partition_handling = autoheal
disk_free_limit.absolute = 2GB
cluster_name = dataTxRabbitMQ
prometheus.return_per_object_metrics = true

```


```properties
MNESIA_BASE=/Users/devtools/integration/messaging/rabbit/rabbit-devOps/messages/mnesia
LOG_BASE=/Users/devtools/integration/messaging/rabbit/rabbit-devOps/logs
QUORUM_DIR=/Users/devtools/integration/messaging/rabbit/rabbit-devOps/messages/quorum
USE_LONGNAME=false
```


Open Dashboard

```shell
open http://localhost:15672/
```

User: guest/guest


---------------------------------------------------
# Run Performance Application

See [RabbitMQ Perf Test](https://rabbitmq.github.io/rabbitmq-perf-test/stable/htmlsingle/)

## Runs as fast as possible

Default - In-memory - classic queue and
 - Direct exchange
 - Classic Queue
 - No Persistent
 - None durable
 - Auto delete
 - publisher - No confirms
 - consumer Auto acknowledgement
 - 12 byte payloads

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/performance/rabbit-spring-perf-test/target/rabbit-spring-perf-test-0.0.1-SNAPSHOT.jar
```

## 1 Producer 2 Consumers

- single publisher without publisher confirms
- two consumers (each receiving a copy of every message) 
- Consumer automatic acknowledgement mode 
- A single queue named “throughput-test-x1-y2”
- Publishers will publish as quickly as possible, without any rate limiting.
- Results will be prefixed with “test1” for easier identification and comparison:

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/performance/rabbit-spring-perf-test/target/rabbit-spring-perf-test-0.0.1-SNAPSHOT.jar -x 1 -y 2 -u "throughput-test-1" -a --id "test 1"
```

## Consumers to manual acknowledgements:

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/performance/rabbit-spring-perf-test/target/rabbit-spring-perf-test-0.0.1-SNAPSHOT.jar -x 1 -y 2 -u "throughput-test-3" --id "test 3"
```

## Durable queues and persistent messages:

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/performance/rabbit-spring-perf-test/target/rabbit-spring-perf-test-0.0.1-SNAPSHOT.jar  -x 1 -y 2 -u "throughput-test-5" --id "test-5" -f persistent
```

------------------

## Quorum Queues


2 Consumers

```shell
java -jar applications/performance/rabbit-spring-perf-test/target/rabbit-spring-perf-test-0.0.1-SNAPSHOT.jar -x 1 -y 2   --quorum-queue --queue test-quorum
```


------------------

# Streaming

See [Java Performance client](https://rabbitmq.github.io/rabbitmq-stream-java-client/stable/htmlsingle/#with-the-java-binary) 

You must enable streams in your cluster

```shell
rabbitmq-plugins enable rabbitmq_stream_management
rabbitmqctl -n rabbit enable_feature_flag stream_queue
```


Download performance test tool

```shell
wget https://github.com/rabbitmq/rabbitmq-java-tools-binaries-dev/releases/download/v-stream-perf-test-latest/stream-perf-test-latest.jar  --directory-prefix=/Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase/applications/performance/lib

```

## Run Perf Test as fast a possible

Default 

- batch size is 100
- A publisher can have at most 10,000 unconfirmed
- message is 10 bytes
- 20 GB for the maximum size of a stream 
- 500 MB Segments

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/performance/lib/stream-perf-test-latest.jar
```