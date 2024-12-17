

# Register Application


```shell
rm /tmp/freemarker-processor*
cp applications/processors/freemarker-processor/target/freemarker-processor-0.0.1-SNAPSHOT.jar /tmp
cp applications/processors/freemarker-processor/target/freemarker-processor-0.0.1-SNAPSHOT-metadata.jar /tmp
ls /tmp/freemarker-processor*
```

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar
```

```shell
app register --bootVersion 3 --type processor --name freemarker-processor  --metadata-uri "file:///tmp/freemarker-processor-0.0.1-SNAPSHOT-metadata.jar" --uri "file:///tmp/freemarker-processor-0.0.1-SNAPSHOT.jar"
```


Create Stream with DSL

    Click Streams -> Create Streams(s)


Using properties


```shell
echo "freemarker.template="\${firstName}",\"\${lastName}"" > /tmp/json-to-csv.properties

cat  /tmp/json-to-csv.properties
```

Use Stream definition

```shell
json-to-csv=http --port=9004 | freemarker-processor --content-type=text/csv | log
```



Deploy Stream :wq



```properties
app.freemarker-processor.spring.config.location=classpath:/application.yml,/tmp/json-to-csv.properties
app.freemarker-processor.freemarker.content-type=text/csv
app.http.server.port=9004
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
app.log.spring.cloud.stream.rabbit.binder.connection-name-prefix=log
deployer.freemarker-processor.bootVersion=3
deployer.http.bootVersion=2
deployer.log.bootVersion=2
```

Testing

```shell
curl -X POST http://localhost:9004  \
   -H 'Content-Type: application/json' \
    -d '{ "firstName" : "Josiah", "lastName" : "Imani" }'
```



```shell
json-to-csv-file=http --port=9005 | freemarker-processor --content-type=text/csv | file --directory=/tmp --name=csv-out.txt
```


```properties
app.freemarker-processor.spring.config.location=classpath:/application.yml,/tmp/json-to-csv.properties
app.freemarker-processor.freemarker.content-type=text/csv
app.file.directory=/tmp
app.file.name=csv-out.txt
deployer.file.bootVersion=2
app.http.server.port=9005
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
app.log.spring.cloud.stream.rabbit.binder.connection-name-prefix=log
deployer.freemarker-processor.bootVersion=3
deployer.http.bootVersion=2
deployer.log.bootVersion=2
```

Testing

```shell
curl -X POST http://localhost:9005  \
   -H 'Content-Type: application/json' \
    -d '{ "firstName" : "Josiah", "lastName" : "Imani" }'
```

