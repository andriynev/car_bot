package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.glassfish.jersey.server.Uri;

import java.util.List;

public class OutputMessage {
    private ResponseType type;
    private String message;
    private List<String> buttons;
    private String picture;
    private Long chatID;

    public OutputMessage(ResponseType type, String message, List<String> buttons, String picture, Long chatID) {
        this.type = type;
        this.message = message;
        this.buttons = buttons;
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

    public List<String> getButtons() {
        return buttons;
    }

    public void setButtons(List<String> buttons) {
        this.buttons = buttons;
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
}
