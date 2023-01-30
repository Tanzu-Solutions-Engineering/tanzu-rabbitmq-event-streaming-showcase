package com.vmware.data.solutions.rabbitmq.streaming.messaging;

import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.MessageHandler;
import com.rabbitmq.stream.Properties;
import com.vmware.data.solutions.rabbitmq.streaming.domain.MessageBag;
import com.vmware.data.solutions.rabbitmq.streaming.domain.PropertiesBag;
import com.vmware.data.solutions.rabbitmq.streaming.repository.MessageBagRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.pdx.PdxInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

import static nyla.solutions.core.util.Organizer.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryMessageHandlerTest {

    private String queueName  = "hello";
    @Mock
    private MessageBagRepository repository;

    @Mock
    private MessageHandler.Context context;

    @Mock
    private Message message;

    @Mock
    private Properties properties;

    @Mock
    private PdxInstance pdx ;

    private Function<byte[], PdxInstance> toPdx = bytes -> pdx;

    private RepositoryMessageHandler subject;
    private MessageBag messageBag;
    private byte[] messageBytes = "{}".getBytes(StandardCharsets.UTF_8);
    private Map<String, String> messageAnnotations ;
    private Map<String, String> applicationProperties;
    private long offset = 24;
    private Long publisherId = 34L;
    private String messageId = "103121";
    private PropertiesBag propertiesBag;

    @BeforeEach
    void setUp() {
        messageAnnotations =  toMap("msg1","1");
        applicationProperties =  toMap("ap1","1");

        propertiesBag = JavaBeanGeneratorCreator.of(PropertiesBag.class).create();

                messageBag = MessageBag.builder()
                .body(pdx)
                .messageAnnotations(messageAnnotations)
                        .properties(propertiesBag)
                .applicationProperties(applicationProperties)
                .offset(offset)
                .publishingId(publisherId)
                .build();

        subject = new RepositoryMessageHandler(repository,toPdx,queueName);
    }

    @Test
    void given_contentAndMessage_when_handle_then_saveToRepository() {
        subject.handle(context,message);

        verify(repository).saveForQueue(anyString(),any(MessageBag.class));
    }

    @Test
    void toMessageBag() {
        when(properties.getMessageIdAsString()).thenReturn(propertiesBag.getMessageIdText());
        when(properties.getUserId()).thenReturn(propertiesBag.getUserId().getBytes(StandardCharsets.UTF_8));
        when(properties.getTo()).thenReturn(propertiesBag.getTo());
        when(properties.getSubject()).thenReturn(propertiesBag.getSubject());
        when(properties.getReplyTo()).thenReturn(propertiesBag.getReplyTo());
        when(properties.getReplyToGroupId()).thenReturn(propertiesBag.getReplyToGroupId());
        when(properties.getCorrelationIdAsString()).thenReturn(propertiesBag.getCorrelationIdText());
        when(properties.getContentType()).thenReturn(propertiesBag.getContentType());
        when(properties.getContentEncoding()).thenReturn(propertiesBag.getContentEncoding());
        when(properties.getAbsoluteExpiryTime()).thenReturn(propertiesBag.getGetAbsoluteExpiryTime());
        when(properties.getCreationTime()).thenReturn(propertiesBag.getCreationTime());
        when(properties.getGroupId()).thenReturn(propertiesBag.getGroupId());
        when(properties.getGroupSequence()).thenReturn(propertiesBag.getGroupSequence());
        when(properties.getReplyToGroupId()).thenReturn(propertiesBag.getReplyToGroupId());


        when(message.getPublishingId()).thenReturn(publisherId);
        when(message.getApplicationProperties()).thenReturn((Map)applicationProperties);
        when(message.getMessageAnnotations()).thenReturn((Map)messageBag.getMessageAnnotations());
        when(message.getBodyAsBinary()).thenReturn(messageBytes);
        when(message.getProperties()).thenReturn(properties);
        when(context.offset()).thenReturn(messageBag.getOffset());


        var actual = subject.toMessageBag(context,message);

        assertEquals(messageBag, actual);

    }
}