https://github.com/tzolov/cdc-fraud-detection-demo

#------------------------
# Rabbit MQ
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml

watch kubectl get pods


##step 1 - add user

kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME

##step 2 - set user permissions

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"

##step 3 - set tag to access the admin dashboard

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app monitoring

k port-forward rabbitmq-server-0 15672:15672 &


## -----------------------

# SQL Server

docker pull mcr.microsoft.com/mssql/server:2017-CU24-ubuntu-16.04
kind load docker-image mcr.microsoft.com/mssql/server:2017-CU24-ubuntu-16.04
k run sqlservercdc --image=mcr.microsoft.com/mssql/server:2017-CU24-ubuntu-16.04 --port 1433 --env=ACCEPT_EULA=Y --env=MSSQL_PID=Standard --env=SA_PASSWORD=Password! --env=MSSQL_AGENT_ENABLED=true  

k exec -it sqlservercdc -- bash

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Password!

SELECT db_name();
go

CREATE TABLE Accounts ( acct_id varchar(255),  acct_nm varchar(255));
go
select * from Accounts;
go

--dry-run -o yaml

#----------------------------
## SCDF 

k port-forward deployment/scdf-server 9393:8080 &



java -jar /Users/devtools/integration/scdf/spring-cloud-dataflow-shell-2.8.1.jar


app import --uri https://dataflow.spring.io/rabbitmq-docker-latest

###### 

stream create --name account-cdc --definition "jdbc --jdbc.supplier.query='select * from Accounts;' --spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver --spring.datasource.username=sa --spring.datasource.password='Password!' --spring.datasource.url='jdbc:sqlserver://sqlservercdc:1433s;databaseName=master' --trigger.cron='0/5 * * * * * *'   --spring.rabbitmq.port=5672 | log --spring.rabbitmq.port=5672"

stream deploy --name account-cdc --properties "deployer.jdbc.kubernetes.requests.memory=1Gi, deployer.log.kubernetes.requests.memory=1Gi, deployer.jdbc.kubernetes.limits.memory=1Gi, deployer.log.kubernetes.limits.memory=1Gi"

fraud-log=:fraud-detection.fraud-detection > log


## CDC test

kubectl exec -it postgres-0 -- psql -c "insert into Accounts ( acct_id,  acct_nm) values('hello','world');"