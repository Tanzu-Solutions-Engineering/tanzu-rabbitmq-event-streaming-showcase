package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.rabbitmq.stream.ConsumerBuilder
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.OffsetSpecification
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.stream.config.ListenerContainerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory
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

        return Environment.builder()
            .host(hostname)
            .username(username)
            .password(password).build()

    }

    @Bean
    fun customizer(cc: ConsumerCustomizer): ListenerContainerCustomizer<StreamListenerContainer> {
        return ListenerContainerCustomizer<StreamListenerContainer> { container, dest, group ->
            container.setConsumerCustomizer(cc)
        }
    }

    @Bean
//    @Profile("stream")
    @ConditionalOnProperty(name = ["rabbitmq.streaming.replay"],havingValue = "true")
    fun consumerCustomizerReplay() : ConsumerCustomizer
    {
        return  ConsumerCustomizer{ id: String, builder: ConsumerBuilder ->
            {
                builder.name("myConsumer")
                    .stream("event-streaming-showcase.stream-account-geode-sink-stream")
                    .offset(OffsetSpecification.offset(0))
                    .autoTrackingStrategy()
            }
        }
    }

    @Bean
//    @Profile("stream")
    @ConditionalOnProperty(name = ["rabbitmq.streaming.replay"], havingValue = "false", matchIfMissing = true)
    fun consumerCustomizer() : ConsumerCustomizer
    {
        return  ConsumerCustomizer{ id: String, builder: ConsumerBuilder ->
            {
                builder.name("myConsumer")
                    .offset(OffsetSpecification.next())
                    .autoTrackingStrategy();
            }
        }
    }

    @Bean
    fun nativeFactory( env : Environment, cc : ConsumerCustomizer)  : RabbitListenerContainerFactory<StreamListenerContainer> {
        var factory =  StreamRabbitListenerContainerFactory(env);
        factory.setNativeListener(true);

        factory.setConsumerCustomizer(cc);
        return factory;
    }


}