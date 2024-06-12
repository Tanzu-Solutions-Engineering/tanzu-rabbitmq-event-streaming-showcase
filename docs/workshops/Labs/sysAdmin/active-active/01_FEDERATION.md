# LAB 01 - Federation

This lab provides details to install RabbitMQ using docker.

**Prerequisite**

Create the docker network

```shell
docker network create tanzu
```

#  Install RabbitMQ Broker

- Run RabbitMQ Upstream
```shell
docker run --name rabbitmqUP  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 3672:3672  -p 35672:15672  bitnami/rabbitmq:3.13.1 
```

- Run RabbitMQ Downstream 1
```shell
docker run --name rabbitmqDOWN1  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 4672:3672  -p 45672:15672  bitnami/rabbitmq:3.13.1 
```

Check Upstream
```shell
docker logs rabbitmqUP -f
```

Check Downstream
```shell
docker logs rabbitmqDOWN1 -f
```


Enable Federation

```shell
docker exec -it  rabbitmqUP rabbitmq-plugins enable rabbitmq_federation_management
docker exec -it  rabbitmqDOWN1 rabbitmq-plugins enable rabbitmq_federation_management
```

Adds a federation upstream named "origin" to downstream
```shell
docker exec -it  rabbitmqDOWN1  rabbitmqctl set_parameter federation-upstream origin '{"uri":"amqp://user:bitnami@rabbitmqUP:5672"}'
```


Adds a federation policy to downstream
```shell
docker exec -it  rabbitmqDOWN1  rabbitmqctl set_policy exchange-federation "^federated\." '{"federation-upstream-set":"all"}'  --priority 10  --apply-to exchanges
```

Open upstream (user credentials: user:bitnami)

```shell
open http://localhost:35672/#/exchanges
```

Create topic exchange "federated.test"


Open downstream (user credentials: user:bitnami)
```shell
open http://localhost:45672/#/exchanges
```

Create topic exchange "federated.test"


-------------------
# Testing


Create queues and binding to a queue for **federated.test** exchange in upstream

![docs/upstream-binding.png](docs/upstream-binding.png)

Create queues and binding to a queue for **federated.test** exchange in downstream

![docs/downstream-binding.png](docs/downstream-binding.png)


Publish message in upstream 

![docs/published.png](docs/published.png)


Message will be sent to queues in both clusters

![docs/published.png](docs/published.png)

# Cleanup


```shell
docker rm -f rabbitmqUP
docker rm -f rabbitmqDOWN1
```