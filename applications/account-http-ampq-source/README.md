## Docker building image

```shell
mvn -pl applications/account-http-ampq-source -am spring-boot:build-image
```

```shell
docker tag account-http-ampq-source:0.0.1-SNAPSHOT cloudnativedata/account-http-ampq-source:0.0.1-SNAPSHOT
docker push cloudnativedata/account-http-ampq-source:0.0.1-SNAPSHOT
```

