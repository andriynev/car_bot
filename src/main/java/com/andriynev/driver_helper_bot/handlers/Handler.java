package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;

@Service
public interface Handler {
    public OutputMessage handle(State state, InputMessage userInput);
    public String getType();
}
