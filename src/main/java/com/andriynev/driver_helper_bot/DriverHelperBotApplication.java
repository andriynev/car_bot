package com.andriynev.driver_helper_bot;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(MessagesProperties.class)
public class DriverHelperBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverHelperBotApplication.class, args);
    }

}
