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

Down 2
```shell
docker run --name rabbitmqDOWN1  --network tanzu --rm -d -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 4672:3672  -p 45672:15672  bitnami/rabbitmq:3.13.1 
```


```shell
docker logs rabbitmqUP -f
```

```shell
docker logs rabbitmqDOWN1 -f
```

```shell
docker exec -it  rabbitmqUP  rabbitmq-plugins list
```


Enable Federation

```shell
docker exec -it  rabbitmqUP rabbitmq-plugins enable rabbitmq_federation_management
docker exec -it  rabbitmqDOWN1 rabbitmq-plugins enable rabbitmq_federation_management
```

```shell
# Adds a federation upstream named "origin"
docker exec -it  rabbitmqDOWN1  rabbitmqctl set_parameter federation-upstream origin '{"uri":"amqp://user:bitnami@rabbitmqUP:5672"}'
```

```shell
docker exec -it  rabbitmqDOWN1  rabbitmqctl set_policy exchange-federation "^federated\." '{"federation-upstream-set":"all"}'  --priority 10  --apply-to exchanges
```


Open upstream

```shell
open http://localhost:35672/#/exchanges
```

Create exchange "federated.test"
```

```shell
rabbitmqadmin declare queue name=my-new-queue durable=false
```

# Cleanup


```shell
docker rm -f rabbitmqUP
docker rm -f rabbitmqDOWN1
```