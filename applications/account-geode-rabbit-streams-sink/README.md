## Docker building image

```shell
mvn -pl applications/stream-account-geode-sink -am spring-boot:build-image
```

```shell
docker tag stream-account-geode-sink:0.0.1-SNAPSHOT cloudnativedata/stream-account-geode-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/stream-account-geode-sink:0.0.1-SNAPSHOT
```

## Apache Geode/GemFire


In Gfsh


```shell
 cd /Users/devtools/repositories/IMDG/geode/apache-geode-1.13.7/bin/
```

```shell
start locator --name=locator
```

```shell
configure pdx --read-serialized=true --disk-store
```

```shell
start server --name=server --start-rest-api=true --http-service-port=7001 --initial-heap=2g --max-heap=2g
```


```shell
create region --name=Account --type=PARTITION
```
