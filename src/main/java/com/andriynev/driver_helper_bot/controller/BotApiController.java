package com.andriynev.driver_helper_bot.controller;

import com.andriynev.driver_helper_bot.dto.JwtTokenResponse;
import com.andriynev.driver_helper_bot.services.DispatcherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Process telegram update", tags = "telegram")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success",
            content = { @Content(mediaType = "application/json") }))
    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return dispatcherService.onWebhookUpdateReceived(update);
    }
}
