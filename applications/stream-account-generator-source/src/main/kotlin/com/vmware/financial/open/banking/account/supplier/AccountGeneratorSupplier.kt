package com.vmware.financial.open.banking.account.supplier

import com.vmware.financial.open.banking.account.domain.Account
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
    @Value("\${account.generator.sleepMs:3000}")private val sleepMs: Long = 3000) : Supplier<Account> {

    override fun get(): Account {
        return nextAccount()
    }

    private fun nextAccount() :Account {
        Thread.sleep(sleepMs)
        var account = JavaBeanGeneratorCreator.of(Account::class.java).create()
        log.info("account: account {}",account)
        return account
    }

    private var log = LogManager.getLogger(AccountGeneratorSupplier::class.java)
}
