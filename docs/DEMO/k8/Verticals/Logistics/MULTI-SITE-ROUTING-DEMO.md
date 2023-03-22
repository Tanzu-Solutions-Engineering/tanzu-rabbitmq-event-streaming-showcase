
## Site 1

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-1.yml
```

Add user

```yaml
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/users/rabbitmq-site-1-user.yml
```

Add Topology 

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site1-topology.yaml
```

```shell
k port-forward  service/rabbitmq-site1 9991:15672
```

USer site1/site1

--------------------

## Hub
```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-hub.yml
```

User
```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/users/rabbitmq-hub-user.yml
```

Topology

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/hub-topology.yaml
```

```shell
k port-forward  service/rabbitmq-hub 9990:15672
```

-------------------
Site 2

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-2.yml
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site2 --for=condition=Ready --timeout=160s
```

User
```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-2-user.yml
```

Topology

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site2-topology.yaml
```

```shell
k port-forward  service/rabbitmq-site2 9992:15672
```
----------------

Site 3

```shell
kubectl apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-3.yml
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq-site3 --for=condition=Ready --timeout=160s
```

User
```yaml
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/rabbitmq-site-3-user.yml
```

Topology

```shell
k apply -f deployment/cloud/k8/data-services/rabbitmq/verticals/transportation_logistics/messsage-topology/site3-topology.yaml
```
```shell
k port-forward  service/rabbitmq-site3 9993:15672
```