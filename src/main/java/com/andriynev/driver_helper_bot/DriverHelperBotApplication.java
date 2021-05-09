package com.andriynev.driver_helper_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DriverHelperBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverHelperBotApplication.class, args);
    }

}
