package com.vmware.tanzu.data.services.rabbitmq.streaming.account.supplier

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class AccountGeneratorSupplierTest {

    private val maxCnt =  10L;
    private lateinit var subject : AccountGeneratorSupplier

    @BeforeEach
    internal fun setUp() {
        subject = AccountGeneratorSupplier(sleepMs = 0, maxAccountCnt= maxCnt)
    }

    @Test
    fun get() {
        var actual = subject.get()
        assertTrue(actual.id.isNotEmpty());
    }

    @Test
    internal fun given_maxCnt_When_get_Then_accountIdWithRange() {


        var actual : Account = subject.get()
        assertEquals("1",actual.id);

        for (i in 2..maxCnt*2)
        {
            actual = subject.get()
            assertEquals((i%maxCnt).toString(),actual.id);
        }

    }
}