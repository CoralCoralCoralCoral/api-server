package com.coral.epidemicsimapiserver.controller;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import com.coral.epidemicsimapiserver.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("/index.html");
    }

    @MessageMapping("/game/{id}/command")
    public void forwardCommands(@Payload String payload, @DestinationVariable("id") String id) {
        System.out.println(id + "---" + payload);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMAND_EXCHANGE_NAME,
                EpidemicSimApiServerApplication.SERVER_UUID + "." + id,
                payload);
    }
}
