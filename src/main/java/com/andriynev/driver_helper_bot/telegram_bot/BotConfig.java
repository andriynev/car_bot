package com.andriynev.driver_helper_bot.telegram_bot;

import com.andriynev.driver_helper_bot.services.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public DriverHelperBot driverHelperBot(DispatcherService dispatcherService) {
        DriverHelperBot driverHelperBot = new DriverHelperBot(dispatcherService);
        driverHelperBot.setBotUserName(botUserName);
        driverHelperBot.setBotToken(botToken);
        driverHelperBot.setWebHookPath(webHookPath);

        return driverHelperBot;
    }
}
