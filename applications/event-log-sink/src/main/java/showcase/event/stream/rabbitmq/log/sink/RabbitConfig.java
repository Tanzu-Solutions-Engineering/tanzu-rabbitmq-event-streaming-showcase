/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */

package showcase.event.stream.rabbitmq.log.sink;

import com.rabbitmq.stream.OffsetSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class RabbitConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.rabbitmq.username:guest}")
    private String username = "guest";

    @Value("${spring.rabbitmq.password:guest}")
    private String password = "guest";

    @Value("${spring.rabbitmq.host:127.0.0.1}")
    private String hostname = "localhost";

    @Value("${rabbitmq.streaming.offset:last}")
    private String offset;

    @Value("${rabbitmq.streaming.singleActiveConsumer:true}")
    private boolean singleActiveConsumer;

    @Value("${spring.cloud.stream.bindings.input.group:event-log-sink}")
    private String streamName;

    @Value("${spring.cloud.stream.bindings.input.destination:input}")
    private String destination;

    @ConditionalOnProperty(name = "spring.cloud.stream.rabbit.bindings.input.consumer.queueNameGroupOnly", havingValue = "true")
    @Bean
    Queue streamGroupOnly() {
        log.info("Creating stream: {}",streamName);
        return QueueBuilder.durable(streamName)
                .stream()
                .build();
    }
    @ConditionalOnProperty(name = "spring.cloud.stream.rabbit.bindings.input.consumer.queueNameGroupOnly", havingValue = "false")
    @Bean
    Queue stream() {

        var stream  = destination+"."+streamName;
        log.info("Creating stream: {}",stream);
        return QueueBuilder.durable(stream)
                .stream()
                .build();
    }


    @Bean
    ConnectionNameStrategy connectionNameStrategy() {
        return (connectionFactory) -> applicationName;
    }

    @Bean
    MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    Consumer<byte[]> logConsumer()
    {
        return bytes ->{
          log.info("CONSUMED: {} ",new String(bytes));
        };
    }

    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
        log.info("applicationName: {}, Offset: {}, singleActiveConsumer: {}",applicationName,offset,singleActiveConsumer);
        return (cont, dest, group) -> {
            if (cont instanceof StreamListenerContainer container) {
                container.setConsumerCustomizer((name, builder) -> {
                    switch (offset)
                    {
                        case "last" -> {
                            builder.name(applicationName);
                            if(singleActiveConsumer)
                                builder.singleActiveConsumer();

                            builder.offset(OffsetSpecification.last());}

                        case "next" -> builder.offset(OffsetSpecification.next());
                    }
                    builder.subscriptionListener(context -> {
                        switch (offset) {
                            case "first" -> {
                                log.info("Replaying from the first record in the stream");
                                context.offsetSpecification(OffsetSpecification.first());
                            }
                        }
                    });
                });
            }
        };
    }
}