package com.vmware.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.settings.ConfigSettings;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class PublishCmdLine implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

        var config = Config.loadArgs(args);
        var clientName = config.getProperty("clientName","Send");
        var routingKeyValue = config.getProperty("routingKey","hello-quorum");
        var exchangeName = config.getProperty("exchange","");
        var message = config.getProperty("message","Hello World!");
        var body = message.getBytes(StandardCharsets.UTF_8);


        var factory = new ConnectionFactory();
        factory.setUri("amqp://user:bitnami@localhost");
        try (Connection connection = factory.newConnection(clientName);
             Channel channel = connection.createChannel()) {

            channel.basicPublish(exchangeName,
                    routingKeyValue,
                    null,
                    body);
            System.out.println(" [x] Sent "+message);

            System.out.println(" Press [enter] to exit.");
            System.in.read();

        }



    }
}
