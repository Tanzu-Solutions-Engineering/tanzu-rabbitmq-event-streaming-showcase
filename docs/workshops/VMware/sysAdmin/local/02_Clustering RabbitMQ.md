Prerequisite

Docker & Minikube

[kubectl(https://kubernetes.io/docs/tasks/tools/)

```json
docker network create tanzu
```

Create Cluster

```shell
cd deployment/local/docker/clustering
docker-compose up -d 
```

- Open Management Console with user/bitnami

```shell
open http://localhost:15672
```


## Configure User

Explore created users

- click Admin -> add a user -> app/app 
- Add Tag (ex: management)
- Create permissions (.* from configure, write and read)

Try logging into management console



# Perf Testing

```shell
docker run -it -p 8080:8080 --hostname rabbitmqperftest --name rabbitmqperftest  --network tanzu --rm pivotalrabbitmq/perf-test:latest com.rabbitmq.perf.PerfTest -x 1  -y 1 -u "queue_test" -a --id "perftest" --uris amqp://user:bitnami@clustering-queue-ram1-1,amqp://user:bitnami@clustering-stats-1,amqp://user:bitnami@clustering-queue-disc1-1 --use-millis --variable-size 2000:30  --rate 100 --quorum-queue --queue app.quorum.queue -c 500 --metrics-prometheus
```


HA Testing

```shell
docker stop clustering-queue-ram1-1
```

- Review RabbitMQ Console
- Review Perf Test Logs
- Restart node

```shell
docker start clustering-queue-ram1-1
```

Stop Node

```shell
docker stop clustering-queue-disc1-1
```

- Review RabbitMQ Console
- Review Perf Test Logs
- Restart node

```shell
docker start clustering-queue-disc1-1
```

Stop Node

```shell
docker stop clustering-stats-1
```

- Review Perf Test Logs
- Restart node


```shell
docker start clustering-stats-1
```


# Cleanup

```shell
cd deployment/local/docker/clustering
docker-compose down
```


# Kubernetes Cluster

Start Minikube

```shell
minikube start  --memory='5g' --cpus='4'
```

Install RabbitMQ Cluster Operator

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

View rabbitmq-system namespace 

```shell
kubectl get namespace rabbitmq-system
```


View PODS in rabbitmq-system

```shell
kubectl get pods -n rabbitmq-system
```

Start Minikube Tunnel

```shell
minikube tunnel
```

Create Cluster

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

Get Status

```shell
kubectl get pods
```

After POD running -> get services

```shell
kubectl get services
```

Get Default User/Password

```shell
kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"

export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo ""
echo "USER:" $ruser
echo "PASWORD:" $rpwd
```


Access DashLoad

Example (use beyond user/password)

```shell
open http://127.0.0.1:15672/
```