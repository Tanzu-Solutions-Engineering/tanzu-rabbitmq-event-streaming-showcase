package com.vmware.data.solutions.rabbitmq.streaming;

import com.rabbitmq.stream.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.username:guest}")
    private String username;


    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    @Value("${spring.rabbitmq.host:localhost}")
    private String hostname;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    Environment rabbitStreamEnvironment() {

        return Environment.builder()
                .host(hostname)
                .clientProperty("name",applicationName)
                .username(username)
                .password(password).build();
    }
}
