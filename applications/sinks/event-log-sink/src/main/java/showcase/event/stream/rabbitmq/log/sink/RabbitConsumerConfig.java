package showcase.event.stream.rabbitmq.log.sink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
@Slf4j
public class RabbitConsumerConfig {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }


    @Bean
    ConnectionNameStrategy connectionNameStrategy() {
        return (connectionFactory) -> applicationName;
    }

}
