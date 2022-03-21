package com.vmware.financial.open.banking.account

import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.TopicExchange
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
    fun exchange(@Value("\${spring.cloud.stream.bindings.supplier-out-0.destination:banking-account}")
                 exchangeName: String) : Exchange
    {
        return TopicExchange(exchangeName)
    }

    @Bean
    fun connectionFactory( ) : ConnectionFactory
    {
        var factory = RabbitConnectionFactoryBean();
        factory.setHost(hostname);
        factory.setUsername(username);
        factory.setPassword(password);

        var tcf =  ThreadChannelConnectionFactory(factory.rabbitConnectionFactory);
        tcf.setConnectionNameStrategy(ConnectionNameStrategy { applicationName!! })

        return tcf
    }

    /**
     * Override the Java Serializer
     */
    @Bean
    fun convert() : MessageConverter {
        return Jackson2JsonMessageConverter();
    }
}