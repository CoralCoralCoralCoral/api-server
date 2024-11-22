package com.coral.epidemicsimapiserver.service;

import com.coral.epidemicsimapiserver.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class GameUpdateListener {

    @RabbitListener(queues = RabbitMQConfig.GAME_UPDATE_QUEUE_NAME)
    public void listen(String message) {
        System.out.println(message);
    }
}
