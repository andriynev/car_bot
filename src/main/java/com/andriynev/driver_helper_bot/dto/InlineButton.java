package com.andriynev.driver_helper_bot.dto;

public class InlineButton {
    private String title;
    private String data;

    public InlineButton(String title, String data) {
        this.title = title;
        this.data = data;
    }

    public InlineButton(String title) {
        this.title = title;
        this.data = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "InlineButton{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
