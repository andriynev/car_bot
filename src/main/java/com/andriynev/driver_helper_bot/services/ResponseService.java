package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.telegram_bot.DriverHelperBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseService {
    DriverHelperBot driverHelperBot;

    @Lazy
    @Autowired
    public ResponseService(DriverHelperBot driverHelperBot) {
        this.driverHelperBot = driverHelperBot;
    }

    public BotApiMethod<?> sendMessage(OutputMessage outputMessage) {

        BotApiMethod<?> replyToUser = null;

        switch (outputMessage.getType()) {
            case MENU:
                replyToUser = sendMessageForm(outputMessage);
                break;
            case QUESTION:
                sendMenu(outputMessage);
                replyToUser = sendMessageForm(outputMessage);
                break;
            default:
                break;
        }
        return replyToUser;
    }

    public BotApiMethod<?> sendMessages(OutputMessage finalMsg, OutputMessage secondaryMsg) {
        sendFinalMessage(finalMsg);

        sendMenu(secondaryMsg);
        return sendMessageForm(secondaryMsg);
    }

    public BotApiMethod<?> onWebhookUpdateReceived(@RequestBody Update update) {
        return driverHelperBot.onWebhookUpdateReceived(update);
    }


    private SendMessage sendMessageForm(OutputMessage outputMessage) {
        SendMessage sendMessage;
        sendMessage = initMessage(outputMessage.getChatID(), outputMessage.getMessage());
        if (outputMessage.getReplyButtons() != null) {
            ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboard(outputMessage.getReplyButtons());
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        if (outputMessage.getInlineButtons() != null) {
            InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboard(outputMessage.getInlineButtons());
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        return sendMessage;
    }

    private void sendMenu(OutputMessage outputMessage) {
        SendMessage sendMessage = initMessage(outputMessage.getChatID(), "");
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboard(outputMessage.getReplyButtons());
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        driverHelperBot.sendMessage(sendMessage);
    }

    private void sendFinalMessage(OutputMessage outputMessage) {
        SendMessage sendMessage = initMessage(outputMessage.getChatID(), outputMessage.getMessage());

        driverHelperBot.sendMessage(sendMessage);
    }


    private ReplyKeyboardMarkup getReplyKeyboard(List<String> replyButtons) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        for (String button : replyButtons) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(button));
            keyboard.add(keyboardRow);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<String> inlineButtons) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        for (String button : inlineButtons) {

            if (inlineButtons.indexOf(button) % 2 == 0 && !keyboardRow.isEmpty()) {
                keyboard.add(keyboardRow);
                keyboardRow = new ArrayList<>();
            }
            keyboardRow.add(new InlineKeyboardButton(button));
        }
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final Long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = initMessage(chatId, textMessage);

        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        return sendMessage;
    }

    private SendMessage createMessageWithKeyboard(Long chatId, String textMessage) {
        SendMessage sendMessage = initMessage(chatId, textMessage);
        return sendMessage;
    }

    private SendMessage createMessageWithKeyboard(Long chatId,
                                                  String textMessage,
                                                  InlineKeyboardMarkup inlineKeyboardMarkup) {

        SendMessage sendMessage = initMessage(chatId, textMessage);

        if (inlineKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        return sendMessage;
    }
    private SendMessage initMessage (Long chatId, String textMessage) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(textMessage);

        return sendMessage;
    }


}
