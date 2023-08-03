
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
rabbitmqadmin delete exchange name=banking.account

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
destroy region --name=AccountStream
destroy region --name=AccountReplay
```

```shell
create region --name=Account --type=PARTITION
create region --name=AccountStream --type=PARTITION
create region --name=AccountReplay --type=PARTITION
```

-------------------


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



```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/stream-account-geode-sink/target/stream-account-geode-sink-0.0.1-SNAPSHOT.jar --gemfire.region.name=AccountStream --spring.profiles.active=stream --server.port=0  --spring.application.name=account-geode-sink-stream
```

In Gfsh

```shell
query --query="select * from /AccountStream"
```


Post data here

```shell
open http://localhost:8080
```


```shell
query --query="select * from /AccountStream"
```


```shell
query --query="select * from /Account"
```



## Replay

```shell
query --query="select * from /AccountReplay"
```

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/stream-account-geode-sink/target/stream-account-geode-sink-0.0.1-SNAPSHOT.jar --gemfire.region.name=AccountReplay --spring.profiles.active=stream --server.port=0  --rabbitmq.streaming.replay=true --spring.application.name=account-geode-sink-replay
```


```shell
query --query="select * from /AccountReplay"
```


Adding routing binding

```
 Queue: test-quorum
 Routing Key: *.vmware.*	
```

Testing with bankId 3.vmware.3