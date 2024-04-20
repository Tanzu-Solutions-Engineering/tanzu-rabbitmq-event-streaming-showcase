
## Rabbit Cleanup 

Remove objects


```shell
rabbitmqctl --node rabbit delete_queue event-streaming-showcase.accounts
rabbitmqctl --node rabbit delete_queue event-streaming-showcase.accounts.dlq

```

## Postgres Cleanup

```shell
psql -d postgres -U postgres
```

```shell
delete from evt_accounts;
delete from evt_locations;
```

## Gfsh Setup 

```shell
cd /Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-10.0.1/bin
```

```shell
./gfsh 
```

```shell
start locator --name=locator
```

```shell
configure pdx --read-serialized=true --disk-store=DEFAULT
```

```shell
start server --name=server --start-rest-api=true --http-service-port=7001 --initial-heap=2g --max-heap=2g
```

```shell
destroy region --name=Account
```

```shell
create region --name=Account --type=PARTITION
```

-------------------

## Run Perf Test as fast a possible

 Default

- batch size is 100
- A publisher can have at most 10,000 unconfirmed
- message is 10 bytes
- 20 GB for the maximum size of a stream
- 500 MB Segments

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
wget https://github.com/rabbitmq/rabbitmq-java-tools-binaries-dev/releases/download/v-stream-perf-test-latest/stream-perf-test-latest.jar
java -jar stream-perf-test-latest.jar
```

--------------------

Start GemFire/Geode Consumers 

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
```
```shell
java -jar applications/account-gemfire-rabbit-stream-sink/target/account-gemfire-rabbit-stream-sink-0.0.1-SNAPSHOT.jar
```

Open RabbitMQ dashboard guest/guest
```shell
open http://localhost:15672/
```

Start GemFire management console

```shell
cd /Users/devtools/repositories/IMDG/gemfire/gideon-console
./start.sh
```

```shell
open https://localhost:7077
```
Click data browser


Start Publisher

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/account-generator-streams-source/target/account-generator-streams-source-0.0.1-SNAPSHOT.jar
```

In Data Explore

```sqlite-sql
select * from /Account limit 10
```


--------------------------------------
## REPLAY Message to Store in GemFire

STOP consumer sink

In Gfsh

```shell
shutdown
```

```shell
start server --name=server --start-rest-api=true --http-service-port=7001 --initial-heap=2g --max-heap=2g
```

Return data in data browser

```sqlite-sql
select * from /Account limit 10
```

Start Consumers with REPLAY option

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
```
```shell
java -jar applications/account-gemfire-rabbit-stream-sink/target/account-gemfire-rabbit-stream-sink-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.replay=true
```

Return data in data browser

----------------------

## Load new Data source

Start PSQL 

```shell
psql -d postgres -U postgres
```

```shell
select * from evt_accounts;
```
```shell
select * from evt_locations;
```


```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/account-jdbc-sink/target/account-jdbc-sink-0.0.1-SNAPSHOT.jar
```


```shell
select * from evt_accounts;
```
```shell
select * from evt_locations;
```


```shell
delete from evt_accounts;
delete from evt_locations;
```

```shell
select * from evt_accounts;
select * from evt_locations;
```

```shell
java -jar applications/account-jdbc-sink/target/account-jdbc-sink-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.replay=true
```

```shell
select * from evt_accounts;
select * from evt_locations;
```
