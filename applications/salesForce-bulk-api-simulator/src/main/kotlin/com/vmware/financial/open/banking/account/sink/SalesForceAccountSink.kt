package com.vmware.financial.open.banking.account.sink

import com.vmware.financial.open.banking.account.domain.Account
import org.springframework.stereotype.Component
import java.util.function.Consumer
import java.util.function.Function


@Component
class SalesForceAccountSink(
    private val bulkApi : BulkApiWrapper,
    private val converter : Function<Account,String>) : Consumer<Account> {

    override fun accept(account: Account) {
        bulkApi.uploadJobData(converter.apply(account))
    }
}

