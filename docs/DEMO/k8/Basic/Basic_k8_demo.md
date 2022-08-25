
```shell
k apply -f cloud/k8/data-services/rabbitmq/vmware-rabbitmq-1-node.yml
```

Get Public URL

```shell
k get services
```

```shell
open http://a1c3b5959f26b48d89387335f391dbb4-1348179783.us-east-2.elb.amazonaws.com:15672
```

## User managements


Get user/password

```shell
/Users/devtools/integration/messaging/rabbit/rabbit-devOps/kubernetes/getcredentials.sh
```

Create new user dealer 

- Create virtual host *Dealer1*
- Create user dealer1/dealer1
- Grant access to Dealer1 Vhost
- dealer1 -> Update user -> administrator



```shell
kubectl create secret generic rabbitmq-dealer1-user \
  --from-literal=username=dealer1 \
  --from-literal=password=dealer1 \
  --from-literal=uri='http://rabbitmq.default.svc:15672'
```


# Messaging - Loosely coupled

- Login as *dealer1/dealer1*
- Create queue **dealer1.workOrder.purchase**
- Create exchange **dealer1.event**

Create binding rules 
- dealer1.event -> queue: dealer1.workOrder.purchase -> routingKey: workOrder.purchase.#

Deploy app

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
kubectl apply -f cloud/k8/apps/http-amqp-source/dealer/dealer-http-source.yml
```

Publish 

exchange: dealer1.event
routing_key= workOrder.purchase.event001

```json
{
    "eventId" : "1",
    "eventDate" : "3-20-2022 00:00:00:000",
    "dateId" : "1",
    "objectName" : "workOrder.purchase"
}
```


Create queue **dealer1.workOrder.sale**
Create queue **dealer1.workOrder.repair**

- dealer1.topic -> queue: dealer1.workOrder.sale -> routingKey: workOrder.sale.#
- dealer1.topic -> queue: dealer1.workOrder.repair -> routingKey: workOrder.repair.#

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
kubectl apply -f cloud/k8/apps/http-amqp-source/dealer/messageTopology/dealer-message-topology.yml
```

exchange: dealer1.topic
routing_key= workOrder.sale.001

```json
{
    "eventId" : "1",
    "eventDate" : "3-20-2022 00:00:00:000",
    "dateId" : "1",
    "objectName" : "workOrder.sale"
}
```



exchange: dealer1.topic
routing_key= workOrder.repair.001

```json
{
    "eventId" : "1",
    "eventDate" : "3-20-2022 00:00:00:000",
    "dateId" : "1",
    "objectName" : "workOrder.repair"
}
```

See docs/DEMO/k8/TLS/TLS_demo.md






