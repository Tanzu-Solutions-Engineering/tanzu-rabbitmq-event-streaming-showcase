
```shell
api=http --port=7575 --path-pattern=timeout | http-request --body-expression=payload --http-method-expression=headers.httpMethod --url-expression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String --spring.cloud.stream.rabbit.bindings.input.consumer.acknowledgeMode=MANUAL --spring.cloud.stream.rabbit.bindings.input.consumer.autoBindDlq=true | log
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