package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import com.andriynev.driver_helper_bot.handlers.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouterService {
    UserRepository userRepository;
    private Map<String, Handler> handlers = new HashMap<>();

    @Autowired
    public RouterService(Handler expertService, Handler mainMenuService, Handler stoService, UserRepository userRepository) {
        handlers.put(expertService.getType(), expertService);
        handlers.put(mainMenuService.getType(), mainMenuService);
        handlers.put(stoService.getType(), stoService);
        this.userRepository = userRepository;

    }

    public OutputMessage route(User user, InputMessage inputMessage) {
        String message = inputMessage.getMessage();
        State newState;
        switch (message) {
            case "/start":
                user.setState(UserService.initialState);
                break;
            case "Expert":
                newState = new State("Expert", "initial");
                user.setState(newState);
                break;
            case "Sto":
                newState = new State("Sto", "initial");
                user.setState(newState);
                break;
            case "Tutorial":
                //newState = new State("Tutorial", "initial");
                //user.setState(newState);
                break;
            default:
                break;
        }

        userRepository.save(user);

        Handler handler = getHandlerByStateType(user.getState().getType());

        return handler.handle(user.getState(), inputMessage);

    }

    private Handler getHandlerByStateType(String stateType) {
        return handlers.get(stateType + "Service");
    }

}
