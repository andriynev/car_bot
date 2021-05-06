package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public interface Handler {
    public SendMessage handle(State state, InputMessage userInput);
    public String getType();
}
