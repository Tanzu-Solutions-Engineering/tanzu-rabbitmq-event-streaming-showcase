
```shell
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.11.0/cert-manager.yaml
```

```shell
helm registry login registry.tanzu.vmware.com \
--username=$HARBOR_USER \
--password=$HARBOR_PASSWORD
```


```shell
helm pull oci://registry.tanzu.vmware.com/tanzu-sql-postgres/postgres-operator-chart --version v2.0.2 --untar --untardir /tmp
```


```shell
kubectl create secret docker-registry regsecret \
--docker-server=https://registry.tanzu.vmware.com/ \
--docker-username=$HARBOR_USER \
--docker-password=$HARBOR_PASSWORD
```

```shell
kubectl create secret docker-registry regsecret \
--docker-server=https://registry.tanzu.vmware.com/ \
--docker-username=$HARBOR_USER \
--docker-password=$HARBOR_PASSWORD -n sql-system
```


```shell
k create namespace sql-system 
```

```shell
helm install my-postgres-operator /tmp/postgres-operator/ \
  --namespace=sql-system \
  --create-namespace \
  --wait    
```

```shell
k get pods -n sql-system
```