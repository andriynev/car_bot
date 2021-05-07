package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.Output;
import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MainMenuService implements Handler {
    private final static String type = "MainMenu";
    private final static String initialStep = "initial";
    private final static String viewMenuStep = "view_menu";
    private final static List<String> buttons = new ArrayList<>(Arrays.asList("Expert", "Sto", "Tutorial"));

    @Override
    public Output handle(State state, InputMessage userInput) {
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

}
