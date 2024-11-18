package com.coral.epidemicsimapiserver.service;

import com.coral.epidemicsimapiserver.repository.GameUpdatePacket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class GameUpdateListener {

    private static final String GAME_UPDATE_EXCHANGE_NAME = "game-update";

    private SimpMessagingTemplate messagingTemplate;
    private ObjectMapper objectMapper;

    @RabbitListener
    public void listenAndForwardGameUpdate() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(GAME_UPDATE_EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, GAME_UPDATE_EXCHANGE_NAME, queueName);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            GameUpdatePacket gameUpdatePacket = objectMapper.readValue(message, GameUpdatePacket.class);
            UUID gameID = gameUpdatePacket.SimID();
            messagingTemplate.convertAndSend(gameID.toString(), gameUpdatePacket.payload());
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
