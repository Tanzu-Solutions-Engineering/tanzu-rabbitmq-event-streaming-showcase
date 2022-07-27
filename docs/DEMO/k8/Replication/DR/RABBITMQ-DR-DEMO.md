k create namespace secrets-ns

k apply -f cloud/k8/data-services/rabbitmq/DR_REPLICATION/secretExport.yml

k apply -f cloud/k8/data-services/rabbitmq/DR_REPLICATION/secret/secrets.yml

kubectl create secret docker-registry tanzu-rabbitmq-registry-creds -secrets-ns --docker-server "registry.tanzu.vmware.com" --docker-username $HARBOR_USER --docker-password $HARBOR_PASSWORD

# START DEMO

## RabbitMQ Cluster Operator -> Provision 2 Cluster


```shell
kubectl get pods
```

```shell
kubectl apply -f cloud/k8/data-services/rabbitmq/DR_REPLICATION/rabbitmq-replication.yaml
```


```shell
kubectl get pods
```

## Standby Replication Operator -> Verify Exchange/Queues Definitions Replication


**Access Upstream Cluster Dashboard**

```shell
kubectl port-forward service/rabbit-hare 35672:15672
````

USER: replicator  
password: supersecret

**Access Downstream Cluster Dashboard**

```shell
kubectl port-forward service/rabbit-bunny 45672:15672
```

export bunny_user=`kubectl get secret rabbit-bunny-default-user -o jsonpath="{.data.username}"| base64 --decode`
export bunny_pwd=`kubectl get secret rabbit-bunny-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo $bunny_user $bunny_pwd

## Standby Replication Operator -> Setup Warm Standby Replication

```shell
kubectl apply -f cloud/k8/data-services/rabbitmq/DR_REPLICATION/rabbitmq-up_down_streams.yml
```


*HARE is upstream*

##  Standby Replication Operator -> Promote downstream cluster

```shell
kubectl exec rabbit-bunny-server-0 -- rabbitmqctl promote_standby_replication_downstream
```

# High Availability -> Auto healing -> upstream will be primary again

```shell
kubectl delete pods tanzu-bunny-server-0
```


## Message Topology -> Provision a vhost

```shell
kubectl apply -f cloud/k8/tmc/data-services/rabbitmq/vhosts/vhost.yaml
```

## Message Topology ->  Creating a user

```shell
kubectl apply -f cloud/k8/tmc/data-services/rabbitmq/users/publish-consume-user.yaml
````

## Message Topology -> Create Quorum

```shell
kubectl apply -f cloud/k8/tmc/data-services/rabbitmq/queues/quorum-queue.yaml
```

## Application -> Pert Test


```shell
kubectl apply -f cloud/k8/tmc/perf-test/perf-test.yml
```


```shell
k logs -f rabbitmq-perf-test
```


## Scale up nodes -> replicas

kubectl edit rmq tanzu-hare


------------------

# Cleanup

k delete -f cloud/k8/tmc/perf-test/perf-test.yml
k delete -f cloud/k8/tmc/data-services/rabbitmq/full-yml
k delete -f cloud/k8/tmc/data-services/rabbitmq/vhosts/vhost.yaml
k delete -f cloud/k8/tmc/data-services/rabbitmq/users/publish-consume-user.yaml
k delete -f cloud/k8/tmc/data-services/rabbitmq/queues/quorum-queue.yaml

# Tips and commands


```shell
kubectl exec tanzu-bunny-server-0 -- rabbitmqctl delete_orphaned_data_on_standby_replication_downstream_cluster
kubectl exec tanzu-bunny-server-0 -- rabbitmqctl delete_internal_streams_on_standby_replication_upstream_cluster
```



kubectl exec tanzu-bunny-server-0 -- rabbitmqctl list_vhosts_available_for_standby_replication_recovery

kubectl exec tanzu-bunny-server-0 -- rabbitmqctl promote_standby_replication_downstream --start-from-scratch --all-available
kubectl exec tanzu-bunny-server-0 -- rabbitmqctl display_standby_promotion_summary

kubectl exec tanzu-bunny-server-0 -- rabbitmq-diagnostics inspect_local_data_available_for_standby_replication_recovery

kubectl exec tanzu-hare-server-0 -- rabbitmqctl disconnect_standby_replication_downstream

kubectl exec tanzu-bunny-server-0 -- rabbitmqctl connect_standby_replication_downstream

kubectl get packageinstall tanzu-rabbitmq -o yaml

```text
Standby Replication plugin:

connect_standby_replication_downstream                           Enables a downstream standby message replication on target node
delete_all_data_on_standby_replication_cluster                   Deletes all data on a multi-DC replication cluster
delete_internal_streams_on_standby_replication_upstream_cluster  Deletes the orphaned data on a multi-DC replication downstream cluster
delete_orphaned_data_on_standby_replication_downstream_cluster   Deletes the orphaned data on a multi-DC replication downstream cluster
disconnect_standby_replication_downstream                        Disconnects standby message replication on node from its upstream
display_disk_space_used_by_standby_replication_data              Displays disk space used by multi-DC replication data on target node
display_standby_promotion_summary                                Displays a summary of the virtual hosts and messages recovered during a multi-DC downstream promotion
list_vhosts_available_for_standby_replication_recovery           Lists virtual hosts available for multi-DC replication recovery on target node
promote_standby_replication_downstream                           Promotes a downstream standby message replication on target node
promote_standby_replication_downstream_cluster                   Promotes a downstream standby message replication cluster
set_standby_replication_upstream_endpoints                       Sets upstream connection settings for standby message replication on target node

```
