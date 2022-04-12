```shell
mvn install
```

```shell
cd applications/stream-account-generator-source
```

```shell
mvn spring-boot:build-image
```

```shell
docker tag stream-account-generator-source:0.0.1-SNAPSHOT cloudnativedata/stream-account-generator-source:0.0.1-SNAPSHOT
docker push cloudnativedata/stream-account-generator-source:0.0.1-SNAPSHOT
```




docker tag stream-account-generator-source:0.0.1-SNAPSHOT $PRIVATE_CONTAINER_REPO/lob-1/stream-account-generator-source:0.0.1-SNAPSHOT
docker push $PRIVATE_CONTAINER_REPO/lob-1/stream-account-generator-source:0.0.1-SNAPSHOT
