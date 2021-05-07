package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.handlers.GroupHandler;
import com.andriynev.driver_helper_bot.handlers.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.*;

@Service
public class RouterService {
    private final Map<String, Handler> handlers = new HashMap<>();

    @Autowired
    public RouterService(Handler expertService, GroupHandler mainMenuService, Handler stoService) {
        handlers.put(expertService.getType(), expertService);
        handlers.put(stoService.getType(), stoService);

        List<Handler> group = new ArrayList<>(Arrays.asList(expertService, stoService));
        mainMenuService.setHandlers(group);
        handlers.put(mainMenuService.getType(), mainMenuService);

    }

    public Output route(User user, InputMessage inputMessage) {
        String message = inputMessage.getMessage();
        State newState;
        switch (message) {
            case "/start":
            case "/main_menu":
            case "Main menu":
                newState = UserService.initialState;
                break;

            default:
                newState = user.getState();
                if (!handlers.containsKey(newState.getType())) {
                    newState = UserService.initialState;
                }
                break;
        }

        Handler handler = getHandlerByStateType(newState.getType());
        return handler.handle(newState, inputMessage);

    }

    private Handler getHandlerByStateType(String stateType) {
        return handlers.get(stateType);
    }

}