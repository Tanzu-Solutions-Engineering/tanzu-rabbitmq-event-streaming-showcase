# LAB 04 - 


**Prerequisite**

- Kubernetes cluster
- RabbitMQ Operator
- brew install qemu

```shell
minikube start  --memory='5g' --cpus='4'  --driver=qemu
```

Setup GemFire Operator

```shell
./deployment/cloud/k8/data-services/gemfire/gf-k8-setup.sh
```

Create GemFire Cluster

```shell
kubectl apply -f deployment/cloud/k8/data-services/gemfire/gemfire.yml
```


# Cleanup

```shell
helm uninstall gemfire-operator --namespace gemfire-system
helm uninstall gemfire-crd --namespace gemfire-system
```