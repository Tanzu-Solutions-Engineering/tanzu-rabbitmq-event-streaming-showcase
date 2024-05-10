package com.vmware.account.jdbc.sink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.account.jdbc.sink.domain.AccountEntity;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class RabbitConsumerConfig {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    ConnectionNameStrategy connectionNameStrategy() {
        return (connectionFactory) -> applicationName;
    }

    @Bean
    ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }

    @Bean
    Converter<byte[], AccountEntity> messageConverter(ObjectMapper objectMapper) {
        return bytes -> {
            try {
                return objectMapper.readValue(bytes,AccountEntity.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
