package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class StoService implements Handler {
    private final static String type = "Sto";

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        return new Output(new State(type, "final"), ResponseType.MENU, "go to sto");
    }

    @Override
    public String getType() {
        return type;
    }
}
