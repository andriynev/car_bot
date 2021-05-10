package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class StoService implements Handler {
    private final static String type = "Sto";
    private final static String initialStep = "initial";
    private final static String requestLocationStep = "request_location";

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        switch (state.getType()){
            case initialStep:
                return new Output(
                        new State(type, requestLocationStep),
                        ResponseType.MENU,
                        "Please provide your location",
                        new ArrayList<>(Arrays.asList(
                                new ReplyButton("Give location", true),
                                new ReplyButton("Main menu"))),
                        null);
            case requestLocationStep:
                if (userInput.getLocation() != null) {
                    return new Output(new State(type, initialStep), ResponseType.QUESTION, userInput.getLocation().toString());
                }
                return new Output(new State(type, initialStep), ResponseType.QUESTION, "Please provide your location");
            default:
                return new Output(new State(type, initialStep), ResponseType.MENU, "Please provide your location");
        }
    }

    @Override
    public String getType() {
        return type;
    }
}
