package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.ResponseType;

import java.util.List;

public class OutputMessage {
    private ResponseType type;
    private String message;
    private List<String> replyButtons;
    private List<String> inlineButtons;
    private String picture;
    private Long chatID;

    // only for callback messages
    private String callbackQueryId;

    public OutputMessage(Output output, Long chatID) {
        this.type = output.getType();
        this.message = output.getMessage();
        this.replyButtons = output.getReplyButtons();
        this.inlineButtons = output.getInlineButtons();
        this.picture = output.getPicture();
        this.chatID = chatID;
    }

    public OutputMessage(Output output, Long chatID, String callbackQueryId) {
        this.type = output.getType();
        this.message = output.getMessage();
        this.replyButtons = output.getReplyButtons();
        this.inlineButtons = output.getInlineButtons();
        this.picture = output.getPicture();
        this.chatID = chatID;
        this.callbackQueryId = callbackQueryId;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getReplyButtons() {
        return replyButtons;
    }

    public void setReplyButtons(List<String> replyButtons) {
        this.replyButtons = replyButtons;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public List<String> getInlineButtons() {
        return inlineButtons;
    }

    public void setInlineButtons(List<String> inlineButtons) {
        this.inlineButtons = inlineButtons;
    }

    public String getCallbackQueryId() {
        return callbackQueryId;
    }

    public void setCallbackQueryId(String callbackQueryId) {
        this.callbackQueryId = callbackQueryId;
    }

    @Override
    public String toString() {
        return "OutputMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", replyButtons=" + replyButtons +
                ", inlineButtons=" + inlineButtons +
                ", picture='" + picture + '\'' +
                ", chatID=" + chatID +
                ", callbackQueryId='" + callbackQueryId + '\'' +
                '}';
    }
}
