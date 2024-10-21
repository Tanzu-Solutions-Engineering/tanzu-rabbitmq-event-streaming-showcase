package com.vmware.tanzu.data.services.rabbitmq.streaming.account.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus

internal class HttpPublisherControllerTest{
    private val contentType: String = "application/json"
    private val exchange: String  = "hello"
    private val objectMapper: ObjectMapper = ObjectMapper()
    private lateinit var  rabbitTemplate: RabbitTemplate
    private lateinit var account : Account
    private lateinit var subject : HttpPublisherController

    @BeforeEach
    internal fun setUp() {
        rabbitTemplate = mock<RabbitTemplate>()

        account = JavaBeanGeneratorCreator.of(Account::class.java).create()

        subject = HttpPublisherController(rabbitTemplate)
    }

    @Test
    internal fun verifyTransaction() {
        verify(rabbitTemplate).isChannelTransacted = true
    }

    @Test
    internal fun publishMessage() {

        val routingKey = "hello"
        var actual = subject.postMessage(contentType, exchange, routingKey,objectMapper.writeValueAsString(account));
        assertEquals(HttpStatus.OK, actual.statusCode)
        verify(rabbitTemplate).send(any<String>(),any<String>(), any<Message>())

    }
}