# API Throttling Sink

Showcase for API Throttling with RabbitMQ and Spring.

------------
# Start SCDF

## Request API Throttle SInk

Register Sink with the following

```properties
sink.api-throttle=https://github.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/releases/download/api-throttle-6-3-2024/api-throttling-sink-0.0.1-SNAPSHOT.jar
```


```shell
api=http --port=7575 --path-pattern=timeout --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled="true" | api-throttle --api.throttling.url="http://localhost:8585/timeout" --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled="true" --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq="true" --spring.cloud.stream.rabbit.bindings.input.consumer.republishToDlq="true" --spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterExchange=apiThrottleDlx --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled="true"
```


```properties
app.api-throttle.api.throttling.url="http://localhost:8585/timeout"
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterExchange=apiThrottleDlx
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true
app.api-throttle.spring.cloud.stream.rabbit.bindings.input.consumer.republishToDlq=true
app.http.path-pattern=timeout
app.http.server.port=7575
app.http.spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
```


app.http-request.http.request.headers-expression=headers

```properties
app.http-request.http.request.body-expression=payload
app.http-request.http.request.expected-response-type=java.lang.String
app.http-request.http.request.headers-expression="{'Content-Type':'application/json'}"
app.http-request.http.request.http-method-expression="'POST'"
app.http-request.http.request.urlExpression="'http://localhost:8585/timeout'"
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=MANUAL
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true
app.http-request.spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
deployer.http-request.local.java-opts=-Dspring.cloud.stream.bindings.input.consumer.backOffMaxInterval=5 -Dspring.cloud.stream.bindings.input.consumer.backOffInitialInterval=5 -Dspring.cloud.stream.bindings.input.consumer.backOffMultiplier=1 -Dspring.cloud.stream.bindings.input.consumer.maxAttempts=3 
app.http.path-pattern=timeout
app.http.server.port=7575
app.http.spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
app.log.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true
deployer.http-request.bootVersion=3
deployer.http.bootVersion=3
deployer.log.bootVersion=3
```
get('http_requestMethod')
#app.http-request.spring.cloud.stream.bindings.input.consumer.backOffInitialInterval=1000
#app.http-request.spring.cloud.stream.bindings.input.consumer.backOffMaxInterval=1000
#app.http-request.spring.cloud.stream.bindings.input.consumer.backOffMultiplier=1
#app.http-request.spring.cloud.stream.bindings.input.consumer.maxAttempts=3



http-source

```shell
java -jar /Users/devtools/integration/scdf/apps/http/http-source-rabbit-4.0.0.jar  --spring.cloud.stream.bindings.output.destination=api-throttle --http.path-pattern=timeout  --server.port=7575 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami
```

Randomly Failures

```shell
curl -X 'POST' \
  'http://localhost:7575/timeout' \
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

Test Timout

```shell
curl -X 'POST' \
  'http://localhost:7575/timeout' \
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
