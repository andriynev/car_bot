package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.Output;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionsService implements Handler {
    private final static String type = "Subscriptions";
    private final UserService userService;

    @Autowired
    public SubscriptionsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Output handle(State state, InputMessage userInput) {
        User user = userService.getOrCreateUser(userInput.getChatID());
        if (user.getSubscriptions().size() == 0) {
            return new Output(new State(type, "final"), ResponseType.MENU, "[Empty set]");
        }

        return new Output(new State(type, "final"), ResponseType.MENU, user.getSubscriptions().toString());
    }

    @Override
    public String getType() {
        return type;
    }
}
