package com.andriynev.driver_helper_bot.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class User {
    @NotNull
    private String id;
    @NotNull
    private String chatID;
    @NotNull
    private State state;
    private List<String> subscription;

    public User(String id, String chatID, State state, List<String> subscription) {
        this.id = id;
        this.chatID = chatID;
        this.state = state;
        this.subscription = subscription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<String> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<String> subscription) {
        this.subscription = subscription;
    }
}
