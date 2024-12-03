package com.coral.epidemicsimapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class EpidemicSimApiServerApplication {

    public static final UUID SERVER_UUID = UUID.randomUUID();

    public static void main(String[] args) throws RuntimeException {
        SpringApplication.run(EpidemicSimApiServerApplication.class, args);
    }
}
