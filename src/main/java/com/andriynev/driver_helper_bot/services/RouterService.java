package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
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
    private final MessagesProperties messagesProperties;


    @Autowired
    public RouterService(Handler expertService, Handler placesService, Handler subscriptionsService, GroupHandler mainMenuService, MessagesProperties messagesProperties) {
        handlers.put(expertService.getType(), expertService);
        handlers.put(placesService.getType(), placesService);
        handlers.put(subscriptionsService.getType(), subscriptionsService);

        List<Handler> group = new ArrayList<>(Arrays.asList(expertService, placesService, subscriptionsService));
        mainMenuService.setHandlers(group);
        handlers.put(mainMenuService.getType(), mainMenuService);
        this.messagesProperties = messagesProperties;
    }

    public Output route(User user, InputMessage inputMessage) {
        String mainMenuLocalized = this.messagesProperties.getMessage("main-menu");
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
                if (message.equals(mainMenuLocalized)) {
                    newState = UserService.initialState;
                    break;
                }
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
