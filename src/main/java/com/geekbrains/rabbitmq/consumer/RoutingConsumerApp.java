package com.geekbrains.rabbitmq.consumer;

import com.rabbitmq.client.*;

public class RoutingConsumerApp {
    private static final String EXCHANGE_NAME = "direct_exchange";

    // todo привязка двух очередей к одному директ exchanger'у

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        String routingKey = "[info]";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println(" [*] Waiting for messages");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");

        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
