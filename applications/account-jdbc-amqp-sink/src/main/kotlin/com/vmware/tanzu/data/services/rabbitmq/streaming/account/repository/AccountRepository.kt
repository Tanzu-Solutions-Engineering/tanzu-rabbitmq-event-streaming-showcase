package com.vmware.tanzu.data.services.rabbitmq.streaming.account.repository

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.AccountEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : CrudRepository<AccountEntity,String> {
}