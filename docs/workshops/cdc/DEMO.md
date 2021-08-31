stream create --name account-cdc  --definition "jdbc --jdbc.supplier.query='select * from Accounts' --spring.datasource.password=CHANGEME --spring.datasource.username=postgres --spring.datasource.url=jdbc:postgresql://postgres/postgres  --spring.rabbitmq.port=5672 | account-geode-sink --spring.cloud.stream.bindings.accountConsumer-in-0.destination=account-cdc --spring.cloud.stream.bindings.accountConsumer-in-0.group=jdbc.account-cdc --server.port=8080 --spring.application.name=scf-account-sink --spring.rabbitmq.stream.host=rabbitmq --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334] --spring.rabbitmq.port=5672"

stream deploy --name account-cdc --properties "deployer.jdbc.kubernetes.requests.memory=1Gi, deployer.account-geode-sink.kubernetes.requests.memory=1Gi, deployer.jdbc.kubernetes.limits.memory=1Gi, deployer.account-geode-sink.kubernetes.limits.memory=1Gi"

stream create --name account-cdc-log --definition ":account-cdc > log"


## CDC test

kubectl exec -it postgres-0 -- psql -c "insert into Accounts ( id,  bank_id, label, number, product_code) values('my-id',  'my-bank_id', 'my-label', 'my-number', 'my-product_code');"
