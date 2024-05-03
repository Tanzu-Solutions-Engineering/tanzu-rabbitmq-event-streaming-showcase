package showcase.event.stream.rabbitmq.account.http.source;

import com.rabbitmq.stream.Environment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.rabbit.stream.support.StreamAdmin;

@Configuration
public class StreamConfig {
    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String streamName;

    @Bean
    StreamAdmin streamAdmin(Environment env) {
        return new StreamAdmin(env, sc -> {
            sc.stream(streamName).create();
        });
    }

    @Bean
    RabbitStreamTemplate rabbitStreamTemplate(Environment environment, MessageConverter messageConverter) {
        var template = new RabbitStreamTemplate(environment,streamName);

        template.setMessageConverter(messageConverter);

        return template;
    }

    @Bean
    MessageConverter messageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }
}
