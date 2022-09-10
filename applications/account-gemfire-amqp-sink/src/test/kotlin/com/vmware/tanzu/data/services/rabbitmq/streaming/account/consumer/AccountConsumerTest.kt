package com.vmware.tanzu.data.services.rabbitmq.streaming.account.consumer

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.repository.AccountRepository
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class AccountConsumerTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit  var account : Account

    @BeforeEach
    internal fun setUp() {
        accountRepository = mock<AccountRepository>()
        account = JavaBeanGeneratorCreator.of(Account::class.java).create()
    }

    @Test
    fun accept() {
        account = JavaBeanGeneratorCreator.of(Account::class.java).create()
        var subject = AccountConsumer(accountRepository)
        subject.accept(account)

        verify(accountRepository).save(any<Account>())
    }
}