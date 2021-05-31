package com.andriynev.driver_helper_bot.controller;

import com.andriynev.driver_helper_bot.services.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class BotApiController {
    private final DispatcherService dispatcherService;

    @Autowired
    public BotApiController(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return dispatcherService.onWebhookUpdateReceived(update);
    }
}
