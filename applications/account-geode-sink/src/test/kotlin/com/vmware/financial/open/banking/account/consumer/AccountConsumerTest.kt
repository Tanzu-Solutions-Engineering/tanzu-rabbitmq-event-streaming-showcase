package com.vmware.financial.open.banking.account.consumer

import com.vmware.financial.open.banking.account.domain.Account
import com.vmware.financial.open.banking.account.service.AccountService
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class AccountConsumerTest {

    private lateinit var accountService: AccountService
    private lateinit  var account : Account

    @BeforeEach
    internal fun setUp() {
        accountService = mock<AccountService>()
        account = JavaBeanGeneratorCreator.of(Account::class.java).create()
    }

    @Test
    fun accept() {
        account = JavaBeanGeneratorCreator.of(Account::class.java).create()
        var subject = AccountConsumer(accountService)
        subject.accept(account)

        verify(accountService).createAccount(any<Account>())
    }
}