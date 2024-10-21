package com.vmware.tanzu.data.services.mqtt.generatormqttsource

import nyla.solutions.core.util.Config
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author Gregory Green
 */
@Configuration
@EnableScheduling
class MqttConfig {
    @Value("\${spring.application.name:generator-mqtt-source}")
    private var publisherId: String = "generator-mqtt-source";

    @Value("\${mqtt.connectionUrl:tcp://localhost:1883}")
    private var connectionUrl: String = "tcp://localhost:1883";

    @Value("\${mqtt.userName:mqtt}")
    private val userName: String = "mqtt";

    @Value("\${mqtt.userPassword:mqtt}")
    private val userPassword: String = "mqtt";

    @Bean
    fun mqttClientPublisher() : IMqttClient
    {
        val publisher: IMqttClient = MqttClient(connectionUrl, publisherId, MemoryPersistence())

        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        options.connectionTimeout = 10
        options.userName = userName
        options.password = userPassword.toCharArray()
        publisher.connect(options)

        return publisher

    }

    @Bean("template")
    fun template() : String{
        return Config.getProperty("GENERATOR_TEMPLATE")
    }

}