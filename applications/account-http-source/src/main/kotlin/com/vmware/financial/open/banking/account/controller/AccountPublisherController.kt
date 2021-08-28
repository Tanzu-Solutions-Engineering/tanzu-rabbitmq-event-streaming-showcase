package com.vmware.financial.open.banking.account.controller

import com.vmware.financial.open.banking.account.domain.Account
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * @author Gregory Green
 */


@RestController("/obp/v4.0.0")
class AccountPublisherController(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${spring.cloud.stream.bindings.supplier-out-0.destination:banking-account}")
    private var exchangeId: String = "banking-account") {

    init {
        rabbitTemplate.isChannelTransacted = true
    }


    @PostMapping("banks/{bankId}/accounts")
    @Transactional
    fun createAccount(@PathVariable("bankId") bankId: String,
                      @RequestBody account: Account): ResponseEntity<Account> {
        rabbitTemplate.convertAndSend(exchangeId, bankId, account)

        return ResponseEntity.ok(account);
    }
}


