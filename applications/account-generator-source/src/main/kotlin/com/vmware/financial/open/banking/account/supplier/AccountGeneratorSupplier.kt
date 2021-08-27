package com.vmware.financial.open.banking.account.supplier

import com.vmware.financial.open.banking.account.domain.Account
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author Gregory Green
 */
@Component
class AccountGeneratorSupplier(@Value("\${sleepMs:3000}")private val sleepMs: Long = 3000) : Supplier<Account> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    override fun get(): Account {
        Thread.sleep(sleepMs)
        return JavaBeanGeneratorCreator.of(Account::class.java).create()
    }
}