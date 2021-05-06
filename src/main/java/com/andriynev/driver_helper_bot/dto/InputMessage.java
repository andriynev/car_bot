package com.andriynev.driver_helper_bot.dto;

public class InputMessage {
    private String type;
    private String message;
    private Long chatID;

    public InputMessage(String type, String message, Long chatID) {
        this.type = type;
        this.message = message;
        this.chatID = chatID;
    }
}
