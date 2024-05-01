package com.vmware.account.jdbc.sink;

import com.rabbitmq.stream.OffsetSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.connection.ThreadChannelConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

@Configuration
@Slf4j
public class RabbitConfig {


    @Value("${spring.rabbitmq.username:guest}")
    private String username = "guest";

    @Value("${spring.rabbitmq.password:guest}")
    private String password = "guest";

    @Value("${spring.rabbitmq.host:localhost}")
    private String hostname = "localhost";

    @Value("${spring.application.name}")
    private String applicationName = null;


    @Bean
    ConnectionFactory connectionFactory() {
        var factory = new RabbitConnectionFactoryBean();
        factory.setHost(hostname);
        factory.setUsername(username);
        factory.setPassword(password);

        var tcf = new ThreadChannelConnectionFactory(factory.getRabbitConnectionFactory());
        tcf.setConnectionNameStrategy( (args) -> applicationName );
        return tcf;
    }

    @Bean
    MessageConverter convert() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
        return (cont, dest, group) -> {
            if(cont instanceof StreamListenerContainer container)
                container.setConsumerCustomizer((name, builder) -> {
                    builder.subscriptionListener(context -> {
                        log.info("Replaying from the first record in the stream");
                        context.offsetSpecification(OffsetSpecification.first());
                    });
            });
        };
    }
}
