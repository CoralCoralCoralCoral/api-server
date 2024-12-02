package com.coral.epidemicsimapiserver.configuration;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfiguration {
    public static final String GAME_UPDATE_QUEUE_NAME = "game-metrics-";
    private static final String GAME_UPDATE_EXCHANGE_NAME = "game-metrics";
    private static final String GAME_UPDATE_ROUTING_KEY = EpidemicSimApiServerApplication.SERVER_UUID + ".*";

    public static final String GAME_COMMAND_EXCHANGE_NAME = "game-commands";

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
}
