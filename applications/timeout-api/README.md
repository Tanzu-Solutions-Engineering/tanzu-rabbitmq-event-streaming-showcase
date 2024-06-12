# Time Out API

Example REST API to validation API Throttling with RabbitMQ.

# Start Rabbit

```shell
docker network create tanzu
```

```shell
docker run --name rabbitmq01  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.11.1
```


------------
# Start SCDF


```shell
--http-method-expression="headers.get('http_requestMethod')" 
```


```shell
api=http --port=7575 --path-pattern=timeout --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled="true" | http-request  --http.request.body-expression=payload --http.request.http-method-expression="'POST'" --http.request.urlExpression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String --spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=MANUAL --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true  --http.request.headers-expression="{'Content-Type':'application/json'}" | log --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled="true"
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


```shell
curl -X 'POST' \
  'http://localhost:8585/timeout' \
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

--------------------

# Standalone Test


http-request-processor


```shell
java -jar /Users/devtools/integration/scdf/apps/http-request/http-request-processor-rabbit-4.0.0.jar --spring.cloud.stream.bindings.input.destination=throttle --spring.cloud.stream.bindings.input.group=http-request --spring.cloud.stream.bindings.output.destination=throttle-api-output --http.request.body-expression=payload --http.request.http-method-expression="'POST'" --http.request.urlExpression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String  --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled="true" --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true   --http.request.headers-expression="{'Content-Type':'application/json'}" --spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterExchange=throttleDlx  --spring.cloud.stream.rabbit.bindings.input.consumer.republishToDlq="true" --server.port=0 --logging.level.root=debug 
```

--spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
--spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=AUTO
--spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true

```shell
java -jar /Users/devtools/integration/scdf/apps/http-request/http-request-processor-rabbit-4.0.0.jar --spring.cloud.stream.bindings.input.destination=throttle --spring.cloud.stream.bindings.input.group=http-request --spring.cloud.stream.bindings.output.destination=throttle-api-output --http.request.body-expression=payload --http.request.http-method-expression="'POST'" --http.request.urlExpression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String --spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=AUTO --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true --spring.cloud.stream.bindings.input.consumer.maxAttempts=3 --spring.cloud.stream.bindings.input.consumer.backOffInitialInterval=3 --spring.cloud.stream.bindings.input.consumer.backOffMaxInterval=3 --spring.cloud.stream.bindings.input.consumer.backOffMultiplier=1 --http.request.headers-expression="{'Content-Type':'application/json'}" --spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterExchange=throttleDlx --spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterExchangeType=topic --spring.cloud.stream.rabbit.bindings.input.consumer.deadLetterRoutingKey="#" --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.deliveryLimit=3 --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.deliveryLimit=3 --spring.cloud.stream.rabbit.bindings.input.consumer.republishToDlq=true --server.port=0 --logging.level.root=debug 
```

--spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami

http-source

```shell
java -jar /Users/devtools/integration/scdf/apps/http/http-source-rabbit-4.0.0.jar  --spring.cloud.stream.bindings.output.destination=throttle --http.path-pattern=timeout  --server.port=7575 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami
```


-----------

Register

```properties
processor.http-request=file:///Users/devtools/integration/scdf/apps/http-request/http-request-processor-rabbit-4.0.0.jar
```


---------------------------------------
# Docker building image

```shell
mvn install
cd applications/timeout-api
mvn package
docker build  --platform linux/amd64,linux/arm64 -t timeout-api:0.0.1-SNAPSHOT .

```

```shell
docker tag timeout-api:0.0.1-SNAPSHOT cloudnativedata/timeout-api:0.0.1-SNAPSHOT
docker push cloudnativedata/timeout-api:0.0.1-SNAPSHOT
```
