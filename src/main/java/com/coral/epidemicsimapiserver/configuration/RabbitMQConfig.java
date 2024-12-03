package com.coral.epidemicsimapiserver.configuration;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${RMQ_URI}")
    private String RMQ_URI;

    public static final String GAME_UPDATE_QUEUE_NAME = "game_update";
    private static final String GAME_UPDATE_EXCHANGE_NAME = "game-updates";
    private static final String GAME_UPDATE_ROUTING_KEY = "test";

    private static final String INIT_GAME_QUEUE_NAME = "init_game";
    private static final String INIT_GAME_EXCHANGE_NAME = "init-game";


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

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(RMQ_URI); // Use the URI provided in application.properties
        return connectionFactory.getRabbitConnectionFactory();
    }

    @Bean
    public RabbitProperties rabbitProperties() {
        RabbitProperties properties = new RabbitProperties();
        properties.setAddresses(RMQ_URI);
        return properties;
    }

}
