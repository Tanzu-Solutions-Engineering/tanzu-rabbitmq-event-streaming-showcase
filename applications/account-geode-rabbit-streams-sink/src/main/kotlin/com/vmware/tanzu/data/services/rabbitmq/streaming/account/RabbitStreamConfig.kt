package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.rabbitmq.stream.ConsumerBuilder
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.OffsetSpecification
import nyla.solutions.core.util.Text
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.stream.config.ListenerContainerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.rabbit.stream.listener.ConsumerCustomizer
import org.springframework.rabbit.stream.listener.StreamListenerContainer


/**
 *
 */
@Configuration
class RabbitStreamConfig {

    @Value("\${spring.rabbitmq.username:guest}")
    private var username: String = "guest";

    @Value("\${spring.rabbitmq.password:guest}")
    private var password:  String = "guest";

    @Value("\${spring.rabbitmq.host:localhost}")
    private var hostname: String = "localhost";

    @Value("\${spring.application.name}")
    private var applicationName: String? = null


    @Bean
    fun rabbitStreamEnvironment(): Environment {

        var env = Environment.builder()
            .host(hostname)
            .username(username)
            .password(password).build()

        env.streamCreator().stream("event-streaming-showcase.stream-account-geode-sink-stream").create()

        return env

    }

    @Bean
    @ConditionalOnProperty(name = ["rabbitmq.streaming.replay"],havingValue = "true")
    fun customizer(): ListenerContainerCustomizer<MessageListenerContainer>? {
        return ListenerContainerCustomizer { cont: MessageListenerContainer, dest: String?, group: String? ->
            val container =
                cont as StreamListenerContainer
            container.setConsumerCustomizer { name: String?, builder: ConsumerBuilder ->

                builder.name(Text.generateId())
                builder.offset(
                    OffsetSpecification.first()
                ).autoTrackingStrategy()
            }
        }
    }

}