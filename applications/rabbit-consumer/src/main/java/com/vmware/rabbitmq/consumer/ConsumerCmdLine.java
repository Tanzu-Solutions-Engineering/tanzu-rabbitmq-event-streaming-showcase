package com.vmware.rabbitmq.consumer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import nyla.solutions.core.util.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConsumerCmdLine implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        //Get Settings
        var config = Config.loadArgs(args);
        var clientName = config.getProperty("clientName","Receive");
        var queueName =  config.getProperty("queue","hello-quorum");
        var exchangeName = config.getProperty("exchange","");
        var queueType = config.getProperty("queueType","quorum");
        var routingKeyValue = config.getProperty("routingKey","");
        int prefetchSize  = config.getPropertyInteger("prefetchSize",0);
        int prefetchCount  = config.getPropertyInteger("prefetchCount",1);
        boolean autoAckFlag = config.getPropertyBoolean("autoAck",true);
        var streamOffset = config.getProperty("streamOffset","last");
        var consumerArguments =  new HashMap<String, Object>();

        if("stream".equals(queueType))
        {
            consumerArguments.put("x-stream-offset", streamOffset);

        }

        //Make connection
        var factory = new ConnectionFactory();
        factory.setUri("amqp://user:bitnami@localhost");
        factory.setClientProperties(Map.of("name",clientName));

        try(var connection = factory.newConnection(clientName)) {

            try (var channel = connection.createChannel()) {
                channel.queueDeclare(queueName,
                        true,
                        false,
                        false,
                        Map.of("x-queue-type", queueType));

                //Declare Qualify of service consumption
                channel.basicQos(prefetchSize, prefetchCount, false);

                //Default Exchange Binding rules
                if (exchangeName.length() > 0)
                    channel.queueBind(queueName,
                            exchangeName,
                            routingKeyValue);


                System.out.println(" [*] Waiting for messages.");


                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");

                    if(!autoAckFlag)
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                };

                channel.basicConsume(queueName, autoAckFlag, consumerArguments, deliverCallback, consumerTag -> {
                });

                System.out.println(" Press [enter] to exit.");
                System.in.read();
            }
            }
        }


}
