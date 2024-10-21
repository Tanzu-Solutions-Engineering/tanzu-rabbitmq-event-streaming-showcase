/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */

package showcase.event.stream.rabbitmq.account.http.source;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.integration.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.rabbit.stream.config.SuperStream;

import static java.lang.String.valueOf;

@Configuration
@Slf4j
@Profile("superStream")
public class RabbitSuperStreamConfig {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String superStreamName;

    @Value("${rabbitmq.streaming.partitions:2}")
    private int partitions;

    private static final String routingKeyName = "id";


    SuperStream superStream(Environment environment) {

        log.info("Creating super stream: {}", superStreamName);

        environment.streamCreator().name(superStreamName)
                .superStream()
                .partitions(partitions).creator()
                .create();

        return new SuperStream(superStreamName, partitions);
    }

    @Bean
    Publisher<Account> publisher(Environment environment, Producer producer, Converter<Account, byte[]> converter) {
        return account -> {
            try {
                producer.send(producer.messageBuilder().applicationProperties()
                                .entry(routingKeyName, account.getId())
                                .messageBuilder().addData
                                        (converter.convert(account)).build(),
                        confirmationStatus -> {
                            if (confirmationStatus.isConfirmed())
                                log.info("SENT: {}", account);
                            else
                                log.error("NOT! SENT: {}", account);
                        });
            } catch (Exception e) {
                log.error("ERROR: {}", e);
            }
        };
    }

    @Bean
    Producer producer(Environment environment) {
        superStream(environment);

        return environment.producerBuilder()
                .superStream(superStreamName)
                .routing(msg -> {
                    var id = valueOf(msg.getApplicationProperties().get(routingKeyName));
                    log.info("routing id: {}",id);
                    return id;
                })
                .producerBuilder()
                .build();
    }
}