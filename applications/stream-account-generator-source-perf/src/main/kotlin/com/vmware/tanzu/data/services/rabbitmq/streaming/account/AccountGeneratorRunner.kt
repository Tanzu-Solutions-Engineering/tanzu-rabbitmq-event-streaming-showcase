package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import nyla.solutions.core.patterns.workthread.ExecutorBoss
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate
import org.springframework.stereotype.Component
import java.text.DecimalFormat
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * Generates accounts
 * @author Gregory Green
 */
@Component
class AccountGeneratorRunner(
    private val template: RabbitStreamTemplate,
    @Value("\${account.generate.count:100000}")
    private val count: Long) : ApplicationRunner {
    private var futures: Future<*>? = null
    private var account : Account = JavaBeanGeneratorCreator.of(Account::class.java).create()
    private val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Generate and send given number of accounts streaming records
     */
    override fun run(args: ApplicationArguments?) {

        var startTime = System.currentTimeMillis()

        for(id in 1.. count) {
            var text =
                "{\"id\":\"$id\",\"name\":\"Account $id\",\"accountType\":\"Billing\",\"status\":\"OPEN\",\"notes\":\"Generated $id\",\"location\":{\"id\":\"$id\",\"address\":\"$id VMware Street\",\"cityTown\":\"City $id\",\"stateProvince\":\"State $id\",\"zipPostalCode\":\"2020$id\",\"countryCode\":\"US\"}}"

                    template.send(
                        template
                            .messageBuilder()
                            .properties().contentType("application/json")
                            .messageBuilder()
                            .addData(text.toByteArray(Charsets.UTF_8))
                            .build()
                    )
        }

        var endTime = System.currentTimeMillis()

        var totalTime = endTime - startTime


        logger.info("Completed loading {} records",DecimalFormat("#,###.##").format(count))


    }
}