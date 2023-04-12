

```shell
java -jar applications/http-amqp-source/target/http-amqp-source-0.0.4-SNAPSHOT.jar
```

## Docker building image

```shell
mvn install
cd applications/http-amqp-source
mvn spring-boot:build-image
```

```shell
docker tag http-amqp-source:0.0.4-SNAPSHOT cloudnativedata/http-amqp-source:0.0.4-SNAPSHOT
docker push cloudnativedata/http-amqp-source:0.0.4-SNAPSHOT
```

