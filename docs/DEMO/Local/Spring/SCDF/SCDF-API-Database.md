

Download the GemFire Sink Rabbit artifact 




```shell
cd /Users/devtools/integration/scdf
```

```shell
./startSkipper.sh
./startSCDF.sh 
```

```shell
open http://localhost:9393/dashboard/index.html
```


jdbc:postgresql://localhost:5432/postgres



```shell
http-postgres=http --port=9991 | jdbc  --tableName=accounts --columns=id,name --spring.application.name=jdbc-sink-postgres  --spring.datasource.url='jdbc:postgresql://localhost:5432/postgres' --spring.datasource.username=postgres
```

Record 1

```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"1\",\"name\": \"ABC\"}"
```

```shell
curl http://localhost:9991 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"2\",\"name\": \"123\"}"
```



```sqlite-sql
select * from accounts;
```


# GemFire 

Execute from online script to start GemFire
```shell
curl https://raw.githubusercontent.com/ggreen/gemfire-extensions/main/deployments/scripts/monitoring/start.sh | bash
```

```shell
http-postgres=http --port=8881 | jdbc --tableName=accounts --columns=id,name --spring.application.name=jdbc-sink-postgres --spring.datasource.url='jdbc:postgresql://localhost:5432/postgres' --spring.datasource.username=postgres
http-gemfire=:http-postgres.http > gemfire-sink --gemfire.region.regionName=Accounts --gemfire.consumer.keyExpression='payload.getField(''id'')' --spring.data.gemfire.pool.default.locators='localhost[10334]' --gemfire.consumer.json=true
```


```shell
curl http://localhost:8881 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"guru\",\"name\": \"Gideon Low\"}"
```

```shell
curl http://localhost:8881 -H "Accept: application/json" --header "Content-Type: application/json"  -X POST -d "{\"id\": \"acme\",\"name\": \"Acme Fitnes\"}"
```

Clear data

```shell
remove --region=/Accounts --key=1
remove --region=/Accounts --key=2
remove --region=/Accounts --key=guru
remove --region=/Accounts --key=acme
```