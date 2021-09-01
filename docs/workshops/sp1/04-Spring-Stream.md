# Spring Add to Stream Queues


#--------------------
# Build account-geode-sink Docker Images 

## step 1 - Change to project dir

```shell

```
    cd ~/projects/rabbitmq/tanzu-rabbitmq-event-streaming-showcase/

## step 2 - build docker (optional if complete lab related to Quorum Queues)

```shell
mvn -pl applications/account-geode-sink -am spring-boot:build-image
```

## step 3 - load docker to kubernetes kind (optional if complete lab related to Quorum Queues)

```shell
kind load docker-image account-geode-sink:0.0.1-SNAPSHOT
```

## step 4 - starts app

```shell
k apply -f cloud/k8/apps/account-geode-sink-stream
```

## step 5 - verify deploy ccount-geode-sink-stream

```shell
watch kubectl get pods
```



#--------------------
# Verify quorum queue publisher/consumer

## step 1 - port forward to access source (optional)

```shell
k port-forward deployment/account-http-source 8080:8080 &
```


## step 2 - port forward Rabbit Cluster dashboard  (optional)

```shell
k port-forward service/rabbitmq 15672:15672 &
```

## step 3 - Access Rabbit Cluster dashboard
 In Web browser open 
 [http://localhost:15672](http://localhost:15672)

user: app
password: CHANGEME

Verify Queue banking.account.bankingAccountStream created


## step 4 - Access Source App

In Web browser open

[http://localhost:8080](http://localhost:8080)

Try account-publisher (will also be sent to stream)


## step 5 - review Account data GemFire region/table

```shell
    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "query --query='select id, bank_id, label, number, product_code from /Account'"
```

