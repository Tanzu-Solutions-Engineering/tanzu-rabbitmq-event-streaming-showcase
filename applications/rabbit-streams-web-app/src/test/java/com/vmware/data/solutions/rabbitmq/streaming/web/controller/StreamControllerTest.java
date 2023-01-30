package com.vmware.data.solutions.rabbitmq.streaming.web.controller;

import com.vmware.data.solutions.rabbitmq.streaming.service.StreamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StreamControllerTest {

    private String queueName = "test";

    @Mock
    private StreamService registry;

    @Test
    void given_stream_when_load_then_read_all_messages_intoCache() {

        var subject = new StreamController(registry);

        subject.load(queueName);

        verify(registry).registerFor(anyString());

    }
}