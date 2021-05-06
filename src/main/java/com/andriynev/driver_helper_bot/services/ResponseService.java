package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.OutputMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ResponseService {
    public BotApiMethod<?> sendMassage(OutputMessage outputMessage) {

        BotApiMethod<?> replyToUser = null;

        switch (outputMessage.getType()){
            case MENU:
                replyToUser = sendMessageForm(outputMessage);
                break;

            default:
                break;
        }
        return replyToUser;
    }


    private SendMessage sendMessageForm (OutputMessage outputMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMenuKeyboard(outputMessage.getButtons());
        return createMessageWithKeyboard(outputMessage.getChatID(), outputMessage.getMessage(), replyKeyboardMarkup);
    }


    private ReplyKeyboardMarkup getMenuKeyboard(List<String> buttons) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        IntStream.range(0, buttons.size()).forEach(i -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(buttons.get(i)));
            keyboard.add(keyboardRow);
        });

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final Long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }



}
