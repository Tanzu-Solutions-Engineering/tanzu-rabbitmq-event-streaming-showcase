package com.vmware.data.solutions.rabbitmq.streaming.repository;

import com.vmware.data.solutions.rabbitmq.streaming.domain.MessageBag;
import lombok.RequiredArgsConstructor;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Text;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class GemFireMessageRepository implements MessageBagRepository{

    private final Function<String, Region<String, MessageBag>> regionFactory;

    @Override
    public void saveForQueue(String queue, MessageBag message) {
        var region = regionFactory.apply(queue);

        if(region == null)
            throw new IllegalArgumentException("GemFire region match queue name not found queue: "+queue);

        String id = toKey(message);

        region.put(id,message);
    }

    protected String toKey(MessageBag message) {
        String messageId = "";
        var properties = message.getProperties();

        if(properties != null)
            messageId = properties.getMessageIdText();


        return new StringBuilder().append(messageId).append("|").append(message.getOffset()).toString();
    }
}
