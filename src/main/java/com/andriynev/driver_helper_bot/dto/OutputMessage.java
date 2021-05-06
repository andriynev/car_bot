package com.andriynev.driver_helper_bot.dto;

public class OutputMessage {
    private String text;

    public OutputMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
