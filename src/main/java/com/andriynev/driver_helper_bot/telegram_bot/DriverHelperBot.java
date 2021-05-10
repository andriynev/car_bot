package com.andriynev.driver_helper_bot.telegram_bot;

import com.andriynev.driver_helper_bot.services.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;


public class DriverHelperBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private final DispatcherService dispatcherService;

    @Autowired
    public DriverHelperBot(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    private boolean isValidWebHookUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            // validate callback params
            if (update.getCallbackQuery().getId() == null) {
                return false;
            }

            if (update.getCallbackQuery().getMessage() == null) {
                return false;
            }

            if (update.getCallbackQuery().getData() == null) {
                return false;
            }

            if (update.getCallbackQuery().getMessage().getMessageId() == null) {
                return false;
            }

            if (update.getCallbackQuery().getMessage().getFrom() == null) {
                return false;
            }

            if (update.getCallbackQuery().getMessage().getChat() == null) {
                return false;
            }

            return true;
        }

        // validate direct message params
        if (!update.hasMessage()) {
            return false;
        }

        if (update.getMessage().getMessageId() == null) {
            return false;
        }

        if (update.getMessage().getFrom() == null) {
            return false;
        }

        if (update.getMessage().getChat() == null) {
            return false;
        }

        if (!update.getMessage().hasText() || !update.getMessage().hasLocation()) {
            return false;
        }

        return true;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println(update);
        if (!isValidWebHookUpdate(update)) {
            return null;
        }

        return dispatcherService.handleUpdate(update);
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(SendPhoto message) {
        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(EditMessageReplyMarkup message) {
        sendTelegramMessage(message);
    }

    private void sendTelegramMessage(BotApiMethod<Serializable> message) {
        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
