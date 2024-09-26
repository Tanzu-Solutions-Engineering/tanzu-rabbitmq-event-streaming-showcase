

```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

```shell
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install prometheus  prometheus-community/prometheus
```

```shell
helm install  grafana grafana/grafana -f deployment/cloud/k8/data-services/rabbitmq/monitoring/values.yaml

```
Get password

```shell
kubectl get secret --namespace default grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
```

Grafana ID

10991


```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```

kubectl annotate pods --all prometheus.io/path=/metrics prometheus.io/port=15692 prometheus.io/scheme=http prometheus.io/scrape=true -n default



----------------------------------

```shell
helm install grafana grafana/grafana --set service.type=LoadBalancer --set service.port=9696
```

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/rabbitmq-3-node.yml
```


kubectl annotate pods --all prometheus.io/path=/metrics prometheus.io/port=15692 prometheus.io/scheme=http prometheus.io/scrape=true -n default