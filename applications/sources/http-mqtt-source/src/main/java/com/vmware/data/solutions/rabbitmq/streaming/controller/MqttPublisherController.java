package com.vmware.data.solutions.rabbitmq.streaming.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class MqttPublisherController {

    private final IMqttClient client;

    @PostMapping("mqtt/{topic}")
    @ResponseStatus(HttpStatus.OK)
    void post(@PathVariable String topic, @RequestBody String body) throws MqttException {
        client.publish(topic,new MqttMessage(body.getBytes(StandardCharsets.UTF_8)));
    }

}
