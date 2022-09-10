## Docker building image

```shell
mvn install
cd applications/account-http-ampq-source
mvn spring-boot:build-image
```

```shell
docker tag account-http-ampq-source:0.0.1-SNAPSHOT cloudnativedata/account-http-ampq-source:0.0.1-SNAPSHOT
docker push cloudnativedata/account-http-ampq-source:0.0.1-SNAPSHOT
```

