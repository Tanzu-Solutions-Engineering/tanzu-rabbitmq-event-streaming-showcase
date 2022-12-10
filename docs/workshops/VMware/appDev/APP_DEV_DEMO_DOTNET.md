

# High Availability with GemFire for Redis Applications

Test Case

- Start GemFire Cluster across 2 servers
- There will 1 locators - 1 data node on each servers
- The total of 2 locators and 2 data nodes across two servers
- Push records to 1 GemFire
- Stop data node on Server 1
- Verify no data loass
- Restart data node on Server 1
- Stop one data node on Server 2
- Verify no data loass
- Stop Locator on Server 2 (total lost of one physical server)
- Verify no data loass

# GemFire Startup

Local environment


```shell
export CLASSPATH="/Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-9.15.0/lib/HikariCP-4.0.3.jar:/Users/devtools/repositories/RDMS/PostgreSQL/driver/postgresql-42.2.9.jar:/Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/gemfire-extensions/applications/libs/nyla.solutions.core-1.5.1.jar:/Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/gemfire-extensions/components/gemfire-extensions-core/build/libs/gemfire-extensions-core-1.0.0.jar:/Users/devtools/repositories/IMDG/gemfire/gemfire-for-redis-apps-1.0.1/lib/*"
```

```shell
cd /Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-9.15.0/bin
./gfsh
```
```shell
start locator --name=gf-locator1 --port=10334 --locators="127.0.0.1[10334],127.0.0.1[10434]" --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --jmx-manager-hostname-for-clients=127.0.0.1 --http-service-bind-address=127.0.0.1
```
```shell
start locator --name=gf-locator2 --port=10434 --locators="127.0.0.1[10334],127.0.0.1[10434]"  --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --http-service-port=0 --J="-Dgemfire.jmx-manager-port=1098"
```

```shell
configure pdx --disk-store --read-serialized=true
```

```shell
start server --name=gf-server1 --initial-heap=500m --max-heap=500m  --locators="127.0.0.1[10334],127.0.0.1[10434]"  --server-port=40401 --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1 --start-rest-api=true --http-service-bind-address=127.0.0.1 --http-service-port=9090  --J=-Dgemfire-for-redis-port=6379 --J=-Dgemfire-for-redis-enabled=true --include-system-classpath=true 
```

```shell
start server --name=gf-server2 --initial-heap=500m --max-heap=500m  --locators="127.0.0.1[10334],127.0.0.1[10434]"  --server-port=40402 --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1 --start-rest-api=true --http-service-bind-address=127.0.0.1 --http-service-port=9092  --J=-Dgemfire-for-redis-port=6372 --J=-Dgemfire-for-redis-enabled=true --include-system-classpath=true 
```

# Cloud Publishers


```shell
k apply -f cloud/k8/apps/http-amqp-source/http-amqp-source.yml
```

# Starting Service API

Start App

```shell
cd /Users/Projects/VMware/Tanzu/Use-Cases/Vertical-Industries/VMware-Retail/dev/vmware-pos-data-showcas/applications/pos-service/

export REDIS_CONNECTION_STRING="localhost:6379,localhost:6372,connectRetry=10"
dotnet run
```

# Start Consumer


```shell
cd /Users/Projects/VMware/Tanzu/Use-Cases/Vertical-Industries/VMware-Retail/dev/vmware-pos-data-showcas
cd applications/pos-consumer
export REDIS_CONNECTION_STRING="localhost:6379,localhost:6372,connectRetry=10"
export RABBIT_HOST="localhost"
export RABBIT_USERNAME="guest"
export RABBIT_PASSWORD="guest"
export RABBIT_CLIENT_NAME="pos-consumer"
export CRYPTION_KEY="SECRET"

dotnet run
```


Open Source Publisher in DC

```shell
open http://172.16.100.73
```

```properties
exchange=pos.products
```

```json
{ 
    "id" : "10", 
    "name" : "Ham", 
    "price" : 2.76, 
    "details" : "Complete satisfaction or your money back. Scan for more food information.", 
    "ingredients" : "Contains 2% Or Less Of: Molasses, Fully Hydrogenated Vegetable Oils (Rapeseed And Soybean), Mono And Diglycerides, Salt.", 
    "directions" : "Ready to eat.", 
    "warnings" : " Allergens: Contains ham.", 
    "quantityAmount" : "28oz", 
    "nutrition" :  {
        "totalFatAmount" : 16,
        "cholesterol" :0,
        "sodium" : 140,
        "totalCarbohydrate" : 8,
        "sugars" : 3,
        "protein" : 7
    }}
```


```json
{ 
    "id" : "12", 
    "name" : "Cheese", 
    "price" : 3.16, 
    "details" : "50 calories per 1 tbsp.", 
    "ingredients" : "fruit juice, sugar, and pectin.", 
    "directions" : "Refrigerate after opening.", 
    "warnings" : "Button Will Pop Up When Seal Is Broken.", 
    "quantityAmount" : "18oz", 
    "nutrition" :  {
        "totalFatAmount" : 16,
        "cholesterol" :0,
        "sodium" : 140,
        "totalCarbohydrate" : 8,
        "sugars" : 3,
        "protein" : 7
    }}
```

Get Product data
```shell
curl http://localhost:5001/api/product/10
```
```shell
curl http://localhost:5001/api/product/12
```


----------------------------------

Open DC RabbitMQ Cluster



Open k9s

Kill Rabbit


```shell
k delete pod rabbitmq-server-0
```

- Publish Product


```json
{ 
    "id" : "13", 
    "name" : "Green Juice", 
    "price" : 3.16, 
    "details" : "50 calories per 1 tbsp.", 
    "ingredients" : "fruit juice, sugar, and pectin.", 
    "directions" : "Refrigerate after opening.", 
    "warnings" : "Button Will Pop Up When Seal Is Broken.", 
    "quantityAmount" : "18oz", 
    "nutrition" :  {
        "totalFatAmount" : 16,
        "cholesterol" :0,
        "sodium" : 140,
        "totalCarbohydrate" : 8,
        "sugars" : 3,
        "protein" : 7
    }}
```


```shell
curl http://localhost:5001/api/product/13
```


- Verify Passive DR RabbitMQ (local-edge)


