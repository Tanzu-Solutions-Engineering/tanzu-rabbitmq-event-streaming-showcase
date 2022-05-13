package com.vmware.tanzu.data.services.mqtt.generatormqttsource

import nyla.solutions.core.util.Config
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GeneratorMqttSourceApplication

fun main(args: Array<String>) {
	Config.loadArgs(args)

	runApplication<GeneratorMqttSourceApplication>(*args)
}
