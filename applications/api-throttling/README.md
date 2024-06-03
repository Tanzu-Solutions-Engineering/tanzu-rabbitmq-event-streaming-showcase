http-source

```shell
java -jar /Users/devtools/integration/scdf/apps/http/http-source-rabbit-4.0.0.jar  --spring.cloud.stream.bindings.output.destination=api-throttle --http.path-pattern=timeout  --server.port=7575
```



```shell
java -jar /Users/devtools/integration/scdf/apps/http/http-source-rabbit-4.0.0.jar  --spring.cloud.stream.bindings.output.destination=api-throttle --http.path-pattern=timeout  --server.port=7575 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami
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