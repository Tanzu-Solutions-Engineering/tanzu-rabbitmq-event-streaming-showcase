package com.example.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class RabbitConfiguration {
    @Bean
    public Consumer<String> sink1() {
        return message -> {
            System.out.println("******************");
            System.out.println("At Queue1");
            System.out.println("******************");
            System.out.println("Received message " + message);
        };
    }

    @Bean
    public Consumer<String> sink2() {
        return message -> {
            System.out.println("******************");
            System.out.println("At Queue2");
            System.out.println("******************");
            System.out.println("Received message " + message);
        };
    }

    @Bean
    public Supplier<String> source1() {
        return () -> {
            String message = "Message From Topic Exchange1";
            System.out.println("******************");
            System.out.println("From Topic Exchange1");
            System.out.println("******************");
            System.out.println("Sending value: " + message);
            return message;

        };
    }

    @Bean
    public Supplier<String> source2() {
        return () -> {
            String message = "Message From Topic Exchange2";
            System.out.println("******************");
            System.out.println("From Topic Exchange2");
            System.out.println("******************");
            System.out.println("Sending value: " + message);
            return message;

        };
    }
}
