package com.vmware.data.solutions.rabbitmq.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttSourceApp {
    public static void main(String[] args) {
        SpringApplication.run(MqttSourceApp.class);
    }
}
