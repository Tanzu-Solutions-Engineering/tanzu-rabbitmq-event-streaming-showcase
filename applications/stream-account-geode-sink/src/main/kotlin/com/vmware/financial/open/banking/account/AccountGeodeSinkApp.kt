package com.vmware.financial.open.banking.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountGeodeSinkApp

fun main(args: Array<String>) {
	runApplication<AccountGeodeSinkApp>(*args)
}
