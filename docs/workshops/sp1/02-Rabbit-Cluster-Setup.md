*RabbitMq*

```shell
    kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
    k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml

```


## step 3 - wait for rabbitmq-server-0-2 then Control^C

```shell
  watch kubectl get pods
```


# ------------------------------
# Add Monitoring app user
     
##step 1 - add user

Keep password: password CHANGEME

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME
```

##step 2 - set user permissions

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"
```


##step 3 - set tag to access the admin dashboard

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app monitoring
```

```shell
k port-forward rabbitmq-server-0 15672:15672
```

CHROME

open [http://localhost:15672](http://localhost:15672)

app CHANGEME

```