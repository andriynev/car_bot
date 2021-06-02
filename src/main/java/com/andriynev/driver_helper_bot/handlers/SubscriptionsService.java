package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
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
    private String humanReadableName = type;
    private final static String nameMessageKey = "subscriptions";
    private static final String initialStep = "initial";
    private final static String viewMenuStep = "view_menu";
    private final MessagesProperties messagesProperties;
    private String description;

    @Autowired
    public SubscriptionsService(MessagesProperties messagesProperties) {
        this.messagesProperties = messagesProperties;
        this.setHumanReadableName(this.messagesProperties.getMessage(nameMessageKey));
        this.setDescription(this.messagesProperties.getMessage(nameMessageKey+"-description"));
    }

    @Override
    public String getHumanReadableName() {
        return humanReadableName;
    }

    @Override
    public void setHumanReadableName(String humanReadableName) {
        if (humanReadableName == null || humanReadableName.isEmpty()) {
            return;
        }
        this.humanReadableName = humanReadableName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        List<InlineButton> buttons = generateButtons(user.getSubscriptions());
        switch (state.getStep()){
            case initialStep:
                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.QUESTION,
                        this.messagesProperties.getMessage("categories"),
                        buttons
                );
            case viewMenuStep:
                if (!User.allSubscriptions.contains(userInput.getMessage())) {
                    Output output = new Output(
                            new State(type, viewMenuStep),
                            ResponseType.CALLBACK_ANSWER,
                            this.messagesProperties.getMessage("you-choice-undefined-category") +": " + userInput.getMessage()
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
                            this.messagesProperties.getMessage("you-successfully-subscribed-to-category") +
                                    ": " +
                                    this.messagesProperties.getMessage(userInput.getMessage())
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
                        this.messagesProperties.getMessage("you-successfully-unsubscribed-from-category") +
                                ": " +
                                this.messagesProperties.getMessage(userInput.getMessage())
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
                this.messagesProperties.getMessage("categories"),
                buttons
        );
    }

    private List<InlineButton> generateButtons(List<String> userSubscriptions) {
        List<InlineButton> buttons = new ArrayList<>();
        for (String subscription: User.allSubscriptions) {
            if (userSubscriptions.contains(subscription)) {
                buttons.add(new InlineButton(this.messagesProperties.getMessage(subscription) + " \uD83D\uDD14", subscription));
            } else {
                buttons.add(new InlineButton(this.messagesProperties.getMessage(subscription), subscription));
            }
        }
        return buttons;
    }

    @Override
    public String getType() {
        return type;
    }
}
