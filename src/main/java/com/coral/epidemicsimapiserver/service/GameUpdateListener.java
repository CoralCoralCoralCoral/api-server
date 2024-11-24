package com.coral.epidemicsimapiserver.service;

import com.coral.epidemicsimapiserver.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameUpdateListener {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.GAME_UPDATE_QUEUE_NAME)
    public void listen(String message) {
        System.out.println(message);
        messagingTemplate.convertAndSend("/topic/test" , message);
    }
}
