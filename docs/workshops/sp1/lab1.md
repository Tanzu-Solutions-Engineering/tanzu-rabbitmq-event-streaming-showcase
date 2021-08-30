Step 1: Insall rabbitmq operator
```
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
```

Step 2:  Install rabbitmq topology Operrator
```
kubectl apply -f https://github.com/rabbitmq/messaging-topology-operator/releases/latest/download/messaging-topology-operator-with-certmanager.yaml
```



Step 3:  Verify the pods are in running state

kubectl get po -n rabbitmq-system

NAME                                          READY   STATUS    RESTARTS   AGE

messaging-topology-operator-f74cbdc5f-l96sb   1/1     Running   0          21m

rabbitmq-cluster-operator-598d56f98f-8d8q5    1/1     Running   0          21m
