package com.coral.epidemicsimapiserver.controller;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import com.coral.epidemicsimapiserver.repository.CreateGameClientResponse;
import com.coral.epidemicsimapiserver.repository.CreateGamePacket;
import com.coral.epidemicsimapiserver.repository.PathogenConfig;
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
                UUID.randomUUID().toString(),
                15 * 60 * 1000,
                150000,
                new PathogenConfig(
                        new double[]{3 * 24 * 60 * 60 * 1000, 8 * 60 * 60 * 1000},
                        new double[]{7 * 24 * 60 * 60 * 1000, 8 * 60 * 60 * 1000},
                        new double[]{330 * 24 * 60 * 60 * 1000f, 90 * 24 * 60 * 60 * 1000f},
                        new double[]{3 * 24 * 60 * 60 * 1000, 8 * 60 * 60 * 1000},
                        new double[]{7 * 24 * 60 * 60 * 1000, 3 * 24 * 60 * 60 * 1000},
                        new double[]{250, 100},
                        0,
                        0
                ));

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
                EpidemicSimApiServerApplication.SERVER_UUID.toString() + '.' + createGamePacket.id(),
                json);

        CreateGameClientResponse response = new CreateGameClientResponse(
                createGamePacket.id(),
                EpidemicSimApiServerApplication.SERVER_UUID.toString()
        );
        String responseJson = null;
        try {
            responseJson = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseJson, HttpStatus.OK);
    }
}
