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

    public OutputMessage(ResponseType type, String message, List<String> replyButtons, String picture, Long chatID) {
        this.type = type;
        this.message = message;
        this.replyButtons = replyButtons;
        this.picture = picture;
        this.chatID = chatID;
    }

    public OutputMessage(ResponseType type, String message, List<String> replyButtons, List<String> inlineButtons, String picture, Long chatID) {
        this.type = type;
        this.message = message;
        this.replyButtons = replyButtons;
        this.inlineButtons = inlineButtons;
        this.picture = picture;
        this.chatID = chatID;
    }

    public OutputMessage(ResponseType type, String message, Long chatID) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
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
}
