package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountGeneratorPerfSourceApp

fun main(args: Array<String>) {
	runApplication<AccountGeneratorPerfSourceApp>(*args)
}
