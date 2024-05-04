/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */

package showcase.event.stream.rabbitmq.log.sink;

import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.Environment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.rabbit.stream.config.SuperStream;

@Configuration
@Slf4j
@Profile("superStream")
public class RabbitSuperStreamConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${rabbitmq.streaming.offset:last}")
    private String offset;


    @Value("${spring.cloud.stream.bindings.input.group:event-log-sink}")
    private String superStreamName;

    @Value("${spring.cloud.stream.bindings.input.destination:input}")
    private String destination;

    @Value("${rabbitmq.streaming.partitions:2}")
    private int partitions;

    @Value("${rabbitmq.streaming.concurrency:1}")
    private int concurrency;

    @ConditionalOnProperty(name = "spring.cloud.stream.rabbit.bindings.output.consumer.queueNameGroupOnly", havingValue = "true")
    @Bean
    SuperStream superStream(Environment environment) {

        log.info("Creating super stream: {}", superStreamName);

        environment.streamCreator().name(superStreamName)
                .superStream()
                .partitions(partitions).creator()
                .create();

        return new SuperStream(superStreamName, partitions);
    }

    @ConditionalOnProperty(name = "spring.cloud.stream.rabbit.bindings.output.consumer.queueNameGroupOnly", havingValue = "false")
    @Bean
    SuperStream stream(Environment environment) {

        var stream  = destination+"."+ superStreamName;
        log.info("Creating stream: {}",stream);
        environment.streamCreator().name(superStreamName)
                .superStream()
                .partitions(partitions).
                creator()
                .create();

        return new SuperStream(superStreamName, partitions);
    }

    @Bean
    Consumer consumer(Environment environment,
                      java.util.function.Consumer<byte[]> consumerFunction){
        return environment.consumerBuilder()
                .superStream(superStreamName)
                .messageHandler((context, message) -> {
                    consumerFunction.accept(message.getBodyAsBinary());
                })
                .build();
    }


//    @Bean
//    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
//        log.info("applicationName: {}, Offset: {}",applicationName,offset);
//        return (cont, dest, group) -> {
//            if (cont instanceof StreamListenerContainer container) {
//                container.setConsumerCustomizer((name, builder) -> {
//                    switch (offset)
//                    {
//                        case "last" -> {
//                            builder.name(applicationName);
//                            builder.offset(OffsetSpecification.last());}
//
//                        case "next" -> builder.offset(OffsetSpecification.next());
//                    }
//                    builder.subscriptionListener(context -> {
//                        switch (offset) {
//                            case "first" -> {
//                                log.info("Replaying from the first record in the stream");
//                                context.offsetSpecification(OffsetSpecification.first());
//                            }
//                        }
//                    });
//                });
//            }
//        };
//    }

}