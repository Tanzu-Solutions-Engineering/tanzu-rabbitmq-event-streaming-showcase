

# Register Application


```shell
cp applications/processors/json-xml-processor/target/json-xml-processor-0.0.1-SNAPSHOT.jar /tmp
cp applications/processors/json-xml-processor/target/json-xml-processor-0.0.1-SNAPSHOT-metadata.jar /tmp
 ls /tmp/json-xml-processor*
```

```shell
java -jar runtime/scdf/spring-cloud-dataflow-shell-2.11.5.jar
```

```shell
app register --bootVersion 3 --type processor --name json-xml-processor  --metadata-uri "file:///tmp/json-xml-processor-0.0.1-SNAPSHOT-metadata.jar" --uri "file:///tmp/json-xml-processor-0.0.1-SNAPSHOT.jar"
```


Create Stream with DSL

    Click Streams -> Create Streams(s)


Use Stream definition

```shell
json-to-xml=http --port=9003 | json-xml-processor --root-name=users | log
```

Deploy Stream 



Using properties

```properties
app.http.server.port=9003
app.json-xml-processor.json.to.xml.root-name=users
app.http.spring.cloud.stream.rabbit.binder.connection-name-prefix=http
app.log.spring.cloud.stream.rabbit.binder.connection-name-prefix=log
deployer.json-xml-processor.bootVersion=3
deployer.http.bootVersion=2
deployer.log.bootVersion=2
```

Test with CSV

```shell
curl -X POST http://localhost:9003  \
   -H 'Content-Type: application/json' \
    -d '{ "firstName" : "Josiah", "lastName" : "Imani" }'
```
