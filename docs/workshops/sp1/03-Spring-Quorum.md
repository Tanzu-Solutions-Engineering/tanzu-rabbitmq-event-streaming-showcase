# Spring Add to Quorum Queues

#--------------------
# Setup GemFire data store


## step 1 - create Account GemFire region/table (executed in terminal)

```shell
    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Account --type=PARTITION"
```

#--------------------
# Build account-geode-sink Docker Images 

## step 1 - Change to project dir

```shell
    cd ~/projects/rabbitmq/tanzu-rabbitmq-event-streaming-showcase/
```

## step 2 - build docker
    
```shell
mvn -pl applications/account-geode-sink -am spring-boot:build-image
```

## step 3 - load docker to kubernetes kind

```shell
kind load docker-image account-geode-sink:0.0.1-SNAPSHOT
```

## step 4 - starts app

```shell
k apply -f cloud/k8/apps/account-geode-sink/account-geode-sink.yml
```

#--------------------
# Build account-http-sourceDocker Images

## step 1 - build docker

```shell
    mvn -pl applications/account-http-source -am spring-boot:build-image
```

## step 2 - load docker to kubernetes kind

```shell
    kind load docker-image account-http-source:0.0.1-SNAPSHOT
```

## step 3 - starts app

```shell
  k apply -f cloud/k8/apps/account-http-source
```

## step 4 - start app see pod with name account-geode-sink, then control^C

```shell
watch kubectl get pods
```


#--------------------
# Verify quorum queue publisher/consumer

## step 1 - port forward to access source

```shell
k port-forward deployment/account-http-source 8080:8080 &
```


## step 2 - port forward Rabbit Cluster dashboard

```shell
k port-forward service/rabbitmq 15672:15672 &
```

## step 3 - Access Rabbit Cluster dashboard
 In Web browser open 
 [http://localhost:15672](http://localhost:15672)


```shell
user: app
password: CHANGEME

```

## step 4 - Access Source App

In Web browser open

[http://localhost:8080](http://localhost:8080)


Try account-publisher


## step 5 - review Account data GemFire region/table

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "query --query='select id, bank_id, label, number, product_code from /Account'"

