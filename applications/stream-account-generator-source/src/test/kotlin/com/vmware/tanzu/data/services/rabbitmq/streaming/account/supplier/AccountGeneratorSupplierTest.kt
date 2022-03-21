package com.vmware.tanzu.data.services.rabbitmq.streaming.account.supplier

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AccountGeneratorSupplierTest {

    @Test
    fun get() {
        val subject = AccountGeneratorSupplier()
        var actual = subject.get()
        assertTrue(actual.id.isNotEmpty());
    }
}