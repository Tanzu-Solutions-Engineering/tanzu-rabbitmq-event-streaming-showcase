package com.vmware.data.solutions.rabbitmq.streaming.service;

import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;
import com.vmware.data.solutions.rabbitmq.streaming.messaging.RepositoryMessageHandler;
import com.vmware.data.solutions.rabbitmq.streaming.repository.MessageBagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class StreamDataService implements StreamService {

    private final MessageBagRepository repository;
    private final Environment environment;
    private final Function<byte[], PdxInstance> toPdx;
    private Map<String, Consumer> consumerMap = new HashMap<>();

    @Override
    public void registerFor(String streamName) {
//        if(!consumerMap.containsKey(queueName)) {
            consumerMap.put(streamName,constructConsumerFor(streamName));
//        }
    }

    private com.rabbitmq.stream.Consumer constructConsumerFor(String queueName) {

        //create stream
        var creator = environment.streamCreator().stream(queueName);
       try{ creator.create(); } catch(Exception e){}


        return environment.consumerBuilder()
                .stream(queueName)
                .offset(OffsetSpecification.first())
                .messageHandler(new RepositoryMessageHandler(repository,toPdx,queueName))
                .build();
    }
}
