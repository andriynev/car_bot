package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.Output;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionsService implements Handler {
    private final static String type = "Subscriptions";
    private final UserService userService;
    private static final String initialStep = "initial";
    private final static String viewMenuStep = "view_menu";

    @Autowired
    public SubscriptionsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Output handle(State state, InputMessage userInput) {
        User user = userService.getOrCreateUser(userInput.getChatID());
        List<String> buttons = generateButtons(user.getSubscriptions());
        switch (state.getStep()){
            case initialStep:
                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.QUESTION,
                        "Your subscriptions",
                        buttons
                );
            case viewMenuStep:

                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.CALLBACK_ANSWER,
                        "You choice " + userInput.getMessage()
                );

        }

        return new Output(
                new State(type, viewMenuStep),
                ResponseType.QUESTION,
                "Your subscriptions",
                buttons
        );
    }

    private List<String> generateButtons(List<String> userSubscriptions) {
        List<String> buttons = new ArrayList<>();
        for (String subscription: User.allSubscriptions) {
            if (userSubscriptions.contains(subscription)) {
                buttons.add(subscription + " ✅");
            } else {
                buttons.add(subscription);
            }
        }
        return buttons;
    }

    @Override
    public String getType() {
        return type;
    }
}
