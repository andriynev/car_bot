package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.telegram_bot.DriverHelperBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResponseService {
    private final String markdownV2MessageType = "MarkdownV2";
    DriverHelperBot driverHelperBot;
    private final Pattern telegramMarkdownForbiddenSymbolsPattern = Pattern.compile("([.+\\-!=>()#_*\\[\\]~`|{}]{1,1})");
    private final Pattern telegramMarkdownLinkPattern = Pattern.compile("\\[[\\p{L}\\s\\d]+\\]\\(((?:\\/|https?:\\/\\/)[\\w\\d./?=#]+)\\)");
    private final MessagesProperties messagesProperties;

    @Lazy
    @Autowired
    public ResponseService(DriverHelperBot driverHelperBot, MessagesProperties messagesProperties) {
        this.driverHelperBot = driverHelperBot;
        this.messagesProperties = messagesProperties;
    }

    public BotApiMethod<?> sendMessage(OutputMessage outputMessage) {

        BotApiMethod<?> replyToUser = null;

        switch (outputMessage.getType()) {
            case MENU:
            case QUESTION:
            case MESSAGE:
                replyToUser = sendMessageForm(outputMessage);
                break;
            case CALLBACK_ANSWER:
                replyToUser = sendAnswerCallbackQuery(outputMessage);
                break;
            default:
                break;
        }

        for (OutputMessage mess : outputMessage.getMessages()) {
            sendAsyncMessage(mess);
            if (mess.getMessages() != null) {
                for (OutputMessage m : mess.getMessages()) {
                    sendAsyncMessage(m);
                }
            }
        }
        
        return replyToUser;
    }

    private void sendAsyncMessage(OutputMessage message) {
        switch (message.getType()){
            case MENU:
            case MESSAGE:
            case QUESTION:
                sendSimpleMessage(message);
                return;
            case EDIT_BUTTONS:
                sendEditMessage(message);
                return;
            case CALLBACK_ANSWER:
                sendAnswerCallbackQuery(message);
                return;
            case LOCATION:
                sendLocation(message);
            case IMAGE:
                sendImage(message);
        }
    }

    public BotApiMethod<?> onWebhookUpdateReceived(@RequestBody Update update) {
        return driverHelperBot.onWebhookUpdateReceived(update);
    }

    public void sendNewsItem(NewsItem item, Long chatID) {
        SendPhoto sendPhoto = prepareNewsItem(item, chatID);
        driverHelperBot.sendPhoto(sendPhoto);
    }

    public void sendMessage(String message, Long chatId, MessageType messageType) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        if (messageType.equals(MessageType.MARKDOWN)) {
            sendMessage.enableMarkdownV2(true);
            message = prepareMarkdownMessage(message);
        }

        sendMessage.setText(message);
        driverHelperBot.sendMessage(sendMessage);
    }

    private SendPhoto prepareNewsItem(NewsItem item, Long chatID) {
        String title = telegramMarkdownForbiddenSymbolsPattern
                .matcher(item.getTitle())
                .replaceAll("\\\\$1");

        String text = telegramMarkdownForbiddenSymbolsPattern
                .matcher(item.getText())
                .replaceAll("\\\\$1");

        String caption = String.format("* %s *\n" + // bold title
                "\n" + // new line to separate title and text
                "%s\n" + // main text
                "\n" + // new line to separate text and category
                "%s: %s\n\n" +
                "\uD83D\uDC49 [%s](%s)",
                title,
                text,
                this.messagesProperties.getMessage("category"), this.messagesProperties.getMessage(item.getCategory()),
                this.messagesProperties.getMessage("details"), item.getOriginLink());

        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatID.toString())
                .photo(new InputFile(item.getPhotoUrl()))
                .caption(caption)
                .build();
        sendPhoto.setParseMode(markdownV2MessageType);
        return sendPhoto;
    }


    private SendMessage sendMessageForm(OutputMessage outputMessage) {
        SendMessage sendMessage;
        sendMessage = initMessage(outputMessage.getChatID(), outputMessage.getMessage(), outputMessage.getMessageType());
        if (outputMessage.getReplyButtons() != null && !outputMessage.getReplyButtons().isEmpty()) {
            ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboard(outputMessage.getReplyButtons());
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        if (outputMessage.getInlineButtons() != null && !outputMessage.getInlineButtons().isEmpty()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboard(outputMessage.getInlineButtons());
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        return sendMessage;
    }

    private void sendImage(OutputMessage outputMessage) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .photo(new InputFile(outputMessage.getPicture()))
                .chatId(outputMessage.getChatID().toString()).build();
        sendPhoto.disableNotification();

        driverHelperBot.sendPhoto(sendPhoto);
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(OutputMessage outputMessage) {
        return AnswerCallbackQuery.builder()
                .callbackQueryId(outputMessage.getCallbackQueryId())
                .text(outputMessage.getMessage())
                .showAlert(outputMessage.isShowAlert())
                .build();
    }

    private void sendEditMessage(OutputMessage outputMessage) {

        InlineKeyboardMarkup replyKeyboardMarkup = getInlineKeyboard(outputMessage.getInlineButtons());
        EditMessageReplyMarkup msg = EditMessageReplyMarkup.builder()
                .chatId(outputMessage.getChatID().toString())
                .messageId(outputMessage.getMessageId())
                .replyMarkup(replyKeyboardMarkup)
                .build();
        msg.setReplyMarkup(replyKeyboardMarkup);

        driverHelperBot.sendMessage(msg);
    }

    private void sendSimpleMessage(OutputMessage outputMessage) {
        SendMessage sendMessage = sendMessageForm(outputMessage);

        driverHelperBot.sendMessage(sendMessage);
    }

    private void sendLocation(OutputMessage outputMessage) {
        SendLocation sendLocation = new SendLocation(
                outputMessage.getChatID().toString(),
                outputMessage.getLatitude(),
                outputMessage.getLongitude()
        );

        driverHelperBot.sendMessage(sendLocation);
    }

    private void sendCallbackAnswer(OutputMessage outputMessage) {
        AnswerCallbackQuery answer = sendAnswerCallbackQuery(outputMessage);

        driverHelperBot.sendMessage(answer);
    }

    private ReplyKeyboardMarkup getReplyKeyboard(List<ReplyButton> replyButtons) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        for (ReplyButton button : replyButtons) {
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardButton btn = KeyboardButton.builder()
                    .text(button.getTitle())
                    .requestLocation(button.isRequestLocation())
                    .build();
            keyboardRow.add(btn);
            keyboard.add(keyboardRow);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<InlineButton> inlineButtons) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        for (InlineButton button : inlineButtons) {

            if (inlineButtons.indexOf(button) % 2 == 0 && !keyboardRow.isEmpty()) {
                keyboard.add(keyboardRow);
                keyboardRow = new ArrayList<>();
            }
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(button.getTitle());
            inlineKeyboardButton.setCallbackData(button.getData());
            keyboardRow.add(inlineKeyboardButton);
        }
        if (!keyboardRow.isEmpty()) {
            keyboard.add(keyboardRow);
        }
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    private SendMessage initMessage(Long chatId, String textMessage, MessageType messageType) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        if (messageType.equals(MessageType.MARKDOWN)) {
            sendMessage.enableMarkdownV2(true);
            textMessage = prepareMarkdownMessage(textMessage);
        }

        sendMessage.setText(textMessage);

        return sendMessage;
    }

    private String prepareMarkdownMessage(String message) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = telegramMarkdownLinkPattern.matcher(message);
        while (m.find()) {
            allMatches.add(m.group());
        }

        for (int i = 0; i < allMatches.size(); i++) {
            message = message.replace(allMatches.get(i), String.format("^link%d^", i));
        }

        message = telegramMarkdownForbiddenSymbolsPattern
                .matcher(message)
                .replaceAll("\\\\$1");

        for (int i = 0; i < allMatches.size(); i++) {
            message = message.replace(String.format("^link%d^", i), allMatches.get(i));
        }

        message = message.replace("^bold^", "*");
        message = message.replace("^italic^", "_");
        return message;
    }
}
