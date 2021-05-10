package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface Handler {
    public Output handle(User user, State state, InputMessage userInput);
    public String getType();
}

