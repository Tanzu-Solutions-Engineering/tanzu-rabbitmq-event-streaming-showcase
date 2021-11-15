package com.vmware.rabbitspringperftest;

import com.rabbitmq.perf.PerfTest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitSpringPerfTestApplication
{

	public static void main(String[] args) {
		SpringApplication.run(RabbitSpringPerfTestApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner()
	{
		CommandLineRunner commandLine = args -> {
			PerfTest.main(args);
		};

		return commandLine;
	}
}
