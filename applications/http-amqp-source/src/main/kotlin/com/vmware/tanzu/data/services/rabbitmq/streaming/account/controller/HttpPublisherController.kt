package com.vmware.tanzu.data.services.rabbitmq.streaming.account.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.Header
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets

/**
 * @author Gregory Green
 */
@RestController("event-streaming-showcase")
class HttpPublisherController(
    private val rabbitTemplate: RabbitTemplate) {

    private val logger: Logger = LoggerFactory.getLogger("HttpPublisherController")

    init {
        rabbitTemplate.isChannelTransacted = true
    }

    @PostMapping("amqp/")
    @Transactional
    fun postMessage(
        @RequestHeader(defaultValue = "application/json") rabbitContentType : String,
        @RequestParam(defaultValue = "amq.topic") exchange : String,
        @RequestParam(defaultValue = "") routingKey : String,
        @RequestBody body: String
    ): ResponseEntity<String> {
        rabbitTemplate.send(exchange,
            routingKey,
            MessageBuilder.withBody(body.toByteArray(StandardCharsets.UTF_8))
                .setContentType(rabbitContentType)
                .build())

        logger.info("Send message exchange:$exchange routingKey:$routingKey body:$body")
        return ResponseEntity.ok(body)
    }
}


