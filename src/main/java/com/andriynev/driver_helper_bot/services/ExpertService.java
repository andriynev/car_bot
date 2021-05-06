package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ExpertService implements Handler {

    private final static String type = "ExpertService";

    @Override
    public SendMessage handle(State state, InputMessage userInput) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Go to Sto");
        sendMessage.setChatId(userInput.getChatID().toString());
        return sendMessage;
    }

    @Override
    public String getType() {
        return type;
    }
}
