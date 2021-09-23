# Tanzu Rabbitmq Event Streaming Showcase


This project demonstrates using the [RabbitMQ](https://www.rabbitmq.com/) [Kubernetes Operator](https://www.rabbitmq.com/kubernetes/operator/operator-overview.html).
There are several [Spring Boot](https://spring.io/projects/spring-boot) applications written in Kotlin.

It demonstrates [quorum queues](https://www.rabbitmq.com/quorum-queues.html) and [streams](https://www.rabbitmq.com/stream.html) application source producers and sink consumers using
[Spring Cloud Streams](https://spring.io/projects/spring-cloud-stream).
These examples have been verified to run using [Spring Cloud DataFlow](https://spring.io/projects/spring-cloud-dataflow).




Application                                 |   Notes
----------------------------------------------------------------------- | ---------------------------------
[account-generator-source](applications/account-generator-source)       | [Spring Cloud Stream](https://github.com/spring-cloud/spring-cloud-stream) source generates account changes 
[account-geode-sink](applications/account-geode-sink)                   | [Spring Cloud Stream](https://github.com/spring-cloud/spring-cloud-stream) sink to save account into [Apache Geode](https://geode.apache.org/). It also supports stream replay.
[account-http-source](applications/account-http-source)                 | [Spring Cloud Stream](https://github.com/spring-cloud/spring-cloud-stream) source that produces account information based on recieved HTTP REST 



# Lab Outline

The followings are some step-by-step learning exercises.

- [Lab 1 - Setup RabbitMQ on K8](docs/workshops/sp1/01-SETUP.md)
- [Lab 2 - Create a RabbitMQ Cluster with HA](docs/workshops/sp1/02-Rabbit-Cluster-Setup.md)
- [Lab 3 - Spring Apps with Quorum Queues](docs/workshops/sp1/03-Spring-Quorum.md) 
- [Lab 4 - Spring Apps with Streams](docs/workshops/sp1/04-Spring-Stream.md)
- [Lab 5 - Spring Cloud DataFlow](docs/workshops/sp1/05-Spring-SCDF.md)
- [Lab 6 - Provision RabbitMQ Topology Operation](docs/workshops/sp1/06-Using-Topology-Operator.md)




## Local installation

Execute the following from the main directory.

See script

```shell
./cloud/k8/local/setup.sh
```


