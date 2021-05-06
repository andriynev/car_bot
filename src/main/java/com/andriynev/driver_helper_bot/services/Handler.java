package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;

@Service
public interface Handler {
    public OutputMessage handle(State state, String userInput);
    public String getType();
}
