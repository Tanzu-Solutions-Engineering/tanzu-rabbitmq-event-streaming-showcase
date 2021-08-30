package com.vmware.financial.open.banking.account.service

import com.vmware.financial.open.banking.account.domain.Account
import org.apache.logging.log4j.LogManager
import org.springframework.data.gemfire.GemfireTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Gregory Green
 */
@Service
class AccountDataService(private val gemFireTemplate: GemfireTemplate) : AccountService  {
    private val log = LogManager.getLogger(AccountDataService::class.java)
    fun toKey(bankId: String, accountId: String): String {
        return "$bankId|$accountId"
    }
     fun toKey(account: Account): String
     {
         return toKey(account.bank_id,account.id)
     }

    override fun createAccount(account: Account): Account {

        log.info("SAVING account:{}",account)
        gemFireTemplate.put(toKey(account),account)
        return account
    }

    override fun findAccountById(bankId: String, accountId: String): Optional<Account> {
        var account : Account? = gemFireTemplate[toKey(bankId,accountId)] ?: return Optional.empty()
        return Optional.of(account!!)
    }

    override fun updateAccount(account: Account): Optional<Account> {
        var key = toKey(account)
        if(!gemFireTemplate.containsKeyOnServer(key))
            return Optional.empty()

        gemFireTemplate.put(key,account)
        return Optional.of(account)
    }
}