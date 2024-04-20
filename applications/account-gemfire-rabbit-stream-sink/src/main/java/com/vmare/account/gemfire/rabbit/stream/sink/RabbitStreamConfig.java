package com.vmare.account.gemfire.rabbit.stream.sink;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

@Configuration
@Slf4j
public class RabbitStreamConfig {


    @Value("${account.sink.stream.name:event-streaming-showcase.accounts}")
    private String streamName  ="event-streaming-showcase.accounts";

    @Value("${spring.rabbitmq.username:guest}")
    private String username  = "guest";

    @Value("${spring.rabbitmq.password:guest}")
    private String password  = "guest";

    @Value("${spring.rabbitmq.host:localhost}")
    private String hostname  = "localhost";

    @Value("${spring.application.name}")
    private String applicationName = null;


    @Bean
    Environment rabbitStreamEnvironment() {

        var env = Environment.builder()
                .host(hostname)
                .username(username)
                .password(password).build();

        env.streamCreator().stream(streamName).create();

        return env;

    }

    @ConditionalOnProperty(name = {"rabbitmq.streaming.replay"},havingValue = "true")
    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
        return (cont, dest, group) -> {
            StreamListenerContainer container = (StreamListenerContainer) cont;
            container.setConsumerCustomizer((name, builder) -> {
                builder.subscriptionListener(context -> {
                    log.info("Replaying from the first record in the stream");
                    context.offsetSpecification(OffsetSpecification.first());
                });
            });
        };
    }
}
