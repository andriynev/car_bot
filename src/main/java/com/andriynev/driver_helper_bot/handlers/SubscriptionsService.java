package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
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
        switch (state.getStep()){
            case initialStep:
                List<InlineButton> buttons = generateButtons(user.getSubscriptions());
                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.QUESTION,
                        "Your subscriptions",
                        buttons
                );
            case viewMenuStep:
                if (!User.allSubscriptions.contains(userInput.getMessage())) {
                    return new Output(
                            new State(type, viewMenuStep),
                            ResponseType.CALLBACK_ANSWER,
                            "You choice undefined category: " + userInput.getMessage()
                    );
                }

                boolean isSubscription = !user.getSubscriptions().contains(userInput.getMessage());
                if (isSubscription) {
                    user.getSubscriptions().add(userInput.getMessage());
                    userService.save(user);

                    return new Output(
                            new State(type, viewMenuStep),
                            ResponseType.CALLBACK_ANSWER,
                            "You successfully subscribed to: " + userInput.getMessage()
                    );
                }

                user.getSubscriptions().remove(userInput.getMessage());
                userService.save(user);

                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.CALLBACK_ANSWER,
                        "You successfully unsubscribed from: " + userInput.getMessage()
                );

        }

        List<InlineButton> buttons = generateButtons(user.getSubscriptions());
        return new Output(
                new State(type, viewMenuStep),
                ResponseType.QUESTION,
                "Your subscriptions",
                buttons
        );
    }

    private List<InlineButton> generateButtons(List<String> userSubscriptions) {
        List<InlineButton> buttons = new ArrayList<>();
        for (String subscription: User.allSubscriptions) {
            if (userSubscriptions.contains(subscription)) {
                buttons.add(new InlineButton(subscription + " ✅", subscription));
            } else {
                buttons.add(new InlineButton(subscription));
            }
        }
        return buttons;
    }

    @Override
    public String getType() {
        return type;
    }
}
