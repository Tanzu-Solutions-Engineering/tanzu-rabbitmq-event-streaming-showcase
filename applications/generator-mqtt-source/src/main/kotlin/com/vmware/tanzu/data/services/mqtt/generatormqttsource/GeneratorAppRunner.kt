package com.vmware.tanzu.data.services.mqtt.generatormqttsource

import nyla.solutions.core.patterns.creational.generator.GenerateTextWithPropertiesCreator
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

/**
 * @author Gregory Green
 */
@Component
class GeneratorAppRunner(
    private val client: IMqttClient,
    @Value("\${generator.mqtt.topic}")
    private val topic: String,
    @Qualifier("template")
    private val template: String
)  {

    private val logger: Logger = LoggerFactory.getLogger(GeneratorAppRunner::class.java)
    private val generator : GenerateTextWithPropertiesCreator = GenerateTextWithPropertiesCreator(template)

    @Scheduled(fixedRate = 5000)
    fun generateMessage() {

        val output = generator.create();
        logger.info("Sending {}", output)

        val msg : MqttMessage = MqttMessage(output.toByteArray(StandardCharsets.UTF_8))

        client.publish(topic,msg)
    }
}