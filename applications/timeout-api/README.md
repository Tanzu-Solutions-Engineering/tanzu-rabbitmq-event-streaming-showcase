
```shell
api=http --port=7575 --path-pattern=timeout | http-request --body-expression=payload --http-method-expression="'POST'" --url-expression="'http://localhost:8585/timeout'" --expected-response-type=java.lang.String | log
```

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