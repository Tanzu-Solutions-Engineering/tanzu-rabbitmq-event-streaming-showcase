/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */

package showcase.event.stream.rabbitmq.accoun.sink;

import com.rabbitmq.stream.OffsetSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

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

    @Bean
    ConnectionNameStrategy connectionNameStrategy() {
        return (connectionFactory) -> applicationName;
    }

    @Bean
    MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
        log.info("Offset: {}",offset);
        return (cont, dest, group) -> {
            if (cont instanceof StreamListenerContainer container) {
                container.setConsumerCustomizer((name, builder) -> {
                    switch (offset)
                    {
                        case "last" -> {
                            builder.name(applicationName);
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