https://github.com/tzolov/cdc-fraud-detection-demo

#-----------------------
# GemFire 

# Set GemFire Pre-Requisite

kubectl create namespace cert-manager

helm repo add jetstack https://charts.jetstack.io

helm repo update

helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true

kubectl create namespace gemfire-system


kubectl create secret docker-registry image-pull-secret --namespace=gemfire-system --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

kubectl create rolebinding psp-gemfire --clusterrole=psp:vmware-system-privileged --serviceaccount=gemfire-system:default


# Install the GemFire Operator

## Local
helm install gemfire-operator /Users/devtools/repositories/IMDG/gf-kubernetes/gemfire-operator-1.0.1.tgz --namespace gemfire-system


kubectl apply -f docs/workshops/cdc/demo-services/gemfire.yml

kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Account --type=PARTITION"


#------------------------
# Rabbit MQ
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"

k apply -f docs/workshops/cdc/demo-services/rabbitmq.yml

watch kubectl get pods


##step 1 - add user

kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME

##step 2 - set user permissions

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"

##step 3 - set tag to access the admin dashboard

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app monitoring

k port-forward rabbitmq-server-0 15672:15672 &


## -----------------------

# Postgres

kubectl exec -it postgres-0 -- psql -c "ALTER USER postgres PASSWORD 'CHANGEME'"
kubectl exec -it postgres-0 -- psql -c "drop table Accounts"
kubectl exec -it postgres-0 -- psql -c "CREATE TABLE Accounts ( id varchar(255),  bank_id varchar(255),  label varchar(255), number varchar(255), product_code varchar(255));"

--dry-run -o yaml

#----------------------------

#--------------------
# Build account-rest-serviceDocker Images

## step 1 - build docker

    mvn -pl applications/account-rest-service -am spring-boot:build-image

## step 2 - load docker to kubernetes kind

    kind load docker-image account-rest-service:0.0.1-SNAPSHOT

## step 3 - starts app

k apply -f cloud/k8/apps/account-rest-service

k port-forward deployment/account-rest-service 4001:4001

open http://localhost:4001/index.html

## step 4 - start app see pod with name account-geode-sink, then control^C

watch kubectl get pods


#--------------------
# Build account-geode-sink Docker Images


## step 2 - build docker

mvn -pl applications/account-geode-sink -am spring-boot:build-image

## step 3 - load docker to kubernetes kind

kind load docker-image account-geode-sink:0.0.1-SNAPSHOT


## SCDF 

k port-forward deployment/scdf-server 9393:8080 &


java -jar /Users/devtools/integration/scdf/spring-cloud-dataflow-shell-2.8.1.jar


app import --uri https://dataflow.spring.io/rabbitmq-docker-latest


app register --type sink --name account-geode-sink --uri docker:account-geode-sink:0.0.1-SNAPSHOT

stream create --name account-cdc  --definition "jdbc --jdbc.supplier.query='select * from Accounts' --spring.datasource.password=CHANGEME --spring.datasource.username=postgres --spring.datasource.url=jdbc:postgresql://postgres/postgres  --spring.rabbitmq.port=5672 | account-geode-sink --spring.cloud.stream.bindings.accountConsumer-in-0.destination=account-cdc --spring.cloud.stream.bindings.accountConsumer-in-0.group=jdbc.account-cdc --server.port=8080 --spring.application.name=scf-account-sink --spring.rabbitmq.stream.host=rabbitmq --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334] --spring.rabbitmq.port=5672"

stream deploy --name account-cdc --properties "deployer.jdbc.kubernetes.requests.memory=1Gi, deployer.account-geode-sink.kubernetes.requests.memory=1Gi, deployer.jdbc.kubernetes.limits.memory=1Gi, deployer.account-geode-sink.kubernetes.limits.memory=1Gi"

stream create --name account-cdc-log --definition ":account-cdc > log"


## CDC test

kubectl exec -it postgres-0 -- psql -c "insert into Accounts ( id,  bank_id, label, number, product_code) values('my-id',  'my-bank_id', 'my-label', 'my-number', 'my-product_code');"

k port-forward gemfire1-locator-0 7070:7070

open http://localhost:7070/pulse

kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "query --query='select id, bank_id, label, number, product_code from /Account'"

kubectl exec -it postgres-0 -- psql -c "insert into Accounts ( id,  bank_id, label, number, product_code) values('afa-id',  'afa-bank_id', 'another-label', 'afa-number', 'afa-product_code');"