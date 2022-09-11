## Docker building image

```shell
mvn install
cd applications/account-jdbc-amqp-sink
mvn spring-boot:build-image
```

```shell
docker tag account-jdbc-amqp-sink:0.0.1-SNAPSHOT cloudnativedata/account-jdbc-amqp-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/account-jdbc-amqp-sink:0.0.1-SNAPSHOT
```

