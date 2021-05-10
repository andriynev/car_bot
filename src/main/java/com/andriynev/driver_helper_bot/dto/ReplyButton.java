package com.andriynev.driver_helper_bot.dto;

public class ReplyButton {
    private String title;
    private boolean requestLocation;

    public ReplyButton(String title, boolean requestLocation) {
        this.title = title;
        this.requestLocation = requestLocation;
    }

    public ReplyButton(String title) {
        this.title = title;
        this.requestLocation = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequestLocation() {
        return requestLocation;
    }

    public void setRequestLocation(boolean requestLocation) {
        this.requestLocation = requestLocation;
    }

    @Override
    public String toString() {
        return "ReplyButton{" +
                "title='" + title + '\'' +
                ", requestLocation=" + requestLocation +
                '}';
    }
}
