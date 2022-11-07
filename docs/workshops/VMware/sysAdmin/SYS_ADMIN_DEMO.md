Prerequiste

- Install RabbitMQ Commercial


DEMO

- Install Cluster

```shell
kubectl apply -f cloud/k8/data-services/rabbitmq/vmware-rabbitmq-1-node.yml
```


Get Services

```shell
kubectl get services
```


```shell
/Users/devtools/integration/messaging/rabbit/rabbit-devOps/kubernetes/getcredentials.sh 
```


Configure User

```shell
 k apply -f cloud/k8/data-services/rabbitmq/MessageTopology/users/admin-user.yml
```

Configure Read/Writes


```shell
k apply -f cloud/k8/data-services/rabbitmq/MessageTopology/users/admin-user-permission.yml
```

# Exchanges and Queues

```shell
k apply -f cloud/k8/data-services/rabbitmq/MessageTopology/Exchange-Queues/pos-topology.yaml
```

# Federations

```shell
rabbitmqctl set_parameter federation-upstream origin '{"uri":"amqp://admin:admin@IP-ADDRESS:5672"}'
```


Adds a policy named "exchange-federation"

```shell
rabbitmqctl set_policy exchange-federation "^pos\." '{"federation-upstream-set":"all"}'  --priority 10 --apply-to exchanges
```

- Create exchange Locally
- pos.product
- Create queues: pos-product-catalog
- Publish Message in Cloud


```json
{ 
    "id" : "2", 
    "name" : "Jelly", 
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

Shovel Example

Create queue local

- Create queues: pos-product-catalog-dr


cd deployment/local/docker
./hare.sh


Open Browser

http://localhost:15670


Create queue

- pos-product-catalog

```shell
rabbitmqctl set_parameter shovel pos-shovel '{"src-protocol": "amqp091", "src-uri": "amqp://", "src-queue": "pos-product-catalog-dr", "dest-protocol": "amqp091", "dest-uri": "amqp://localhost:5670", "dest-queue": "pos-product-catalog"}'
```

Set Shovel Status


Publish Message in Cloud


```json
{ 
    "id" : "1", 
    "name" : "Peanut Butter", 
    "price" : 2.76, 
    "details" : "Complete satisfaction or your money back. Scan for more food information.", 
    "ingredients" : "Roasted Peanuts, Contains 2% Or Less Of: Molasses, Fully Hydrogenated Vegetable Oils (Rapeseed And Soybean), Mono And Diglycerides, Salt.", 
    "directions" : "Ready to eat.", 
    "warnings" : " Allergens: Contains Peanuts.", 
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

# Scalability


```shell
k get RabbitMQCluster
```

Increase Replicas

```shell
k edit RabbitMQCluster rabbitmq
```

Kill first nodes

```shell
k exec -it  rabbitmq-server-0 bash
```

```shell
ps -ef | grep beam
```


Publish Message


```json
{ 
    "id" : "3", 
    "name" : "Milk", 
    "price" : 2.76, 
    "details" : "Complete satisfaction or your money back. Scan for more food information.", 
    "ingredients" : "%2 Milk", 
    "directions" : "Best sold by Thursday.", 
    "warnings" : " Contains lactate.", 
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