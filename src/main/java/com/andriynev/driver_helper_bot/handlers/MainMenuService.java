package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MainMenuService implements GroupHandler {
    private final static String type = "MainMenu";
    private final static String initialStep = "initial";
    private final static String viewMenuStep = "view_menu";
    private final List<String> buttons = new ArrayList<>();

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        switch (state.getStep()){
            case initialStep:
                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.MENU,
                        "MainMenu",
                        buttons,
                        null
                );
            case viewMenuStep:
                if (buttons.contains(userInput.getMessage())) {
                    Output out = new Output(
                            new State(userInput.getMessage(), initialStep),
                            ResponseType.QUESTION,
                            userInput.getMessage()
                    );
                    out.setRedirect(true);
                    return out;
                }

        }

        return new Output(
                new State(type, viewMenuStep),
                ResponseType.MENU,
                "MainMenu",
                buttons,
                null
        );
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setHandlers(List<Handler> group) {
        for (Handler handler: group) {
            buttons.add(handler.getType());
        }
    }
}
