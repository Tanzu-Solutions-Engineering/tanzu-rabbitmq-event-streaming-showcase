package com.vmware.rabbitmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PubApp {

	public static void main(String[] args) {
		SpringApplication.run(PubApp.class, args);
	}

}
