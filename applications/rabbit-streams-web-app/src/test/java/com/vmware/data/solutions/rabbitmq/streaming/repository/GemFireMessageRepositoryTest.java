package com.vmware.data.solutions.rabbitmq.streaming.repository;

import com.vmware.data.solutions.rabbitmq.streaming.domain.MessageBag;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GemFireMessageRepositoryTest {

    @Mock
    private Region<String,MessageBag> region;

     private Function<String,Region<String, MessageBag>> regionFactory = regionName -> {return region;};


    private GemFireMessageRepository subject;
    private String queueName = "test";
    private MessageBag message = JavaBeanGeneratorCreator.of(MessageBag.class).ignoreProperty("body").create();

    @BeforeEach
    void setUp() {
        subject = new GemFireMessageRepository(regionFactory);
    }

    @Test
    void saveForQueue() {
        subject.saveForQueue(queueName,message);

        verify(region).put(anyString(),any(MessageBag.class));
    }

    @Test
    void toKey() {
        String expected = message.getProperties().getMessageIdText()+"|"+message.getOffset();
        var actual = subject.toKey(message);
        assertEquals(expected, actual);
    }
}