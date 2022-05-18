package com.vmware.tanzu.data.services.rabbitmq.streaming.account.consumer

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.repository.AccountRepository
import org.springframework.stereotype.Component
import java.util.function.Consumer

/**
 * @author Gregory Green
 */
@Component
class AccountConsumer(private val accountService: AccountRepository) :
    Consumer<Account> {
    override fun accept(account: Account) {
        accountService.save(account)
    }
}
