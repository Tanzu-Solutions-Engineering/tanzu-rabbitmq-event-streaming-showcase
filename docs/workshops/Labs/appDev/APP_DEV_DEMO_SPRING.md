
```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/Spring/dev/spring-modern-data-architecture
java -jar applications/retail-web-app/target/retail-web-app-0.0.1-SNAPSHOT.jar --retail.customer.id=nyla
```

Open application

```shell
open http://localhost:8080
```



# Start Analytics Application


Login as retail user

```shell
psql -d postgres -U retail 
```

Verify database access in psql

```shell
\dt
```


Run application

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/Spring/dev/spring-modern-data-architecture
java -jar applications/retail-analytics-app/target/retail-analytics-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```


- Login with default guest/guest
- Goto Exchanges -> retail.products
- REQUIRED: Add Header
    - contentType=application/json

```json
[
  {"id" : "sku1", "name" : "Peanut butter"},
  {"id" : "sku2", "name" : "Jelly"},
  {"id" : "sku3", "name" : "Bread"},
  {"id" : "sku4", "name" : "Milk"}
]
```

- Click Publish Message

This will load products into Postgres

Verify Products in Postgres psql with retail login user

```shell
psql -d postgres -U retail 
```

```sql
 select * from products;
```

# Customer Orders

```shell
open http://localhost:15672
```

Steps

- Login with default guest/guest
- Goto Exchanges -> retail.customer.orders
- Click publish message




----------------------------
## Reload Products from stream

In psql

```sql
 delete from products;
```

Verify deleted (0 rows)

```sql
select * from products;
```


Kill analytics application


Run application with replay flag

```shell
java -jar applications/retail-analytics-app/target/retail-analytics-app-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.replay=true --spring.profiles.active=local 
```

Verify producted reloaded from RabbitMQ

```sql
select * from products;
```


--------------------

Publish the following JSON an order



- Buy Milk, Peanut and butter

Replace nyla at needed

exchange: retail.customer.orders

```json
  {"id":4,"customerIdentifier":{"customerId":"nyla"},
  "productOrders":[
    {"productId":"sku4","quantity":1},
    {"productId":"sku1","quantity":1},
    {"productId":"sku2","quantity":1}
  ]}
```


- Buy Peanut Butter and Jelly

```json
  {"id":1,"customerIdentifier":{"customerId":"nyla"},
  "productOrders":[
    {"productId":"sku1","quantity":1},
    {"productId":"sku2","quantity":1}
  ]}
```

- Buy Bread
-
```json
  {"id":2,"customerIdentifier":{"customerId":"nyla"},
  "productOrders":[
    {"productId":"sku3","quantity":1}
  ]}
```


- Buy Milk

```json
  {"id":3,"customerIdentifier":{"customerId":"nyla"},
  "productOrders":[
    {"productId":"sku4","quantity":1}
  ]}
```

