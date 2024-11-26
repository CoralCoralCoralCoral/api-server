package com.coral.epidemicsimapiserver.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;


@Configuration
public class RabbitMQConfig {
    public static final String GAME_UPDATE_QUEUE_NAME = "game_update";
    private static final String GAME_UPDATE_EXCHANGE_NAME = "game-updates";
    private static final String GAME_UPDATE_ROUTING_KEY = "test";

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(GAME_UPDATE_QUEUE_NAME).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new ExchangeBuilder(GAME_UPDATE_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(GAME_UPDATE_ROUTING_KEY);
    }
}
