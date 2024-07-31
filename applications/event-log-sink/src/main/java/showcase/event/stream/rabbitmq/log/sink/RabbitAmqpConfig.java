package showcase.event.stream.rabbitmq.log.sink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import showcase.event.stream.rabbitmq.log.sink.functions.LogConsumer;

@Configuration
@Slf4j
@Profile("ampq")
public class RabbitAmqpConfig {


    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host:localhost}")
    private String hostname;

    @Value("${spring.rabbitmq.queue:event-log-sink}")
    private String queueName;

    @Value("${spring.rabbitmq.routing.key:#}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange:showcase.event.streaming.accounts}")
    private String exchange;

    @Bean
    Exchange exchange()
    {

        return ExchangeBuilder.topicExchange(exchange).build();
    }

    @Bean
    Queue queue()
    {
        return QueueBuilder.durable(queueName).quorum().build();
    }

    @Bean
    Binding binding(Queue queue,Exchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(
            ConnectionNameStrategy connectionNameStrategy,
            MessageListener exampleListener) {
        var container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory(connectionNameStrategy));
        container.addQueueNames(queueName);
        container.setMessageListener(exampleListener);
        return container;
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory(
            ConnectionNameStrategy connectionNameStrategy) {
        var connectionFactory =
                new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setConnectionNameStrategy(connectionNameStrategy);
        return connectionFactory;
    }

    @Bean
    @Primary
    public MessageListener messageListener(LogConsumer logConsumer) {
        return new MessageListener() {
            @Override
            public void onMessage(Message message) {
                logConsumer.accept(message.getBody());
            }
        };
    }
}
