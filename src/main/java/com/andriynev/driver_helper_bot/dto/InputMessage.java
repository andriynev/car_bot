package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.InputMessageType;

public class InputMessage {
    private final InputMessageType type;
    private final String message;
    private final Long chatID;

    // only for callback messages
    private final String callbackId;

    public InputMessage(InputMessageType type, String message, Long chatID) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
        this.callbackId = "";
    }

    public InputMessage(InputMessageType type, String message, Long chatID, String callbackId) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
        this.callbackId = callbackId;
    }

    public InputMessage() {
        type = InputMessageType.DIRECT;
        message = "";
        chatID = 0L;
        this.callbackId = "";
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
}
