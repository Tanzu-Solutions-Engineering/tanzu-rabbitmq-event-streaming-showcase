## Docker building image

```shell
mvn -pl applications/stream-account-geode-sink -am spring-boot:build-image
```

```shell
docker tag stream-account-geode-sink:0.0.1-SNAPSHOT cloudnativedata/stream-account-geode-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/stream-account-geode-sink:0.0.1-SNAPSHOT
```

