package com.andriynev.driver_helper_bot.telegram_bot;

import com.andriynev.driver_helper_bot.services.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BotConfig {

    @Bean
    @Autowired
    public DriverHelperBot driverHelperBot(DispatcherService dispatcherService, Environment env) {
        String botUserName = env.getProperty("telegrambot.userName");
        String webHookPath = env.getProperty("telegrambot.webHookPath");
        String botToken = env.getProperty("telegrambot.botToken");
        DriverHelperBot driverHelperBot = new DriverHelperBot(dispatcherService);
        driverHelperBot.setBotUserName(botUserName);
        driverHelperBot.setBotToken(botToken);
        driverHelperBot.setWebHookPath(webHookPath);

        return driverHelperBot;
    }
}
