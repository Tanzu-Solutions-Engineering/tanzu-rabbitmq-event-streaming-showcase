package com.vmware.tanzu.data.services.rabbitmq.streaming.account.repository

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account,String> {
}