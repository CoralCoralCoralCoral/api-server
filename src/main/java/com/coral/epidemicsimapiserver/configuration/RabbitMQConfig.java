package com.coral.epidemicsimapiserver.configuration;

import com.coral.epidemicsimapiserver.EpidemicSimApiServerApplication;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String NOTIFICATION_QUEUE_NAME = "notification-queue";
    private static final String NOTIFICATION_EXCHANGE_NAME = "notification";
    private static final String NOTIFICATION_ROUTING_KEY = EpidemicSimApiServerApplication.SERVER_UUID + ".*";

    private static final String INIT_QUEUE_NAME = "init-queue";
    public static final String INIT_EXCHANGE_NAME = "init";

    public static final String COMMAND_EXCHANGE_NAME = "command";

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable(NOTIFICATION_QUEUE_NAME).build();
    }

    @Bean
    public Exchange gameCommandsQueue() {
        return new ExchangeBuilder(COMMAND_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
    }

    @Bean
    public TopicExchange exchange() {
        return new ExchangeBuilder(NOTIFICATION_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) throws Exception {
        String routingKey = "";
        if (exchange.getName().equals(NOTIFICATION_EXCHANGE_NAME)) {
            routingKey = NOTIFICATION_ROUTING_KEY;
        } else {
            throw new Exception("No routing key associated to exchange");
        }
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public TopicExchange initExchange() {
        return new ExchangeBuilder(INIT_EXCHANGE_NAME, "topic").durable(false).autoDelete().build();
    }

    @Bean
    public Queue initGameQueue() {
        return QueueBuilder.nonDurable(INIT_QUEUE_NAME).build();
    }

    @Bean
    public Binding initGameBinding(Queue initGameQueue, TopicExchange initExchange) {
        return BindingBuilder.bind(initGameQueue).to(initExchange).with("#");
    }
}
