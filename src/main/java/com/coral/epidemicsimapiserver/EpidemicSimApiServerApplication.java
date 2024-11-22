package com.coral.epidemicsimapiserver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class EpidemicSimApiServerApplication {

    public static final UUID SERVER_UUID = UUID.randomUUID();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EpidemicSimApiServerApplication.class, args);
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        try (Connection connection = factory.createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.exchangeDeclare("init-game", "topic");
            channel.queueDeclare("init-game", false, false, false, null);
            channel.queueBind("init-game", "init-game", "#");
        }
    }

}
