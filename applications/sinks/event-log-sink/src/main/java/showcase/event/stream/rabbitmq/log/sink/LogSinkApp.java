/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */
package showcase.event.stream.rabbitmq.log.sink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LogSinkApp {

	public static void main(String[] args) {

		log.info("****** PROPERTIES: {}\n\n\n ENV: {}",System.getProperties(),System.getenv());
		SpringApplication.run(LogSinkApp.class, args);
	}

}
