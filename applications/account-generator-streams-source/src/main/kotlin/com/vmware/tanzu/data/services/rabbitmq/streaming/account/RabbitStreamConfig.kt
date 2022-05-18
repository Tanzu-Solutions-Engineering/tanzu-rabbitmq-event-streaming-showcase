package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.rabbitmq.stream.*
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory
import org.springframework.rabbit.stream.listener.ConsumerCustomizer
import org.springframework.rabbit.stream.listener.StreamListenerContainer
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate


/**
 *
 */
@Configuration
class RabbitStreamConfig() {

    @Value("\${spring.rabbitmq.username:guest}")
    private var username: String = "guest";

    @Value("\${account.generator.stream.name:event-streaming-showcase.accounts}")
    private val streamName: String = "event-streaming-showcase.accounts"

    @Value("\${spring.rabbitmq.password:guest}")
    private var password:  String = "guest";

    @Value("\${spring.rabbitmq.host:localhost}")
    private var hostname: String = "localhost";

    @Value("\${spring.application.name}")
    private var applicationName: String? = null


    @Bean
    fun rabbitStreamEnvironment(): Environment {

        val env =  Environment.builder()
            .host(hostname)
            .username(username)
            .password(password).build()


//       try{
//           env.streamCreator().stream(streamName).create()
//       }
//       catch(e : StreamException)
//       {
//       }

        return env

    }

    @Bean
    fun streamTemplate(env: Environment): RabbitStreamTemplate {
        val template = RabbitStreamTemplate(env, streamName)
        template.setProducerCustomizer { name: String?, builder: ProducerBuilder ->
            builder.name(
                "test"
            )
        }

        template.setMessageConverter(messageConverter())

        return template
    }

//    @Bean
//    fun nativeFactory( env : Environment)  : RabbitListenerContainerFactory<StreamListenerContainer> {
//        var factory =  StreamRabbitListenerContainerFactory(env);
//        factory.setNativeListener(true);
//
//
////        env.streamCreator().stream(streamName).
//
//        val cc : ConsumerCustomizer = ConsumerCustomizer{ id: String, builder: ConsumerBuilder ->
//            builder.name("myConsumer")
//                .offset(OffsetSpecification.first())
//                .manualTrackingStrategy();
//        }
//        factory.setConsumerCustomizer(cc);
//        return factory;
//    }

    @Primary
    @Bean
    fun messageConverter() : MessageConverter
    {
        return Jackson2JsonMessageConverter();
    }

//    @RabbitListener(queues = ["test.stream.queue1"],containerFactory = "nativeFactory")
//    fun streamListener(input : String) {
//    }
//
//    @RabbitListener(id = "test", queues = ["test.stream.queue2"], containerFactory = "nativeFactory")
//    void nativeMsg(Message in, Context context) {
//        ...
//        context.storeOffset();
//    }

}