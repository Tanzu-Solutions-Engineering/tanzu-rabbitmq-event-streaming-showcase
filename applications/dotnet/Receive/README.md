# Receive

Example Dot NET console to receive messages from [RabbitMQ](https://www.rabbitmq.com/)



# Build Docker image

```shell
cd applications/dotnet/Receive

#docker build  --platform linux/amd64,linux/arm64 -t event-receive:0.0.1 .
docker build -t event-receive:0.0.1 .
docker tag event-receive:0.0.1 cloudnativedata/event-receive:0.0.1
docker push cloudnativedata/event-receive:0.0.1
```
