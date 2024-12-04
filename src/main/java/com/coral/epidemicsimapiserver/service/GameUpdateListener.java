package com.coral.epidemicsimapiserver.service;

import com.coral.epidemicsimapiserver.configuration.RabbitMQConfig;
import com.coral.epidemicsimapiserver.repository.Metrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GameUpdateListener {

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.GAME_UPDATE_QUEUE_NAME)
    public void listen(Message message) throws Exception {
        HashMap<String, Metrics> metrics = objectMapper.readValue(message.getBody(), HashMap.class);
        String gameId = message.getMessageProperties().getReceivedRoutingKey().split("\\.")[1];
        messagingTemplate.convertAndSend("/topic/game-update/" + gameId, metrics);
    }
}
