package com.vmware.data.solutions.rabbitmq.streaming.repository;

import com.vmware.data.solutions.rabbitmq.streaming.domain.MessageBag;

public interface MessageBagRepository {
    void saveForQueue(String queue, MessageBag message);
}
