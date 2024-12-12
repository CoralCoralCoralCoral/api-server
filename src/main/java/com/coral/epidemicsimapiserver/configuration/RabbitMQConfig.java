package com.coral.epidemicsimapiserver.configuration;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String GAME_UPDATE_QUEUE_NAME = "update-queue";
    private static final String GAME_UPDATE_EXCHANGE_NAME = "update";
    private static final String GAME_UPDATE_ROUTING_KEY = EpidemicSimApiServerApplication.SERVER_UUID + ".*";

    private static final String INIT_GAME_QUEUE_NAME = "init-queue";
    public static final String INIT_GAME_EXCHANGE_NAME = "init";

    public static final String GAME_COMMAND_EXCHANGE_NAME = "command";

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(GAME_UPDATE_QUEUE_NAME).build();
    }

    @Bean
    public Exchange gameCommandsQueue() {
        return new ExchangeBuilder(GAME_COMMAND_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
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

    @Bean
    public TopicExchange initExchange() {
        return new ExchangeBuilder(INIT_GAME_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
    }

    @Bean
    public Queue initGameQueue() {
        return QueueBuilder.nonDurable(INIT_GAME_QUEUE_NAME).build();
    }

    @Bean
    public Binding initGameBinding(Queue initGameQueue, TopicExchange initExchange) {
        return BindingBuilder.bind(initGameQueue).to(initExchange).with("#");
    }
}
