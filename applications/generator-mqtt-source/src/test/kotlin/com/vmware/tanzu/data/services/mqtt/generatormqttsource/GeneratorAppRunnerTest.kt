package com.vmware.tanzu.data.services.mqtt.generatormqttsource

import org.eclipse.paho.client.mqttv3.IMqttClient
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class GeneratorAppRunnerTest{
    private val topic = "topic"

    @Test
    internal fun generateMessage() {

        var template = "hello \${name} this is cool and the date is \${date}"

        var client : IMqttClient = mock<IMqttClient>()

        var subject = GeneratorAppRunner(client, topic, template)

        subject.generateMessage()

        verify(client).publish(any<String>(), any())
    }
}