package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.handlers.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouterService {
    private Map<String, Handler> handlers = new HashMap<>();

    @Autowired
    public RouterService(Handler expertService, Handler mainMenuService, Handler stoService) {
        handlers.put(expertService.getType(), expertService);
        handlers.put(mainMenuService.getType(), mainMenuService);
        handlers.put(stoService.getType(), stoService);

    }

    public Output route(User user, InputMessage inputMessage) {
        String message = inputMessage.getMessage();
        State newState;
        switch (message) {
            case "/start":
            case "MainMenu":
                newState = UserService.initialState;
                break;

            default:
                newState = user.getState();
                break;
        }

        Handler handler = getHandlerByStateType(newState.getType());
        return handler.handle(newState, inputMessage);

    }

    private Handler getHandlerByStateType(String stateType) {
        return handlers.get(stateType);
    }

}
