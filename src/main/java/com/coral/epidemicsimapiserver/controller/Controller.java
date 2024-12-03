package com.coral.epidemicsimapiserver.controller;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import com.coral.epidemicsimapiserver.configuration.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("/index.html");
    }

    @MessageMapping("/game/{id}/command")
    public void forwardCommands(@Payload String payload, @DestinationVariable("id") String id) {
        System.out.println(id + "---" + payload);
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(new CachingConnectionFactory());
        rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.GAME_COMMAND_EXCHANGE_NAME,
                EpidemicSimApiServerApplication.SERVER_UUID + "." + id,
                payload);
    }
}
