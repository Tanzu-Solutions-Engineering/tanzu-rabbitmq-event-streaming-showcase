package com.vmware.financial.open.banking.account.controller

import com.vmware.financial.open.banking.account.domain.Account
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import nyla.solutions.core.util.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus

internal class AccountControllerTest{
    private lateinit var  rabbitTemplate: RabbitTemplate
    private val bankId: String = Text.generateId()
    private lateinit var account : Account

    @Test
    internal fun publishMessage() {
        rabbitTemplate = mock<RabbitTemplate>()

        account = JavaBeanGeneratorCreator.of(Account::class.java).create()
        var subject = AccountController(rabbitTemplate,)

        var actual = subject.createAccount(bankId,account);
        assertEquals(HttpStatus.OK, actual.statusCode)
        verify(rabbitTemplate).convertAndSend(any<String>(),any<String>(), any<Account>())

    }
}