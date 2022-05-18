package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.rabbitmq.stream.Message
import com.rabbitmq.stream.MessageBuilder
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.boot.ApplicationArguments
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate

/**
 * Test for AccountGeneratorRunner
 */
internal class AccountGeneratorRunnerTest{


    @Test
    internal fun run() {
        val template = mock<RabbitStreamTemplate>()
        val count:Long = 10
        val args = mock<ApplicationArguments>()
        val builder = mock<MessageBuilder>()
        val message = mock<Message>()
        val properties = mock<MessageBuilder.PropertiesBuilder>()


        whenever(template.messageBuilder()).thenReturn(builder)
        whenever(builder.addData(any())).thenReturn(builder)
        whenever(builder.build()).thenReturn(message)
        whenever(builder.properties()).thenReturn(properties)
        whenever(properties.contentType(any())).thenReturn(properties)
        whenever(properties.messageBuilder()).thenReturn(builder)

        var subject = AccountGeneratorRunner(template, count)

        subject.run(args)

        verify(template, times(count.toInt())).send(message)
    }
}