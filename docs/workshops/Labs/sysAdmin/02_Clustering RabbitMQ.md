# LAB 02 - Cluster RabbitMQ

This lab demonstrates ways to set up a RabbitMQ cluster

**Prerequisite**

- Docker & Minikube
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
- Download Source Code

Example with git
```shell
git clone https://github.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase.git
cd tanzu-rabbitmq-event-streaming-showcase
```


Create docker network if needed

```shell
docker network create tanzu
```

# 1 - Create Cluster using Docker

- Create cluster using docker compose

```shell
cd deployment/local/docker/clustering
docker-compose up -d 
```

Wait for broker to start 

Example see message - "started TCP listener on [::]:5672"

```shell
docker logs clustering-queue-disc1-1
```

- Open Management Console with user/bitnami
```shell
open http://localhost:15672
```


## Configure User using management console (Optional)

Explore created users

- click Admin -> add a user -> app/app 
- Add Tag (ex: management)
- Create permissions (.* from configure, write and read)

Try logging into management console


# 2 - Perf Testing

- Run Performance test application
```shell
docker run -it -p 8080:8080 --hostname rabbitmqperftest --name rabbitmqperftest  --network tanzu --rm pivotalrabbitmq/perf-test:latest com.rabbitmq.perf.PerfTest -x 1  -y 1 -u "queue_test" -a --id "perftest" --uris amqp://user:bitnami@clustering-queue-ram1-1,amqp://user:bitnami@clustering-stats-1,amqp://user:bitnami@clustering-queue-disc1-1 --use-millis --variable-size 2000:30  --rate 100 --quorum-queue --queue app.quorum.queue -c 500 --metrics-prometheus
```


# 3 - HA Testing

- Stop a RabbitMQ node
```shell
docker stop clustering-queue-ram1-1
```

Testing 
- Review RabbitMQ Console
- Review Perf Test Logs


Restart node

```shell
docker start clustering-queue-ram1-1
```

Stop another RabbitMQ Node

```shell
docker stop clustering-queue-disc1-1
```

- Review RabbitMQ Console
- Review Perf Test Logs

Restart node

```shell
docker start clustering-queue-disc1-1
```

Stop Node

```shell
docker stop clustering-stats-1
```

- Review Perf Test Logs


Restart node

```shell
docker start clustering-stats-1
```


# 4 - Cleanup Docker Cluster

```shell
cd deployment/local/docker/clustering
docker-compose down
```


# 5 -  Kubernetes Cluster

Start Minikube

```shell
minikube start  --memory='5g' --cpus='4'
```
or
```shell
minikube start  --memory='3g' --cpus='2'
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

View for POD to be running state

Start Minikube Tunnel

```shell
minikube tunnel --bind-address=0.0.0.0
```

Create RabbitMQ Cluster (new terminal)

```shell
kubectl apply -f https://raw.githubusercontent.com/Tanzu-Solutions-Engineering/tanzu-rabbitmq-event-streaming-showcase/main/deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

Get POD Status

```shell
kubectl get pods
```

After POD running -> get services

```shell
kubectl get services
```

Get Default RabbitMQ User/Password


Example (Unix)

```shell
kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"

export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo ""
echo "USER:" $ruser
echo "PASSWORD:" $rpwd
```

Access Management Console 

Example (use above user/password)

```shell
open http://127.0.0.1:15672/
```

Test HA by deleting nodes

```shell
kubectl delete pod rabbitmq-server-1 --force=true
```




# 6 - Clean up cluster

Delete the cluster (from directory tanzu-rabbitmq-event-streaming-showcase)

```shell
kubectl delete -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

Clean Mini Kube
```shell
minikube delete
```
