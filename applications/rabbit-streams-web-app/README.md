# rabbit-streams-web-app

This application provides a REST API to load RabbitMQ stream/super streams messages 
into GemFire.


Select stream data

```shell
query --query="select body from /transaction"
```

## Docker building image

```shell
mvn -pl applications/rabbit-streams-web-app -am spring-boot:build-image
```

```shell
docker tag rabbit-streams-web-app:0.0.1-SNAPSHOT cloudnativedata/rabbit-streams-web-app:0.0.1-SNAPSHOT
docker push cloudnativedata/rabbit-streams-web-app:0.0.1-SNAPSHOT
```