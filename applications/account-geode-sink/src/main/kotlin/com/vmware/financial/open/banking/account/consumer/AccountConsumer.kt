package com.vmware.financial.open.banking.account.consumer

import com.vmware.financial.open.banking.account.domain.Account
import com.vmware.financial.open.banking.account.service.AccountService
import org.springframework.stereotype.Component
import java.util.function.Consumer

/**
 * @author Gregory Green
 */
@Component
class AccountConsumer(private val accountService: AccountService) : Consumer<Account> {
    /**
     * Performs this operation on the given argument.
     *
     * @param account the input argument
     */
    override fun accept(account: Account) {
        accountService.createAccount(account)
    }
}