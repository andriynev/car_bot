package com.andriynev.driver_helper_bot.services;


import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.Output;
import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
        User user = identifyUser(inputMessage.getChatID());
        Output out = routerService.route(user, inputMessage);
        user.setState(out.getState());
        user = userService.save(user);
        OutputMessage mess = new OutputMessage(out, user.getChatID());

        if (!out.isRedirect()) {
            return responseService.sendMessage(mess);
        }

        Output outSecondary = routerService.route(user, new InputMessage());
        OutputMessage messSecondary = new OutputMessage(outSecondary, user.getChatID());
        return responseService.sendMessages(mess, messSecondary);

    }

    private InputMessage getCallBackMessage(CallbackQuery callbackQuery) {
        return new InputMessage("callback", callbackQuery.getData(), callbackQuery.getMessage().getChatId());
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

    public BotApiMethod<?> onWebhookUpdateReceived(@RequestBody Update update) {
        return responseService.onWebhookUpdateReceived(update);
    }
}
