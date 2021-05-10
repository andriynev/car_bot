package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.InputMessageType;

public class InputMessage {
    private final InputMessageType type;
    private final String message;
    private final Long chatID;
    private final Integer messageId;
    private final FromUser fromUser;

    // only for callback messages
    private String callbackId;

    public InputMessage(InputMessageType type, String message, Long chatID, Integer messageId, FromUser from) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
        this.callbackId = "";
        this.messageId = messageId;
        this.fromUser = from;
    }

    public InputMessage(InputMessageType type, String message, Long chatID, Integer messageId, FromUser from, String callbackId) {
        this(type, message, chatID, messageId, from);
        this.callbackId = callbackId;
    }

    public InputMessage(InputMessage inputMessage) {
        type = inputMessage.type;
        message = "";
        chatID = inputMessage.chatID;
        this.callbackId = inputMessage.callbackId;
        this.messageId = inputMessage.messageId;
        if (inputMessage.fromUser != null) {
            fromUser = inputMessage.fromUser;
        } else {
            this.fromUser = new FromUser();
        }
    }

    public InputMessage() {
        type = InputMessageType.DIRECT;
        message = "";
        chatID = 0L;
        this.callbackId = "";
        this.messageId = 0;
        this.fromUser = new FromUser();
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

    public Integer getMessageId() {
        return messageId;
    }

    public FromUser getFromUser() {
        return fromUser;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    @Override
    public String toString() {
        return "InputMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", chatID=" + chatID +
                ", messageId=" + messageId +
                ", fromUser=" + fromUser +
                ", callbackId='" + callbackId + '\'' +
                '}';
    }
}
