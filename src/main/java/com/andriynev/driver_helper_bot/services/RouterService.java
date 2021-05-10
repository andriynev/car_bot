package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.handlers.GroupHandler;
import com.andriynev.driver_helper_bot.handlers.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouterService {
    private final Map<String, Handler> handlers = new HashMap<>();

    @Autowired
    public RouterService(Handler expertService, Handler stoService, Handler subscriptionsService, GroupHandler mainMenuService) {
        handlers.put(expertService.getType(), expertService);
        handlers.put(stoService.getType(), stoService);
        handlers.put(subscriptionsService.getType(), subscriptionsService);

        List<Handler> group = new ArrayList<>(Arrays.asList(expertService, stoService, subscriptionsService));
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
            case "/state":
                return new Output(
                        user.getState(),
                        ResponseType.QUESTION,
                        user.getState().toString()
                );

            default:
                newState = user.getState();
                if (!handlers.containsKey(newState.getType())) {
                    newState = UserService.initialState;
                }
                break;
        }

        Handler handler = getHandlerByStateType(newState.getType());
        return handler.handle(user, newState, inputMessage);

    }

    private Handler getHandlerByStateType(String stateType) {
        return handlers.get(stateType);
    }

}
