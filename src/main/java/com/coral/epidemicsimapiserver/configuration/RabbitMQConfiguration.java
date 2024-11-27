package com.coral.epidemicsimapiserver.configuration;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfiguration {
    public static final String GAME_UPDATE_QUEUE_NAME = "game-metrics-";
    private static final String GAME_UPDATE_EXCHANGE_NAME = "game-metrics";
    private static final String GAME_UPDATE_ROUTING_KEY = EpidemicSimApiServerApplication.SERVER_UUID + ".*";

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(GAME_UPDATE_QUEUE_NAME).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new ExchangeBuilder(GAME_UPDATE_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) throws Exception{
        String routingKey = "";
        if (exchange.getName().equals(GAME_UPDATE_EXCHANGE_NAME)) {
            routingKey = GAME_UPDATE_ROUTING_KEY;
        } else {
            throw new Exception("No routing key associated to exchange");
        }
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
