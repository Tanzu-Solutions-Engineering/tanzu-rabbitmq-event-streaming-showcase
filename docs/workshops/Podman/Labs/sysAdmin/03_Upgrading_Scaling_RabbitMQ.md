# Lab 03: Upgrade & Scaling RabbitMQ


**Prerequisite**

- See [Podman](https://podman-desktop.io/docs/installation) & [Kind](https://podman-desktop.io/docs/kind/installing)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
- Start Kind Cluster
- Download Source Code

Example with git
```shell
git clone https://github.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase.git
cd tanzu-rabbitmq-event-streaming-showcase
```



-----------------------------------------
# 1 - Create RabbitMQ Broker


Install RabbitMQ Operator

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

View PODS in rabbitmq-system

```shell
kubectl get pods -n rabbitmq-system
```

Waited for PODS to be in Running status

Create Rabbit Broker

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/upgrade/rabbitmq-1-node-3.12.13.yml
```

View PODs

```shell
kubectl get pods 
```

Wait for server to be in running state

```shell
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq --for=condition=Ready --timeout=160s
```

View k8 Services

```shell
kubectl get services
```

Get Default User/Password

Example UNIX
```shell
kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"

export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo ""
echo "USER:" $ruser
echo "PASSWORD:" $rpwd
```

Access Management Console


```shell
kubectl port-forward service/rabbitmq 15672:15672 5672:5672
```

Access DashLoad (use above user/password)

```shell
open http://127.0.0.1:15672/
```

Review version

![rabbitmq_version.png](img/rabbitmq_version.png)]

![img.png](img/img.png)


# 2 - Upgrade RabbitMQ Broker

Upgrade version
```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/upgrade/rabbitmq-1-node-3.12.14.yml
```

Enable all features

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl enable_feature_flag all
```

View PODs

```shell
kubectl get pods -w
```

Wait for broker to start

Access Management Console


```shell
kubectl port-forward service/rabbitmq 15672:15672 5672:5672
```


Access Management console (use above user/password)

```shell
open http://127.0.0.1:15672/
```

Note: RabbitMQ Version

# 3 - Scale to 3 Nodes

Scale to 3 nodes
```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/upgrade/rabbitmq-3-node-3.12.14.yml
```


View PODs

```shell
kubectl get pods -w
```

Wait for brokers to start


Access Management console (use above user/password)

```shell
open http://127.0.0.1:15672/
```

*Note the RabbitMQ operator may restart additional nodes as needed when scaling from 1 to 3 nodes*

# 4 - Upgrade 3 Node Cluster

Upgrade RabbitMQ cluster

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/upgrade/rabbitmq-3-node-3.13.yml
```

View PODs

```shell
kubectl get pods -w
```

Wait for all nodes to restart

Access Management console (use above user/password)

```shell
open http://127.0.0.1:15672/
```
