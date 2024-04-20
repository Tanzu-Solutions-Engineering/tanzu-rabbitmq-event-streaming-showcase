
## Rabbit Cleanup 

Remove objects


```shell
rabbitmqctl --node rabbit delete_queue banking.account.bankingAccountStream
rabbitmqctl --node rabbit delete_queue banking.account.bankingAccountStream.dlq
rabbitmqctl --node rabbit delete_queue banking.atm.atmBankStream
rabbitmqctl --node rabbit delete_queue banking.bank.bankingBankStream

rabbitmqctl --node rabbit delete_queue banking.account.bankingAccount
rabbitmqctl --node rabbit delete_queue banking.account.bankingAccount.dlq
rabbitmqctl --node rabbit delete_queue banking.account.bankingAccountStream.dlq
rabbitmqctl --node rabbit delete_queue event-streaming-showcase.stream-account-geode-sink
rabbitmqctl --node rabbit delete_queue event-streaming-showcase.stream-account-geode-sink.dlq
rabbitmqctl --node rabbit delete_queue event-streaming-showcase.stream-stream-account-jdbc-sink-stream
rabbitmqctl --node rabbit delete_queue event-streaming.stream-account-geode-sink
rabbitmqctl --node rabbit delete_queue event-streaming.stream-account-geode-sink.dlq
rabbitmqctl --node rabbit delete_queue stream-account-geode-sink.event-streaming
rabbitmqadmin delete exchange name=banking.account
rabbitmqadmin delete exchange name=event-streaming-showcase
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
cd /Users/devtools/repositories/IMDG/geode/apache-geode-1.13.7/bin
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
start server --name=server1
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
java -jar applications/performance/lib/stream-perf-test-0.5.0.jar
```

--------------------

Start Consumers Quorum

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
```
```shell
java -jar applications/stream-account-geode-sink/target/stream-account-geode-sink-0.0.1-SNAPSHOT.jar
```

Open RabbitMQ dashboard guest/guest
```shell
open http://localhost:15672/
```


In Gfsh

```shell
query --query="select * from /Account"
```

Start Publisher

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/stream-account-http-source/target/stream-account-http-source-0.0.1-SNAPSHOT.jar
```


```shell
open http://localhost:8080
```


In Gfsh 

```shell
query --query="select * from /Account"
```

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
java -jar applications/stream-account-jdbc-sink/target/stream-account-jdbc-sink-0.0.1-SNAPSHOT.jar
```


Post data
```shell
open http://localhost:8080
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
java -jar applications/stream-account-jdbc-sink/target/stream-account-jdbc-sink-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.replay=true
```

```shell
select * from evt_accounts;
select * from evt_locations;
```
