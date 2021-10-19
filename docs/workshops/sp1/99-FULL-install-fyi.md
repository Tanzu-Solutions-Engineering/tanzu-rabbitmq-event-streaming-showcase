 
# Prerequisite


*Postgres*
Download https://network.pivotal.io/products/tanzu-sql-postgres/
See [Installing Postgres for Kubernetes](https://postgres-kubernetes.docs.pivotal.io/1-1/installing.html)

Example

     kubectl create namespace cert-manager
     helm repo add jetstack https://charts.jetstack.io
     helm repo update
     helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true
    kubectl create secret docker-registry regsecret --docker-server=https://registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

----------

*Postgres*

cd ~/dataServices/postgres
curl -OL https://cloud-native-data.s3.amazonaws.com/postgres-for-kubernetes-v1.2.0.tar.gz
tar xvf postgres-for-kubernetes-v1.2.0.tar.gz
cd postgres-for-kubernetes-v1.2.0/


docker load -i ./images/postgres-instance   
docker load -i ./images/postgres-operator
docker images "postgres-*"
export HELM_EXPERIMENTAL_OCI=1
helm registry login registry.pivotal.io \
--username=$HARBOR_USER --password=$HARBOR_PASSWORD

helm chart pull registry.pivotal.io/tanzu-sql-postgres/postgres-operator-chart:v1.2.0
helm chart export registry.pivotal.io/tanzu-sql-postgres/postgres-operator-chart:v1.2.0  --destination=/tmp/

kubectl create secret docker-registry regsecret \
--docker-server=https://registry.pivotal.io --docker-username=$HARBOR_USER \
--docker-password=$HARBOR_PASSWORD


helm install --wait my-postgres-operator /tmp/postgres-operator/

kubectl get all | grep postgres
------------------------

cd ~/projects/rabbitmq/tanzu-rabbitmq-event-streaming-showcase/
git pull
k apply -f cloud/k8/data-services/postgres

kubectl exec -it postgres-0 -- psql -c "ALTER USER postgres PASSWORD 'CHANGEME'"


-------

*GemFire*


Example

    kubectl create namespace gemfire-system
    cd /Users/devtools/repositories/IMDG/gf-kubernetes

    kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD
    kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD --namespace gemfire-system

    helm install gemfire-operator gemfire-operator-1.0.2.tgz --namespace gemfire-system
    helm ls --namespace gemfire-system

    cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
    k apply -f cloud/k8/data-services/geode/gemfire.yml

    watch kubectl get pods

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION"

*RabbitMq*

    kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
    k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml

```shell

  watch kubectl 
     
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app monitoring

k port-forward rabbitmq-server-0 15672:15672

CHROME
open http://localhost:15670

```



Geode sink

```shell
    
    mvn -pl applications/account-geode-sink -am spring-boot:build-image
    kind load docker-image account-geode-sink:0.0.1-SNAPSHOT
    

```





account-http-source

```shell
    mvn -pl applications/account-http-source -am spring-boot:build-image
    kind load docker-image account-http-source:0.0.1-SNAPSHOT
```


Generator Source
```shell
    mvn -pl applications/stream-account-generator-source -am spring-boot:build-image
    kind load docker-image stream-account-generator-source:0.0.1-SNAPSHOT
    
```

---------------

**Start Demo**
---------------
###

```shell
kubectl exec -it gemfire1-locator-0 -- gfsh -e connect -e "create region --name=Account --type=PARTITION"
```

```shell
k apply -f cloud/k8/apps/account-geode-sink/account-geode-sink.yml
```

watch kubectl get pods

k apply -f cloud/k8/apps/stream-account-generator-source/stream-account-generator-source.yml



k delete -f cloud/k8/apps/source/vehicle-generator-source/gke







--------------------------------------------------------------------

# RabbitMQ

----

## Edge running RabbitMq


  k delete -f cloud/k8/apps/source/vehicle-generator-source/gke

  k port-forward rabbitmq-server-0 5672:5672

  docker run --network edge --name rabbitmqMqttEdge --hostname localhost -it -p 15674:15672  -p  1883:1883  -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream


  docker exec -it rabbitmqMqttEdge bash

  rabbitmqctl -n rabbit add_user mqtt

  rabbitmqctl -n rabbit set_permissions mqtt "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*"
  rabbitmqctl set_user_tags mqtt monitoring

  rabbitmqadmin declare queue name=vehicleSink.vehicleRepositorySink queue_type=quorum arguments='{"x-max-length":10000,"x-max-in-memory-bytes":0}'

  rabbitmqadmin declare binding source=amq.topic  destination=vehicleSink.vehicleRepositorySink routing_key=#

  rabbitmqctl set_parameter shovel dc-shovel  '{"src-protocol": "amqp091", "src-uri": "amqp://", "src-queue": "vehicleSink.vehicleRepositorySink", "dest-protocol": "amqp091", "dest-uri": "amqp://vehicle:security@host.docker.internal", "dest-queue": "vehicleSink.vehicleRepositorySink"}'


SAFARI http://localhost:15674


cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/vehicle-generator-mqtt-source/build/libs/vehicle-generator-mqtt-source-0.0.1-SNAPSHOT.jar

chrome http://34.138.55.178/

kill shovel connection


# Postgres streaming

cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink

docker run --network edge --name rabbitmqStreamEdge --hostname localhost -it -p 15676:15672  -p 5554:5554 -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_stream advertised_host localhost -rabbitmq_stream advertised_port 5554 -rabbitmq_stream tcp_listeners [5554]" --rm pivotalrabbitmq/rabbitmq-stream

FIRE FOX

http://localhost:15676/



*Streaming Postgres*

k port-forward rabbitmq-server-0 5552:5552

docker exec -it rabbitmqStreamEdge bash

rabbitmqctl -n rabbit add_user streaming streaming

rabbitmqctl -n rabbit set_permissions streaming "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*"
rabbitmqctl set_user_tags streaming monitoring

cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/shovel-streaming-app/build/libs/shovel-streaming-app-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.stream.maxLengthGb=90 --rabbitmq.streaming.routing.input.uris="rabbitmq-stream://streaming:streaming@localhost:5554" --rabbitmq.streaming.routing.output.uris="rabbitmq-stream://vehicle:security@localhost:5552" --rabbitmq.streaming.routing.input.stream.name=VehicleStream --rabbitmq.streaming.routing.output.stream.name=VehicleStream

select avg(speed), max(speed), min(speed),  count(*)  
from vehicle_iot.vehicle_telemetry


cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/vehicle-generator-streaming-source/build/libs/vehicle-generator-streaming-source-0.0.5-SNAPSHOT.jar --rabbitmq.streaming.uris=rabbitmq-stream://streaming:streaming@localhost:5554 --delayMs=500


Kill connection


select avg(speed), max(speed), min(speed),  count(*)  
from vehicle_iot.vehicle_telemetry


Scale up instances

k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink/vehicle-telemetry-jdbc-streaming-sink.yml

FAQ

- No space on disk

```shell
docker system prune
```
# ------------------------------------

# SCDF 


wget https://github.com/mikefarah/yq/releases/download/v4.12.1/yq_linux_386.tar.gz -O - |\
tar xz && sudo mv yq_linux_386 /usr/bin/yq

curl -OL https://github.com/vmware-tanzu/carvel-kbld/releases/download/v0.30.0/kbld-linux-amd64
sudo mv kbld-linux-amd64 /usr/bin/kbld
sudo chmod +x /usr/bin/kbld

curl -OL https://github.com/vmware-tanzu/carvel-kapp/releases/download/v0.39.0/kapp-linux-amd64 



./bin/install-dev.sh -m prometheus -d postgresql -o ./services/dev


 
# kubectl wait pod/postgresql-0 --for=condition=Ready

kubectl create secret docker-registry scdf-image-regcred --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

## Prometheus operator
cd /tmp
curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/v0.18.3/install.sh | bash -s v0.18.3
kubectl create -f https://operatorhub.io/install/prometheus.yaml
kubectl get csv -n operators
kubectl apply -f ./services/dev/monitoring


## SCDF Configurations Services

kubectl apply -f ./services/dev/postgresql/secret.yaml
kubectl apply -f ./services/dev/rabbitmq/config.yaml
kubectl apply -f ./services/dev/rabbitmq/secret.yaml

kubectl apply -f ./services/dev/skipper.yaml
kubectl wait pod -l=app=skipper --for=condition=Ready

kubectl apply -f ./services/dev/data-flow.yaml
kubectl wait pod -l=app=scdf-server --for=condition=Ready

kubectl apply -f ./services/dev/monitoring-proxy