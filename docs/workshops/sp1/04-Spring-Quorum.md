# Spring Add to Quorum Queues

## Pre-requisite
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
 k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml

## Wait for  Rabbit to start 
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app monitoring

#--------------------
# Setup GemFire data store


## step 1 - create Account GemFire region/table 

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Account --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION"

#--------------------
# Build account-geode-sink Docker Images 

## step 1 - Change to project dir

    cd ~/projects/rabbitmq/tanzu-rabbitmq-event-streaming-showcase/

## step 2 - build docker
    
mvn -pl applications/account-geode-sink -am spring-boot:build-image

## step 3 - load docker to kubernetes kind

kind load docker-image account-geode-sink:0.0.1-SNAPSHOT

## step 4 - starts app

k apply -f cloud/k8/apps/account-geode-sink/account-geode-sink.yml

#--------------------
# Build account-http-sourceDocker Images

## step 1 - build docker

    mvn -pl applications/account-http-source -am spring-boot:build-image

## step 2 - load docker to kubernetes kind

    kind load docker-image account-http-source:0.0.1-SNAPSHOT

## step 3 - starts app

  k apply -f cloud/k8/apps/account-http-source

## step 4 - start app see pod with name account-geode-sink, then control^C

watch kubectl get pods


#--------------------
# Verify quorum queue publisher/consumer

## step 1 - port forward to access source

k port-forward deployment/account-http-source 8080:8080 &


## step 2 - port forward Rabbit Cluster dashboard
k port-forward service/rabbitmq 15672:15672 &

## step 3 - Access Rabbit Cluster dashboard
 In Web browser open 
 http://localhost:15672

user: app
password: CHANGEME

## step 4 - Access Source App

In Web browser open
http://localhost:8080

Try account-publisher

```shell
k apply -f cloud/k8/apps/account-geode-sink/account-geode-sink.yml
```

watch kubectl get pods

k apply -f cloud/k8/apps/account-generator-source/account-generator-source.yml



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
