
## Docker building image

```shell
mvn install
cd applications/event-account-jdbc-sink
mvn clean package

docker build  --platform linux/amd64,linux/arm64 -t event-account-jdbc-sink:0.0.1-SNAPSHOT .

```

```shell
docker tag event-account-jdbc-sink:0.0.1-SNAPSHOT cloudnativedata/event-account-jdbc-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/event-account-jdbc-sink:0.0.1-SNAPSHOT
```
