package com.andriynev.driver_helper_bot.services;


import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.InputMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.Collections;


@Service
public class DispatcherService {
    private final UserService userService;
    private final RouterService routerService;
    private final ResponseService responseService;

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

        User user = identifyUser(inputMessage);
        State previousState = user.getState();
        Output out = routerService.route(user, inputMessage);
        user.setState(out.getState());
        user = userService.save(user);

        OutputMessage mess;
        if (inputMessage.getType().equals(InputMessageType.DIRECT)) {
            mess = new OutputMessage(out, user.getChatID(), inputMessage.getMessageId());
        } else {
            mess = new OutputMessage(
                    out,
                    user.getChatID(),
                    inputMessage.getMessageId(),
                    inputMessage.getCallbackId()
            );
        }


        if (!out.isRedirect()) {
            return responseService.sendMessage(mess);
        }

        State currentState = user.getState();
        boolean isNeedToChangeMenu = !currentState.getType().equals(previousState.getType());

        Output outSecondary = routerService.route(user, new InputMessage(inputMessage));
        OutputMessage messSecondary = new OutputMessage(outSecondary, user.getChatID(), inputMessage.getMessageId());
        user.setState(outSecondary.getState());
        userService.save(user);
        if (isNeedToChangeMenu) {
            ArrayList<ReplyButton> button = new ArrayList<>();
            button.add(new ReplyButton("Main menu"));
            mess.setReplyButtons(button);
        }

        messSecondary.addMessage(mess);
        return responseService.sendMessage(messSecondary);

    }

    private InputMessage getCallBackMessage(CallbackQuery callbackQuery) {
        return new InputMessage(
                InputMessageType.CALLBACK,
                callbackQuery.getData(),
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                new FromUser(callbackQuery.getFrom().getFirstName(), callbackQuery.getFrom().getLastName(), callbackQuery.getFrom().getUserName()),
                callbackQuery.getId()
        );
    }

    private InputMessage getDirectMessage(Message message) {
        Location location = message.hasLocation()
                ? new Location(message.getLocation().getLongitude(), message.getLocation().getLatitude())
                : null;

        String text = message.hasText() ? message.getText() : "";
        return new InputMessage(
                InputMessageType.DIRECT,
                text,
                message.getChatId(),
                message.getMessageId(),
                new FromUser(message.getFrom().getFirstName(), message.getFrom().getLastName(), message.getFrom().getUserName()),
                location
        );
    }

    private User identifyUser(InputMessage inputMessage) {
        return userService.getOrCreateUser(
                inputMessage.getChatID(),
                inputMessage.getFromUser().getFirstName(),
                inputMessage.getFromUser().getLastName(),
                inputMessage.getFromUser().getUserName()
        );
    }

    private boolean hasCallBack(Update update) {
        return update.hasCallbackQuery();
    }

    public BotApiMethod<?> onWebhookUpdateReceived(@RequestBody Update update) {
        return responseService.onWebhookUpdateReceived(update);
    }
}
