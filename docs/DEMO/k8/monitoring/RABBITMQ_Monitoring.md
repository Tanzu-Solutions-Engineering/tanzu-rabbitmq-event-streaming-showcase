# K8 RabbitMQ monitoring details with Grafana/Prometheus

## Install RabbitMQ Cluster Operator

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

## Install Prometheus with scrape_interval 

Note: Additional option --set service.type=LoadBalancer --set service.port=9696

```shell
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install prometheus  prometheus-community/prometheus --set server.global.scrape_interval=5s --set server.global.scrape_timeout=4s
```

## Install Grafana

```shell
helm install  grafana grafana/grafana -f deployment/cloud/k8/data-services/rabbitmq/monitoring/values.yaml
```
 
Get  Grafana password

```shell
kubectl get secret --namespace default grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
```

Also Note RabbiTMQ Grafana ID = 10991



## Install RabbitMQ Cluster 

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

Annotate Rabbit PODs
```shell
kubectl annotate pods --all prometheus.io/path=/metrics prometheus.io/port=15692 prometheus.io/scheme=http prometheus.io/scrape=true prometheus.io/scrape_interval=5s -n default

```
