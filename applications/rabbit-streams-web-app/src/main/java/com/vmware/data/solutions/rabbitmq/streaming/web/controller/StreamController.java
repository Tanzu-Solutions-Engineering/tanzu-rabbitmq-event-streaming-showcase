package com.vmware.data.solutions.rabbitmq.streaming.web.controller;

import com.vmware.data.solutions.rabbitmq.streaming.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("streams")
public class StreamController {

    private final StreamService registry;

    @PostMapping(path = "stream")
    public void load(String queueName) {

        registry.registerFor(queueName);
    }
}
