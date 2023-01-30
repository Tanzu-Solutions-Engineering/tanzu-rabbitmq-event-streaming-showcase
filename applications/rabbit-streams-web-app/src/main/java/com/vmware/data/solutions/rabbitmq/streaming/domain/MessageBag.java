package com.vmware.data.solutions.rabbitmq.streaming.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.geode.pdx.PdxInstance;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageBag {
    private long offset;
    private PdxInstance body;
    private Long publishingId;
    private PropertiesBag properties;
    private Map<String, String>  applicationProperties;
    private Map<String, String> messageAnnotations;

}
