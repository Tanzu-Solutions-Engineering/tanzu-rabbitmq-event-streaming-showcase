package com.vmware.data.solutions.rabbitmq.streaming.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertiesBag {

    private String messageIdText;

    private String userId;

    private String to;

    private String subject;

    private String replyTo;

    private String correlationIdText;

    private String contentType;

    private String contentEncoding;

    private long getAbsoluteExpiryTime;

    private long creationTime;

    private String groupId;

    private long groupSequence;

    private String replyToGroupId;
}
