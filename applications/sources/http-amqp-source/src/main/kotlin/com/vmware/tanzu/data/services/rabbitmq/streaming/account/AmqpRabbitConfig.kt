package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    private val logger: Logger = LoggerFactory.getLogger("AmqpRabbitConfig")

    @Value("\${spring.rabbitmq.vhost:/}")
    private val virtualHost: String = ""

    @Value("\${spring.rabbitmq.username:guest}")
    private var username: String = "guest";

    @Value("\${spring.rabbitmq.password:guest}")
    private var password:  String = "guest";

    @Value("\${spring.rabbitmq.host:localhost}")
    private var hostname: String = "localhost";

    @Value("\${spring.application.name}")
    private var applicationName: String? = null

    @Bean
    fun exchange(@Value("\${spring.cloud.stream.bindings.supplier-out-0.destination:http-amqp-source}")
                 exchangeName: String) : Exchange
    {
        return TopicExchange(exchangeName)
    }

    @Bean
    fun connectionFactory( ) : ConnectionFactory
    {
        logger.info("virtualHost: $virtualHost")

        var factory = RabbitConnectionFactoryBean();
        factory.setHost(hostname);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost)

        var tcf =  ThreadChannelConnectionFactory(factory.rabbitConnectionFactory);
        tcf.setConnectionNameStrategy(ConnectionNameStrategy { applicationName!! })

        var connection = factory.rabbitConnectionFactory.newConnection()
        connection.close()

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