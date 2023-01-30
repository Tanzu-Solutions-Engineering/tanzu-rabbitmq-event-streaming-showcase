package com.vmware.data.solutions.rabbitmq.streaming.messaging;

import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.ConsumerBuilder;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.StreamCreator;
import com.vmware.data.solutions.rabbitmq.streaming.repository.MessageBagRepository;
import com.vmware.data.solutions.rabbitmq.streaming.service.StreamDataService;
import org.apache.geode.pdx.PdxInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamDataServiceTest {

    @Mock
    private MessageBagRepository repository;

    @Mock
    private Environment environment;

    private String queueName = "transaction-1";

    @Mock
    private StreamCreator creator;

    @Mock
    private ConsumerBuilder consumerBuilder;

    @Mock
    private Consumer consumer;

    @Mock
    private PdxInstance pdx;
    private Function<byte[], PdxInstance> toPdx = bytes -> pdx;

    private StreamDataService subject;

    @BeforeEach
    void setUp() {
        subject = new StreamDataService(repository,environment,toPdx);
    }

    @Test
    void given_queue_when_load_putAll_messages() {

        when(environment.streamCreator()).thenReturn(creator);
        when(creator.stream(anyString())).thenReturn(creator);
        when(environment.consumerBuilder()).thenReturn(consumerBuilder);
        when(consumerBuilder.stream(anyString())).thenReturn(consumerBuilder);
        when(consumerBuilder.build()).thenReturn(consumer);
        when(consumerBuilder.offset(any())).thenReturn(consumerBuilder);
        when(consumerBuilder.messageHandler(any())).thenReturn(consumerBuilder);


        subject.registerFor(queueName);

        verify(consumerBuilder,atLeastOnce()).messageHandler(any());
    }
}