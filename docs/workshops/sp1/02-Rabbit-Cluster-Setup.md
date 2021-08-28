*RabbitMq*

    kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
    k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml

```shell


## step 3 - wait for rabbitmq-server-0-2 then Control^C
  watch kubectl get pods


# ------------------------------
# Add Monitoring app user
     
##step 1 - add user

kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME

##step 2 - set user permissions

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"

##step 3 - set tag to access the admin dashboard

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app monitoring

k port-forward rabbitmq-server-0 15672:15672

CHROME
open http://localhost:15670

```