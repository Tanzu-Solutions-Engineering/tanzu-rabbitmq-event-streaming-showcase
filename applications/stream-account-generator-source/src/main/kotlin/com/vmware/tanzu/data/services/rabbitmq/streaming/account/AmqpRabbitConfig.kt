package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import nyla.solutions.core.data.collections.QueueSupplier
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean
import org.springframework.amqp.rabbit.connection.ThreadChannelConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 *
 */
@Configuration
//@EnableAsync
class AmqpRabbitConfig {

    @Value("\${spring.rabbitmq.username:guest}")
    private var username: String = "guest";

    @Value("\${spring.rabbitmq.password:guest}")
    private var password:  String = "guest";

    @Value("\${spring.rabbitmq.host:localhost}")
    private var hostname: String = "localhost";

    @Value("\${spring.application.name}")
    private var applicationName: String? = null

    @Bean
    fun connectionNameStrategy(): ConnectionNameStrategy? {
        return ConnectionNameStrategy { applicationName!! }
    }

    @Bean
    fun connectionFactory( ) : ConnectionFactory
    {
        var factory = RabbitConnectionFactoryBean();
        factory.setHost(hostname);
        factory.setUsername(username);
        factory.setPassword(password);

        return ThreadChannelConnectionFactory(factory.rabbitConnectionFactory);
    }

    @Bean
    fun convert() : MessageConverter
    {
        return Jackson2JsonMessageConverter();
    }

    @Bean
    fun rabbitListenerContainerFactory() :SimpleRabbitListenerContainerFactory {
        var factory = SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(Jackson2JsonMessageConverter());
        return factory;
    }
}