Start SCDF



"{name:'Nikola',dob:'10-July-1856'}"

```shell
--http-method-expression="headers.get('http_requestMethod')" 
```

org.springframework.messaging


```shell
api=http --port=7575 --path-pattern=timeout --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled="true" | http-request --body-expression=payload --http-method-expression="'POST'" --url-expression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String --spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=MANUAL --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.deliveryLimit=3 --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.deliveryLimit=1 --spring.cloud.stream.bindings.input.consumer.maxAttempts=1 --spring.cloud.stream.bindings.input.consumer.backOffInitialInterval=10 --spring.cloud.stream.bindings.input.consumer.backOffMaxInterval=30 | log --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled="true"
```


app.http-request.http.request.headers-expression=headers

```properties
app.http-request.http.request.body-expression=payload
app.http-request.http.request.expected-response-type=java.lang.String
app.http-request.http.request.headers-expression={'Content-Type':'application/json'}
app.http-request.http.request.http-method-expression='POST'
app.http-request.http.request.url-expression='http://localhost:8585/timeout'
app.http-request.spring.cloud.stream.bindings.input.consumer.backOffInitialInterval=10
app.http-request.spring.cloud.stream.bindings.input.consumer.backOffMaxInterval=30
app.http-request.spring.cloud.stream.bindings.input.consumer.maxAttempts=1
app.http-request.spring.cloud.stream.default.consumer.maxAttempts=1
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=MANUAL
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true
app.http.spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.deliveryLimit=1
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true
app.http.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true
app.log.spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.deliveryLimit=1
app.http-request.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true
app.http-request.spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
app.http.path-pattern=timeout
app.http.server.port=7575
app.http.spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true
app.log.spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true
deployer.http-request.bootVersion=3
deployer.http.bootVersion=3
deployer.log.bootVersion=3
app.http-request.logging.level.org.springframework.messaging=DEBUG
app.http-request.logging.org.springframework.integration=DEBUG
```
get('http_requestMethod')


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


```shell

```
http-request-processor

```shell
java -jar /Users/devtools/integration/scdf/apps/http-request/http-request-processor-rabbit-4.0.0.jar --spring.cloud.stream.bindings.input.destination=throttle --spring.cloud.stream.bindings.input.group=http-request --spring.cloud.stream.bindings.output.destination=api-output --http.request.body-expression=payload --http.request.http-method-expression="'POST'" --http.request.urlExpression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String --spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=MANUAL --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.dlqQuorum.deliveryLimit=3 --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true --spring.cloud.stream.rabbit.bindings.output.producer.quorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true --spring.cloud.stream.rabbit.bindings.input.consumer.quorum.deliveryLimit=1 --spring.cloud.stream.bindings.input.consumer.maxAttempts=1 --spring.cloud.stream.bindings.input.consumer.backOffInitialInterval=10 --spring.cloud.stream.bindings.input.consumer.backOffMaxInterval=30 --http.request.headers-expression="{'Content-Type':'application/json'}" --server.port=0
```


```shell
java -jar /Users/devtools/integration/scdf/apps/http/http-source-rabbit-4.0.0.jar  --spring.cloud.stream.bindings.output.destination=throttle --http.path-pattern=timeout  --server.port=7575
```