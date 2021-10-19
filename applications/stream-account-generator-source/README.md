```shell
mvn -pl applications/stream-account-generator-source -am spring-boot:build-image
```

```shell
docker tag stream-account-generator-source:0.0.1-SNAPSHOT cloudnativedata/stream-account-generator-source:0.0.1-SNAPSHOT
docker push cloudnativedata/stream-account-generator-source:0.0.1-SNAPSHOT
```

