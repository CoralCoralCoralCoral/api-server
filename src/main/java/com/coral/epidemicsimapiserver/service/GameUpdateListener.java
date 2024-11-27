package com.coral.epidemicsimapiserver.service;

import com.coral.epidemicsimapiserver.configuration.RabbitMQConfiguration;
import com.coral.epidemicsimapiserver.repository.Metrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameUpdateListener {

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfiguration.GAME_UPDATE_QUEUE_NAME)
    public void listen(Message message) throws Exception {
        Metrics metrics = objectMapper.readValue(message.getBody(), Metrics.class);
        System.out.println(message.getMessageProperties().getReceivedRoutingKey() + "---" + metrics);
        messagingTemplate.convertAndSend("/topic/test" , message.getBody());
    }
}
