package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.InputMessageType;

public class InputMessage {
    private final InputMessageType type;
    private final String message;
    private final Long chatID;

    // only for callback messages
    private final String inlineMessageId;

    // only for callback messages
    private final String callbackId;

    public InputMessage(InputMessageType type, String message, Long chatID) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
        this.callbackId = "";
        this.inlineMessageId = "";
    }

    public InputMessage(InputMessageType type, String message, Long chatID, String callbackId, String inlineMessageId) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
        this.callbackId = callbackId;
        this.inlineMessageId = inlineMessageId;
    }

    public InputMessage(InputMessage inputMessage) {
        type = inputMessage.type;
        message = "";
        chatID = inputMessage.chatID;
        this.callbackId = inputMessage.callbackId;
        this.inlineMessageId = inputMessage.inlineMessageId;
    }

    public InputMessage() {
        type = InputMessageType.DIRECT;
        message = "";
        chatID = 0L;
        this.callbackId = "";
        this.inlineMessageId = "";
    }

    public InputMessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Long getChatID() {
        return chatID;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public String getInlineMessageId() {
        return inlineMessageId;
    }

    @Override
    public String toString() {
        return "InputMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", chatID=" + chatID +
                ", callbackId='" + callbackId + '\'' +
                '}';
    }
}
