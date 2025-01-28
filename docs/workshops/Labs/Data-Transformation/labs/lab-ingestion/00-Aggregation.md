


```shell
http-aggregator=http --port=8181 | aggregator --aggregator.message-store-type=jdbc --spring.datasource.url=jdbc:h2:mem:test --correlation="#jsonPath(payload, 'id')" --spring.datasource.schema="classpath://org/springframework/integration/jdbc/schema-h2.sql" --release=size()==5 --aggregation="#this.![@jacksonObjectMapper.readValue(payload, T(java.util.Map))]" | log --level=INFO

```








```properties
app.aggregator.correlation="jsonPath(payload, 'id')"
app.aggregator.message-store-type=jdbc
app.aggregator.spring.datasource.schema=classpath://org/springframework/integration/jdbc/schema-h2.sql

```

http-aggregator=http --port=8181 --allowed-headers=correlationId | aggregator --aggregator.message-store-type=jdbc --spring.datasource.url=jdbc:h2:mem:test --spring.datasource.schema=classpath://org/springframework/integration/jdbc/schema-h2.sql --correlation=payload.id | log --level=INFO


```shell
curl -X 'POST' \
  'http://localhost:8181' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'correlationId: 0001' \
  -d '{
  "id": "001",
  "name": "Event Demo 1",
  "accountType": "test",
  "status": "OPEN",
  "notes": "Testing 123",
  "location": {
    "id": "001.001",
    "address": "1 Straight Stree",
    "cityTown": "Wayne",
    "stateProvince": "NJ",
    "zipPostalCode": "55555",
    "countryCode": "US"
  }
}'

curl -X 'POST' \
  'http://localhost:8181' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'correlationId: 0002' \
  -d '{
  "id": "002",
  "name": "Event Demo 2",
  "accountType": "test",
  "status": "OPEN",
  "notes": "Testing 123",
  "location": {
    "id": "001.001",
    "address": "2 Straight Stree",
    "cityTown": "Wayne",
    "stateProvince": "NJ",
    "zipPostalCode": "55555",
    "countryCode": "US"
  }
}'
```

jsonPath(payload, '$.id')
size()==2

http-aggregator=http --port=8181 | aggregator --aggregator.message-store-type=jdbc --spring.datasource.url=jdbc:h2:mem:test --correlation="#jsonPath(payload, 'id')" --spring.datasource.schema="classpath://org/springframework/integration/jdbc/schema-h2.sql" --release=size()==2 | log --level=INFO

http-aggregator=http --port=8181 | aggregator --aggregator.message-store-type=jdbc --spring.datasource.url=jdbc:h2:mem:test --spring.datasource.schema=classpath://org/springframework/integration/jdbc/schema-h2.sql | log --level=INFO