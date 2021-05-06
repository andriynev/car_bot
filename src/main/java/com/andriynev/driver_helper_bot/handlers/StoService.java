package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class StoService implements Handler {
    private final static String type = "StoService";

    @Override
    public OutputMessage handle(State state, InputMessage userInput) {
        return new OutputMessage(ResponseType.MENU, "go to sto", userInput.getChatID());
    }

    @Override
    public String getType() {
        return type;
    }
}
