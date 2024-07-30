package com.vmware.data.solutions.rabbitmq.streaming.messaging;

import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.MessageHandler;
import com.rabbitmq.stream.Properties;
import com.vmware.data.solutions.rabbitmq.streaming.domain.MessageBag;
import com.vmware.data.solutions.rabbitmq.streaming.domain.PropertiesBag;
import com.vmware.data.solutions.rabbitmq.streaming.repository.MessageBagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.util.Organizer;
import nyla.solutions.core.util.Text;
import org.apache.geode.pdx.PdxInstance;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class RepositoryMessageHandler implements MessageHandler {

    private final MessageBagRepository repository;
    private final Function<byte[], PdxInstance> toPdx;
    private final String streamQueueName;

    @Override
    public void handle(Context context, Message message) {

        var msg = toMessageBag(context,message);

        log.info("Saving msg: {}",msg);

        repository.saveForQueue(streamQueueName,msg);
    }

    protected MessageBag toMessageBag(Context context, Message message) {

        return MessageBag.builder()
                .offset(context.offset())
                .body(toPdx.apply(message.getBodyAsBinary()))
                .publishingId(message.getPublishingId())
                .properties(toPropertyBag(message.getProperties()))
                .applicationProperties(
                        Organizer.convertMap(message.getApplicationProperties(), k -> k.toString(), v -> Text.toString(v)))
                .messageAnnotations(
                        Organizer.convertMap(message.getMessageAnnotations(), k -> k.toString(), v -> Text.toString(v)))
                .build();
    }

    private PropertiesBag toPropertyBag(Properties properties) {
        if(properties == null)
            return null;

        var builder = PropertiesBag.builder()
                .messageIdText(properties.getMessageIdAsString())
                .to(properties.getTo())
                .contentType(properties.getContentType())
                .contentEncoding(properties.getContentEncoding())
                .creationTime(properties.getCreationTime())
                .correlationIdText(properties.getCorrelationIdAsString())
                .replyTo(properties.getReplyTo())
                .replyToGroupId(properties.getReplyToGroupId())
                .groupId(properties.getGroupId())
                .getAbsoluteExpiryTime(properties.getAbsoluteExpiryTime())
                .subject(properties.getSubject())
                .groupSequence(properties.getGroupSequence());

            if(properties.getUserId() != null)
                builder.userId( new String(properties.getUserId(), StandardCharsets.UTF_8));

            return builder.build();

    }
}
