package com.vmware.tanzu.data.services.rabbitmq.streaming.account.supplier

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author Gregory Green
 */
@Component
class AccountGeneratorSupplier(
    @Value("\${account.generator.sleepMs:3000}") private val sleepMs: Long = 3000,
    @Value("\${account.generator.maxAccountCnt:500000}")  private val maxAccountCnt: Long = 50000)
    : Supplier<Account> {

    private var account = JavaBeanGeneratorCreator.of(Account::class.java).create()

    private var currentCount = 1L
    override fun get(): Account {
        return nextAccount()
    }

    private fun nextAccount() : Account {
        Thread.sleep(sleepMs)

        account.id = currentCount.toString()
        account.location.id = account.id

        currentCount++
        currentCount %= maxAccountCnt

        return account
    }

    private var log = LogManager.getLogger(AccountGeneratorSupplier::class.java)
}
