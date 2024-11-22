package com.coral.epidemicsimapiserver.controller;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import com.coral.epidemicsimapiserver.repository.CreateGamePacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/create")
    public ResponseEntity<String> createGame() {
        CreateGamePacket createGamePacket = new CreateGamePacket(
                EpidemicSimApiServerApplication.SERVER_UUID.toString(),
                UUID.randomUUID().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(createGamePacket);
        } catch (JsonProcessingException e) {
            // This may want to be changed to BAD_REQUEST when the user can add their own
            // initial options as both UUIDs as Strings should always be serializable
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        rabbitTemplate.convertAndSend(
                "init-game",
                createGamePacket.SERVER_UUID() + '.' + createGamePacket.SIMULATION_UUID(),
                json);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
