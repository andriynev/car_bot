package com.andriynev.driver_helper_bot.services;


import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class DispatcherService {
    private UserService userService;
    private RouterService routerService;
    private ResponseService responseService;

    @Autowired
    public DispatcherService(UserService userService, RouterService routerService, ResponseService responseService) {
        this.userService = userService;
        this.routerService = routerService;
        this.responseService = responseService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        InputMessage inputMessage;
        if (hasCallBack(update)){
            inputMessage =  getCallBackMessage(update.getCallbackQuery());
        } else {
            inputMessage = getDirectMessage(update.getMessage());
        }
        User user = identifyUser(update.getMessage().getChatId());

        return responseService.sendMassage(routerService.route(user, inputMessage));

    }

    private InputMessage getCallBackMessage(CallbackQuery callbackQuery) {
        return new InputMessage("callback", callbackQuery.getMessage().getText(), callbackQuery.getMessage().getChatId());
    }

    private InputMessage getDirectMessage(Message message) {
        return new InputMessage("direct", message.getText(), message.getChatId());
    }

    private User identifyUser(Long chatID) {
        return userService.getOrCreateUser(chatID);
    }

    private boolean hasCallBack(Update update) {
        return update.hasCallbackQuery();
    }
}
