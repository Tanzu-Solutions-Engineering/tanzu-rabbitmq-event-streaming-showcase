package com.vmware.financial.open.banking.account.controller

import com.vmware.financial.open.banking.account.domain.Account
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * @author Gregory Green
 */
@RestController("/obp/v4.0.0")
class AccountController() {

    @PostMapping("banks/{bankId}/accounts")
    fun createAccount(@PathVariable("bankId") bankId : String, @RequestBody account: Account) : ResponseEntity<Account> {
        TODO("not implemented")
    }


}