# API Throttling Sink

Showcase for API Flow Control Throttling with RabbitMQ and Spring.


Install RabbitMQ Kubernetes Operator

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

```shell
kubectl get pods -n rabbitmq-system
```


Deploy RabbitMQ


```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

```shell
kubectl get pods -w
```

------------
# Start Timeout App

```shell
kubectl apply -f deployment/cloud/k8/apps/timeout-api/timeout-api.yml
```
------------

# Start SCDF

## Request API Throttle SInk


Install and start [Spring Cloud DataFlow locally](https://dataflow.spring.io/docs/installation/local/)

```shell
./deployment/cloud/k8/data-services/scdf/install_scdf.sh
```


Wait for pods to be running

```shell
kubectl get pods -w
```

Open dashboard


```shell
export SCDF_HOST=`kubectl get services scdf-spring-cloud-dataflow-server --output jsonpath='{.status.loadBalancer.ingress[0].ip}'`
echo $SCDF_HOST
```

```shell
open http://$SCDF_HOST:9393/dashboard
```


Register Sink with the following

Client Applications -> Register -> By Properties

See api-throttle properties

```properties
sink.api-throttle=docker:cloudnativedata/api-throttling-sink:0.0.1-SNAPSHOT
sink.api-throttle.bootVersion=3
```
------------------------------
# Testing Timeout

timeout-api

```shell
export TIMEOUT_HTTP_HOST=`kubectl get services timeout-api --output jsonpath='{.status.loadBalancer.ingress[0].ip}'`
echo $TIMEOUT_HTTP_HOST
```


```shell
curl -X 'POST' "http://$TIMEOUT_HTTP_HOST:8080/timeout" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "string",
  "name": "string",
  "accountType": "string",
  "status": "string",
  "notes": "string",
  "location": {
    "id": "string",
    "address": "string",
    "cityTown": "string",
    "stateProvince": "string",
    "zipPostalCode": "string",
    "countryCode": "string"
  }
}'
```

------------------------------
Create Stream

```shell
api=http | api-throttle
```

Example Deployment Properties

```properties
deployer.http.kubernetes.createLoadBalancer=true
app.api-throttle.api.throttling.url="http://timeout-api:8080/timeout"
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterExchange=apiThrottleDlx
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.republishToDlq=true
app.http.path-pattern=timeout
app.http.server.port=7575
app.http.spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
app.http.spring.cloud.stream.rabbit.bindings.output.producer.dlqQuorum.enabled=true
app.http.spring.cloud.stream.rabbit.bindings.output.producer.deadLetterExchange=apiThrottleDlx
app.http.spring.cloud.stream.rabbit.bindings.output.producer.autoBindDlq=true
```



Additional Optional Configurations

```properties
app.api-throttle.spring.cloud.stream.bindings.input.consumer.backOffInitialInterval=1000
app.api-throttle.spring.cloud.stream.bindings.input.consumer.backOffMaxInterval=1000
app.api-throttle.spring.cloud.stream.bindings.input.consumer.backOffMultiplier=1
app.api-throttle.spring.cloud.stream.bindings.input.consumer.maxAttempts=3
```


Wait for applications to deploy

```shell
kubectl get pod -w

```


-----------------------------------

Randomly Failures


View App Logs

```shell
export API_HTTP_HOST=`kubectl get services api-http --output jsonpath='{.status.loadBalancer.ingress[0].ip}'`
echo $API_HTTP_HOST
```

```shell
curl -X 'POST' "http://$API_HTTP_HOST:7575/timeout" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "string",
  "name": "string",
  "accountType": "string",
  "status": "string",
  "notes": "string",
  "location": {
    "id": "string",
    "address": "string",
    "cityTown": "string",
    "stateProvince": "string",
    "zipPostalCode": "string",
    "countryCode": "string"
  }
}'
```

Test Timeout to Dead Letter Queue

```shell
curl -X 'POST' \
   "http://$API_HTTP_HOST:7575/timeout" \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "string",
  "name": "string",
  "accountType": "string",
  "status": "TIMEOUT",
  "notes": "string",
  "location": {
    "id": "string",
    "address": "string",
    "cityTown": "string",
    "stateProvince": "string",
    "zipPostalCode": "string",
    "countryCode": "string"
  }
}'
```


---------------------------------------
# Docker building image

```shell
mvn install
cd applications/api-throttling-sink
mvn package
docker build  --platform linux/amd64,linux/arm64 -t api-throttling-sink:0.0.1-SNAPSHOT .

```

```shell
docker tag api-throttling-sink:0.0.1-SNAPSHOT cloudnativedata/api-throttling-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/api-throttling-sink:0.0.1-SNAPSHOT
```

------------------------------
# Standalone (Without SCDF) Testing

Sett http-source

```shell
java -jar /Users/devtools/integration/scdf/apps/http/http-source-rabbit-4.0.0.jar  --spring.cloud.stream.bindings.output.destination=api-throttle --http.path-pattern=timeout  --server.port=7575 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami
```
