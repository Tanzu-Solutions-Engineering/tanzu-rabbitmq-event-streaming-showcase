```shell
kubectl create secret docker-registry tanzu-vmware-secret --docker-server=registry.tanzu.vmware.com --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD
```

See https://github.com/rabbitmq/cluster-operator/tree/main/docs/examples/tls

```shell
k apply -f cloud/k8/data-services/rabbitmq/certificates/rabbitmq-self-sign-cert.yml
```

```shell
kubectl apply -f cloud/k8/data-services/rabbitmq/certificates/rabbit-cluster-tls.yml
```


## Testings


set -e

kubectl exec -it tls-server-0 -c rabbitmq -- openssl s_client -connect tls-nodes.default.svc.cluster.local:5671 </dev/null

kubectl exec -it tls-server-0 -c rabbitmq -- openssl s_client -connect tls-nodes.default.svc.cluster.local:15671 </dev/null

