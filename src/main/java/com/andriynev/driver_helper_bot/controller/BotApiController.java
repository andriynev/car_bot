package com.andriynev.driver_helper_bot.controller;

import com.andriynev.driver_helper_bot.telegram_bot.DriverHelperBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class BotApiController {
    private final DriverHelperBot telegramBot;

    @Autowired
    public BotApiController(DriverHelperBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
