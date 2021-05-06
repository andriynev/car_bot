package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;

@Service
public class ExpertService implements Handler {

    private final static String type = "ExpertService";

    @Override
    public OutputMessage handle(State state, String userInput) {
        return null;
    }

    @Override
    public String getType() {
        return type;
    }
}
