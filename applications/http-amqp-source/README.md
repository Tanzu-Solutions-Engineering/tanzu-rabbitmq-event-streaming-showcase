

```shell
java -jar applications/http-amqp-source/target/http-amqp-source-0.0.7-SNAPSHOT.jar
```

## Docker building image

```shell
mvn install
cd applications/http-amqp-source
mvn package
docker build  --platform linux/amd64,linux/arm64 -t http-amqp-source:0.0.7-SNAPSHOT .
```

```shell
docker tag http-amqp-source:0.0.7-SNAPSHOT cloudnativedata/http-amqp-source:0.0.7-SNAPSHOT
docker push cloudnativedata/http-amqp-source:0.0.7-SNAPSHOT
```

