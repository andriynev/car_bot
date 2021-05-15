package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SubscriptionsService implements Handler {
    private final static String type = "Subscriptions";
    private static final String initialStep = "initial";
    private final static String viewMenuStep = "view_menu";

    @Autowired
    public SubscriptionsService() {
    }

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        List<InlineButton> buttons = generateButtons(user.getSubscriptions());
        switch (state.getStep()){
            case initialStep:
                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.QUESTION,
                        "Categories",
                        buttons
                );
            case viewMenuStep:
                if (!User.allSubscriptions.contains(userInput.getMessage())) {
                    Output output = new Output(
                            new State(type, viewMenuStep),
                            ResponseType.CALLBACK_ANSWER,
                            "You choice undefined category: " + userInput.getMessage()
                    );
                    output.setShowAlert(true);
                    return output;
                }


                boolean isSubscription = !user.getSubscriptions().contains(userInput.getMessage());
                List<String> newSubs = new ArrayList<>(user.getSubscriptions());
                if (isSubscription) {
                    newSubs.add(userInput.getMessage());
                    user.setSubscriptions(newSubs);
                    buttons = generateButtons(newSubs);
                    Output output =  new Output(
                            new State(type, viewMenuStep),
                            ResponseType.CALLBACK_ANSWER,
                            "You successfully subscribed to: " + userInput.getMessage() + " category"
                    );
                    output.setShowAlert(true);
                    output.setMessages(Collections.singletonList(new Output(
                            new State(type, viewMenuStep),
                            ResponseType.EDIT_BUTTONS,
                            buttons
                    )));

                    return output;
                }

                newSubs.remove(userInput.getMessage());
                user.setSubscriptions(newSubs);
                buttons = generateButtons(newSubs);

                Output output = new Output(
                        new State(type, viewMenuStep),
                        ResponseType.CALLBACK_ANSWER,
                        "You successfully unsubscribed from: " + userInput.getMessage() + " category"
                );

                output.setShowAlert(true);
                output.setMessages(Collections.singletonList(new Output(
                        new State(type, viewMenuStep),
                        ResponseType.EDIT_BUTTONS,
                        buttons
                )));

                return output;

        }


        return new Output(
                new State(type, viewMenuStep),
                ResponseType.QUESTION,
                "Categories",
                buttons
        );
    }

    private List<InlineButton> generateButtons(List<String> userSubscriptions) {
        List<InlineButton> buttons = new ArrayList<>();
        for (String subscription: User.allSubscriptions) {
            if (userSubscriptions.contains(subscription)) {
                buttons.add(new InlineButton(subscription + " \uD83D\uDD14", subscription));
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
