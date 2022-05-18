## Docker building image

```shell
mvn -pl applications/stream-account-http-source -am spring-boot:build-image
```

```shell
docker tag stream-account-http-source:0.0.1-SNAPSHOT cloudnativedata/stream-account-http-source:0.0.1-SNAPSHOT
docker push cloudnativedata/stream-account-http-source:0.0.1-SNAPSHOT
```

