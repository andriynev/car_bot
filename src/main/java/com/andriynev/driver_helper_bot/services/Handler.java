package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.Message;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;

@Service
public interface Handler {
    public Message handle(State state, String userInput);
    public String getType();
}
