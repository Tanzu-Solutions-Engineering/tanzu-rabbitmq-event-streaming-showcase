# Lab 04: Management, Monitoring & Troubleshooting


**Prerequisite**
- Docker & Minikube
- [kubectl(https://kubernetes.io/docs/tasks/tools/)

Start Minikube (if not started)

```shell
minikube start  --memory='5g' --cpus='4'
```

# 1 - Install Message Topology Operator

First, install cert-manager version 1.2.0+ on your cluster. For example, for version 1.3.1, run:

```shell
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.3.1/cert-manager.yaml
```

Wait for cert manager
```shell
kubectl wait pod -l=app.kubernetes.io/instance=cert-manager --for=condition=Ready --timeout=160s -n cert-manager
```


Then, to install the Operator, run the following command:

```shell
kubectl apply -f https://github.com/rabbitmq/messaging-topology-operator/releases/latest/download/messaging-topology-operator-with-certmanager.yaml
```

Wait for operator
```shell
kubectl wait pod -l=app.kubernetes.io/name=messaging-topology-operator --for=condition=Ready --timeout=160s -n rabbitmq-system
```

# 2 - Use Message Topology

Create User
```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/users/app-user.yaml
```

Create Vhost

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/vhosts/vhost.yaml
```

Adder Permission

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/permissions/app-permission.yaml
```

Create Quorum Queue

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/queues/quorum-queue.yaml
```

Create Stream

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/queues/stream-queue.yaml
```

Create Lazy Queue

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/queues/lazy-queue.yaml
```

Create Exchange - Fanout

```shell
kubectl apply -f  deployment/cloud/k8/data-services/rabbitmq/MessageTopology/examples/exchanges/fanout-exchange.yaml
```

# 2 - Perf Test Monitoring & Troubleshooting 

```shell
kubectl apply -f deployment/cloud/k8/apps/rabbitmq-perf-test/perf-test.yml
```

Tail logs

```shell
 kubectl logs deployment/perf-test -f
```

HA Monitoring and Troubleshooting

- View connections in management console
- Kill server node connected to perftest

Example: 
```shell
kubectl delete pod rabbitmq-server-0 --force=true
```

- Wait for Server to restarted
- View connections in management console
- Kill server node connected to perftest

Example:
```shell
kubectl delete pod rabbitmq-server-2 --force=true
```

Force a network partition of by killing 2 servers 

Example
```shell
kubectl delete pod rabbitmq-server-0 rabbitmq-server-1 --force=true
```

# 3 - Debugging Network Partitions


Check RabbitMQ cluster status

Example 
```shell
kubectl exec rabbitmq-server-0 -- rabbitmq-diagnostics cluster_status
```

Example output

```
Node rabbit@rabbitmq-server-0.rabbitmq-nodes.default cannot communicate with rabbit@rabbitmq-server-2.rabbitmq-nodes.default
```

Restart problem broker node

```shell
kubectl delete pod rabbitmq-server-2
```


# 3 - Clean up

```shell
kubectl delete deployment perf-test
```
```shell
kubectl delete RabbitMQCluster rabbitmq
```