# SCDF Applications

You can pick and choose the needed application from the bulk HTTP link. 
You can see the files here for releases. See it under HTTP Maven links for the apps and metadata. 
You can download directly the app you want using a tool like wget or curl. 
Then run it by doing java -jar...

## Database Sink

Get App

```shell
wget https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jdbc-sink-rabbit/3.2.1/jdbc-sink-rabbit-3.2.1.jar
```

Run App


```shell
echo "CREATE TABLE IF NOT EXISTS accounts (id    varchar(40) PRIMARY KEY, name   varchar(40) NOT NULL CHECK (name <> ''));" > create-schema.sql 
```

```shell
java -jar jdbc-sink-rabbit-3.2.1.jar --jdbc.consumer.tableName=accounts \
--jdbc.consumer.columns=id,name \
--spring.datasource.username=postgres \
--spring.cloud.stream.bindings.input.destination=accounts \
--spring.cloud.stream.bindings.input.group=jdbc-sink \
--spring.cloud.stream.rabbit.bindings.input.consumer.quorum.enabled=true \
--spring.datasource.url=jdbc:postgresql://localhost:5432/postgres \
--spring.datasource.data="file:$PWD/create-schema.sql" \
--spring.datasource.initialization-mode=ALWAYS
```

-- \




Get Http Source
```shell
wget https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/http-source-rabbit/3.2.1/http-source-rabbit-3.2.1.jar
```

Post to HTTP

```shell
java -jar http-source-rabbit-3.2.1.jar --server.port=8111 --spring.rabbitmq.addresses=localhost:5672 \
--spring.rabbitmq.username=$RABBIT_USERNAME \
--spring.rabbitmq.password=$RABBIT_PASSWORD \
--spring.rabbitmq.template.exchange=http-source \
--spring.cloud.stream.bindings.output.destination=accounts \
--spring.cloud.stream.binder.output.connection-name-prefix=http 
```


Send Data

```shell
curl --header "Content-Type: application/json" --request POST \
  --data '{"id":"hello1","name":"world1"}'  http://localhost:8111
```


See https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/stream-applications-descriptor/2021.1.2/stream-applications-descriptor-2021.1.2.rabbit-apps-maven-repo-url.properties


```properties
source.cdc-debezium=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/cdc-debezium-source-rabbit/3.2.1/cdc-debezium-source-rabbit-3.2.1.jar
source.cdc-debezium.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/cdc-debezium-source-rabbit/3.2.1/cdc-debezium-source-rabbit-3.2.1-metadata.jar
source.file=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/file-source-rabbit/3.2.1/file-source-rabbit-3.2.1.jar
source.file.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/file-source-rabbit/3.2.1/file-source-rabbit-3.2.1-metadata.jar
source.ftp=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/ftp-source-rabbit/3.2.1/ftp-source-rabbit-3.2.1.jar
source.ftp.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/ftp-source-rabbit/3.2.1/ftp-source-rabbit-3.2.1-metadata.jar
source.geode=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/geode-source-rabbit/3.2.1/geode-source-rabbit-3.2.1.jar
source.geode.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/geode-source-rabbit/3.2.1/geode-source-rabbit-3.2.1-metadata.jar
source.http=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/http-source-rabbit/3.2.1/http-source-rabbit-3.2.1.jar
source.http.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/http-source-rabbit/3.2.1/http-source-rabbit-3.2.1-metadata.jar
source.jdbc=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jdbc-source-rabbit/3.2.1/jdbc-source-rabbit-3.2.1.jar
source.jdbc.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jdbc-source-rabbit/3.2.1/jdbc-source-rabbit-3.2.1-metadata.jar
source.jms=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jms-source-rabbit/3.2.1/jms-source-rabbit-3.2.1.jar
source.jms.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jms-source-rabbit/3.2.1/jms-source-rabbit-3.2.1-metadata.jar
source.load-generator=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/load-generator-source-rabbit/3.2.1/load-generator-source-rabbit-3.2.1.jar
source.load-generator.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/load-generator-source-rabbit/3.2.1/load-generator-source-rabbit-3.2.1-metadata.jar
source.mail=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mail-source-rabbit/3.2.1/mail-source-rabbit-3.2.1.jar
source.mail.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mail-source-rabbit/3.2.1/mail-source-rabbit-3.2.1-metadata.jar
source.mongodb=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mongodb-source-rabbit/3.2.1/mongodb-source-rabbit-3.2.1.jar
source.mongodb.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mongodb-source-rabbit/3.2.1/mongodb-source-rabbit-3.2.1-metadata.jar
source.mqtt=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mqtt-source-rabbit/3.2.1/mqtt-source-rabbit-3.2.1.jar
source.mqtt.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mqtt-source-rabbit/3.2.1/mqtt-source-rabbit-3.2.1-metadata.jar
source.rabbit=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/rabbit-source-rabbit/3.2.1/rabbit-source-rabbit-3.2.1.jar
source.rabbit.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/rabbit-source-rabbit/3.2.1/rabbit-source-rabbit-3.2.1-metadata.jar
source.s3=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/s3-source-rabbit/3.2.1/s3-source-rabbit-3.2.1.jar
source.s3.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/s3-source-rabbit/3.2.1/s3-source-rabbit-3.2.1-metadata.jar
source.sftp=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/sftp-source-rabbit/3.2.1/sftp-source-rabbit-3.2.1.jar
source.sftp.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/sftp-source-rabbit/3.2.1/sftp-source-rabbit-3.2.1-metadata.jar
source.syslog=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/syslog-source-rabbit/3.2.1/syslog-source-rabbit-3.2.1.jar
source.syslog.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/syslog-source-rabbit/3.2.1/syslog-source-rabbit-3.2.1-metadata.jar
source.tcp=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/tcp-source-rabbit/3.2.1/tcp-source-rabbit-3.2.1.jar
source.tcp.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/tcp-source-rabbit/3.2.1/tcp-source-rabbit-3.2.1-metadata.jar
source.time=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/time-source-rabbit/3.2.1/time-source-rabbit-3.2.1.jar
source.time.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/time-source-rabbit/3.2.1/time-source-rabbit-3.2.1-metadata.jar
source.twitter-message=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-message-source-rabbit/3.2.1/twitter-message-source-rabbit-3.2.1.jar
source.twitter-message.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-message-source-rabbit/3.2.1/twitter-message-source-rabbit-3.2.1-metadata.jar
source.twitter-search=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-search-source-rabbit/3.2.1/twitter-search-source-rabbit-3.2.1.jar
source.twitter-search.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-search-source-rabbit/3.2.1/twitter-search-source-rabbit-3.2.1-metadata.jar
source.twitter-stream=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-stream-source-rabbit/3.2.1/twitter-stream-source-rabbit-3.2.1.jar
source.twitter-stream.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-stream-source-rabbit/3.2.1/twitter-stream-source-rabbit-3.2.1-metadata.jar
source.websocket=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/websocket-source-rabbit/3.2.1/websocket-source-rabbit-3.2.1.jar
source.websocket.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/websocket-source-rabbit/3.2.1/websocket-source-rabbit-3.2.1-metadata.jar
source.zeromq=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/zeromq-source-rabbit/3.2.1/zeromq-source-rabbit-3.2.1.jar
source.zeromq.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/zeromq-source-rabbit/3.2.1/zeromq-source-rabbit-3.2.1-metadata.jar
sink.cassandra=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/cassandra-sink-rabbit/3.2.1/cassandra-sink-rabbit-3.2.1.jar
sink.cassandra.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/cassandra-sink-rabbit/3.2.1/cassandra-sink-rabbit-3.2.1-metadata.jar
sink.analytics=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/analytics-sink-rabbit/3.2.1/analytics-sink-rabbit-3.2.1.jar
sink.analytics.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/analytics-sink-rabbit/3.2.1/analytics-sink-rabbit-3.2.1-metadata.jar
sink.elasticsearch=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/elasticsearch-sink-rabbit/3.2.1/elasticsearch-sink-rabbit-3.2.1.jar
sink.elasticsearch.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/elasticsearch-sink-rabbit/3.2.1/elasticsearch-sink-rabbit-3.2.1-metadata.jar
sink.wavefront=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/wavefront-sink-rabbit/3.2.1/wavefront-sink-rabbit-3.2.1.jar
sink.wavefront.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/wavefront-sink-rabbit/3.2.1/wavefront-sink-rabbit-3.2.1-metadata.jar
sink.file=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/file-sink-rabbit/3.2.1/file-sink-rabbit-3.2.1.jar
sink.file.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/file-sink-rabbit/3.2.1/file-sink-rabbit-3.2.1-metadata.jar
sink.ftp=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/ftp-sink-rabbit/3.2.1/ftp-sink-rabbit-3.2.1.jar
sink.ftp.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/ftp-sink-rabbit/3.2.1/ftp-sink-rabbit-3.2.1-metadata.jar
sink.geode=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/geode-sink-rabbit/3.2.1/geode-sink-rabbit-3.2.1.jar
sink.geode.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/geode-sink-rabbit/3.2.1/geode-sink-rabbit-3.2.1-metadata.jar
sink.jdbc=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jdbc-sink-rabbit/3.2.1/jdbc-sink-rabbit-3.2.1.jar
sink.jdbc.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/jdbc-sink-rabbit/3.2.1/jdbc-sink-rabbit-3.2.1-metadata.jar
sink.log=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/log-sink-rabbit/3.2.1/log-sink-rabbit-3.2.1.jar
sink.log.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/log-sink-rabbit/3.2.1/log-sink-rabbit-3.2.1-metadata.jar
sink.mongodb=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mongodb-sink-rabbit/3.2.1/mongodb-sink-rabbit-3.2.1.jar
sink.mongodb.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mongodb-sink-rabbit/3.2.1/mongodb-sink-rabbit-3.2.1-metadata.jar
sink.mqtt=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mqtt-sink-rabbit/3.2.1/mqtt-sink-rabbit-3.2.1.jar
sink.mqtt.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/mqtt-sink-rabbit/3.2.1/mqtt-sink-rabbit-3.2.1-metadata.jar
sink.pgcopy=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/pgcopy-sink-rabbit/3.2.1/pgcopy-sink-rabbit-3.2.1.jar
sink.pgcopy.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/pgcopy-sink-rabbit/3.2.1/pgcopy-sink-rabbit-3.2.1-metadata.jar
sink.rabbit=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/rabbit-sink-rabbit/3.2.1/rabbit-sink-rabbit-3.2.1.jar
sink.rabbit.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/rabbit-sink-rabbit/3.2.1/rabbit-sink-rabbit-3.2.1-metadata.jar
sink.redis=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/redis-sink-rabbit/3.2.1/redis-sink-rabbit-3.2.1.jar
sink.redis.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/redis-sink-rabbit/3.2.1/redis-sink-rabbit-3.2.1-metadata.jar
sink.router=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/router-sink-rabbit/3.2.1/router-sink-rabbit-3.2.1.jar
sink.router.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/router-sink-rabbit/3.2.1/router-sink-rabbit-3.2.1-metadata.jar
sink.rsocket=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/rsocket-sink-rabbit/3.2.1/rsocket-sink-rabbit-3.2.1.jar
sink.rsocket.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/rsocket-sink-rabbit/3.2.1/rsocket-sink-rabbit-3.2.1-metadata.jar
sink.s3=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/s3-sink-rabbit/3.2.1/s3-sink-rabbit-3.2.1.jar
sink.s3.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/s3-sink-rabbit/3.2.1/s3-sink-rabbit-3.2.1-metadata.jar
sink.sftp=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/sftp-sink-rabbit/3.2.1/sftp-sink-rabbit-3.2.1.jar
sink.sftp.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/sftp-sink-rabbit/3.2.1/sftp-sink-rabbit-3.2.1-metadata.jar
sink.tcp=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/tcp-sink-rabbit/3.2.1/tcp-sink-rabbit-3.2.1.jar
sink.tcp.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/tcp-sink-rabbit/3.2.1/tcp-sink-rabbit-3.2.1-metadata.jar
sink.throughput=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/throughput-sink-rabbit/3.2.1/throughput-sink-rabbit-3.2.1.jar
sink.throughput.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/throughput-sink-rabbit/3.2.1/throughput-sink-rabbit-3.2.1-metadata.jar
sink.twitter-message=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-message-sink-rabbit/3.2.1/twitter-message-sink-rabbit-3.2.1.jar
sink.twitter-message.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-message-sink-rabbit/3.2.1/twitter-message-sink-rabbit-3.2.1-metadata.jar
sink.twitter-update=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-update-sink-rabbit/3.2.1/twitter-update-sink-rabbit-3.2.1.jar
sink.twitter-update.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-update-sink-rabbit/3.2.1/twitter-update-sink-rabbit-3.2.1-metadata.jar
sink.websocket=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/websocket-sink-rabbit/3.2.1/websocket-sink-rabbit-3.2.1.jar
sink.websocket.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/websocket-sink-rabbit/3.2.1/websocket-sink-rabbit-3.2.1-metadata.jar
sink.zeromq=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/zeromq-sink-rabbit/3.2.1/zeromq-sink-rabbit-3.2.1.jar
sink.zeromq.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/zeromq-sink-rabbit/3.2.1/zeromq-sink-rabbit-3.2.1-metadata.jar
processor.aggregator=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/aggregator-processor-rabbit/3.2.1/aggregator-processor-rabbit-3.2.1.jar
processor.aggregator.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/aggregator-processor-rabbit/3.2.1/aggregator-processor-rabbit-3.2.1-metadata.jar
processor.bridge=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/bridge-processor-rabbit/3.2.1/bridge-processor-rabbit-3.2.1.jar
processor.bridge.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/bridge-processor-rabbit/3.2.1/bridge-processor-rabbit-3.2.1-metadata.jar
processor.filter=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/filter-processor-rabbit/3.2.1/filter-processor-rabbit-3.2.1.jar
processor.filter.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/filter-processor-rabbit/3.2.1/filter-processor-rabbit-3.2.1-metadata.jar
processor.groovy=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/groovy-processor-rabbit/3.2.1/groovy-processor-rabbit-3.2.1.jar
processor.groovy.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/groovy-processor-rabbit/3.2.1/groovy-processor-rabbit-3.2.1-metadata.jar
processor.header-enricher=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/header-enricher-processor-rabbit/3.2.1/header-enricher-processor-rabbit-3.2.1.jar
processor.header-enricher.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/header-enricher-processor-rabbit/3.2.1/header-enricher-processor-rabbit-3.2.1-metadata.jar
processor.http-request=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/http-request-processor-rabbit/3.2.1/http-request-processor-rabbit-3.2.1.jar
processor.http-request.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/http-request-processor-rabbit/3.2.1/http-request-processor-rabbit-3.2.1-metadata.jar
processor.image-recognition=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/image-recognition-processor-rabbit/3.2.1/image-recognition-processor-rabbit-3.2.1.jar
processor.image-recognition.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/image-recognition-processor-rabbit/3.2.1/image-recognition-processor-rabbit-3.2.1-metadata.jar
processor.object-detection=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/object-detection-processor-rabbit/3.2.1/object-detection-processor-rabbit-3.2.1.jar
processor.object-detection.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/object-detection-processor-rabbit/3.2.1/object-detection-processor-rabbit-3.2.1-metadata.jar
processor.semantic-segmentation=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/semantic-segmentation-processor-rabbit/3.2.1/semantic-segmentation-processor-rabbit-3.2.1.jar
processor.semantic-segmentation.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/semantic-segmentation-processor-rabbit/3.2.1/semantic-segmentation-processor-rabbit-3.2.1-metadata.jar
processor.script=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/script-processor-rabbit/3.2.1/script-processor-rabbit-3.2.1.jar
processor.script.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/script-processor-rabbit/3.2.1/script-processor-rabbit-3.2.1-metadata.jar
processor.splitter=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/splitter-processor-rabbit/3.2.1/splitter-processor-rabbit-3.2.1.jar
processor.splitter.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/splitter-processor-rabbit/3.2.1/splitter-processor-rabbit-3.2.1-metadata.jar
processor.transform=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/transform-processor-rabbit/3.2.1/transform-processor-rabbit-3.2.1.jar
processor.transform.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/transform-processor-rabbit/3.2.1/transform-processor-rabbit-3.2.1-metadata.jar
processor.twitter-trend=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-trend-processor-rabbit/3.2.1/twitter-trend-processor-rabbit-3.2.1.jar
processor.twitter-trend.metadata=https://repo.maven.apache.org/maven2/org/springframework/cloud/stream/app/twitter-trend-processor-rabbit/3.2.1/twitter-trend-processor-rabbit-3.2.1-metadata.jar
```