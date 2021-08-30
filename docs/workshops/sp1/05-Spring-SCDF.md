# Spring SCDF



# Build account-generator-source Docker Images

## step 1 - Change to project dir

    cd ~/projects/rabbitmq/tanzu-rabbitmq-event-streaming-showcase/

## step 2 - build docker (optional if complete lab related to Quorum Queues)

mvn -pl applications/account-generator-source -am spring-boot:build-image

## step 3 - load docker to kubernetes kind (optional if complete lab related to Quorum Queues)

kind load docker-image account-generator-source:0.0.1-SNAPSHOT


--------------

## Open SCDF Dashboard

## step 1 - add account-generator-source
k port-forward deployment/scdf-server 9393:8080 &

open http://localhost:9393/dashboard

Click Add Application -> Register application

name: account-generator-source
type: source
URI: docker:account-generator-source:0.0.1-SNAPSHOT

Click -< Import Application

## step 2 - add account-generator-source

open http://localhost:9393/dashboard

Click Add Application -> Register application

name: account-geode-sink
type: sink
URI: docker:account-geode-sink:0.0.1-SNAPSHOT

Click -< Import Application

## step create account pipeline with 

open http://localhost:9393/dashboard

Click Streams -> Streams -> Create Streams


```definition
account-generator-source --server.port=8080 | account-geode-sink --server.port=8080 --spring.profiles.active=stream --spring.application.name=scf-account-sink-stream --spring.rabbitmq.stream.host=rabbitmq --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334]
```

Click Create Stream 

Name: scf-account-stream

In Terminal
cd ~/dataServices/scdf
curl -OL https://cloud-native-data.s3.amazonaws.com/spring-cloud-dataflow-shell-2.8.1.jar
java -jar spring-cloud-dataflow-shell-2.8.1.jar


stream deploy --name scf-acct --properties "deployer.account-generator-source.kubernetes.requests.memory=1Gi, deployer.account-geode-sink.kubernetes.requests.memory=1Gi, deployer.account-generator-source.kubernetes.limits.memory=1Gi, deployer.account-geode-sink.kubernetes.limits.memory=1Gi"
stream undeploy --name scf-acct

stream list



## step 5 - review Account data GemFire region/table

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "query --query='select id, bank_id, label, number, product_code from /Account'"

## destroy region

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "destroy region --name=Account"

## destroy region

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Account --type=PARTITION"
    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "query --query='select id, bank_id, label, number, product_code from /Account'"

## Destroy pipeline

## In SCDF shell

stream destroy --name scf-acct


stream create --name scdf-account-stream-replay --definition "account-generator-source --server.port=8080 | account-geode-sink --rabbitmq.streaming.replay=true --server.port=8080 --spring.profiles.active=stream --spring.application.name=scf-account-sink-stream --spring.rabbitmq.stream.host=rabbitmq --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334]"

stream deploy --name scdf-account-stream-replay --properties "deployer.account-generator-source.kubernetes.requests.memory=1Gi, deployer.account-geode-sink.kubernetes.requests.memory=1Gi, deployer.account-generator-source.kubernetes.limits.memory=1Gi, deployer.account-geode-sink.kubernetes.limits.memory=1Gi"

watch kubectl get pods