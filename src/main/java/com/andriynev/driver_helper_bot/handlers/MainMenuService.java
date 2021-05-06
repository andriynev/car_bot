package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MainMenuService implements Handler {
    private final static String type = "MainMenuService";


    @Override
    public OutputMessage handle(State state, InputMessage userInput) {
        ArrayList<String> buttons = new ArrayList<>();
        buttons.add("Expert");
        buttons.add("Sto");
        buttons.add("Tutorial");
        OutputMessage mainMenuOutputMessage = new OutputMessage(
                ResponseType.MENU,
                "MainMenu",
                buttons,
                null,
                userInput.getChatID()
        );
        return mainMenuOutputMessage;
    }

    @Override
    public String getType() {
        return type;
    }

}
