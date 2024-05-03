
Run application

```shell
java -jar applications/event-account-http-source/target/event-account-http-source-0.0.1-SNAPSHOT.jar
```

open http://localhost:8095

## Docker building image

```shell
mvn install
cd applications/event-account-http-source
mvn package

docker build  --platform linux/amd64,linux/arm64 -t event-account-http-source:0.0.1-SNAPSHOT .

```

```shell
docker tag event-account-http-source:0.0.1-SNAPSHOT cloudnativedata/event-account-http-source:0.0.1-SNAPSHOT
docker push cloudnativedata/event-account-http-source:0.0.1-SNAPSHOT
```
