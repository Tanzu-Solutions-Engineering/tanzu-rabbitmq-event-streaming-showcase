package com.vmware.data.solutions.rabbitmq.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqttLogSinkApp {

    public static void main(String[] args) {
        SpringApplication.run(MqttLogSinkApp.class);
    }
}
