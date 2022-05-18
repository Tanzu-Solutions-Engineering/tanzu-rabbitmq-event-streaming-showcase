package com.vmware.tanzu.data.services.rabbitmq.streaming.account.controller

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import nyla.solutions.core.util.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus

internal class AccountPublisherControllerTest{
    private lateinit var  rabbitTemplate: RabbitTemplate
    private lateinit var account : Account
    private lateinit var subject : AccountPublisherController

    @BeforeEach
    internal fun setUp() {
        rabbitTemplate = mock<RabbitTemplate>()

        account = JavaBeanGeneratorCreator.of(Account::class.java).create()

        subject = AccountPublisherController(rabbitTemplate,)
    }

    @Test
    internal fun verifyTransaction() {
        verify(rabbitTemplate).isChannelTransacted = true
    }

    @Test
    internal fun publishMessage() {

        var actual = subject.saveAccount(account);
        assertEquals(HttpStatus.OK, actual.statusCode)
        verify(rabbitTemplate).convertAndSend(any<String>(),any<String>(), any<Account>())

    }
}