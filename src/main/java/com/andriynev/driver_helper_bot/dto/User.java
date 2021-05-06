package com.andriynev.driver_helper_bot.dto;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;

public class User {
    @NotNull
    @Id
    private String id;
    @NotNull
    private Long chatID;
    @NotNull
    private State state;
    private List<String> subscription;

    public User() {
    }

    public User(String id, Long chatID, State state, List<String> subscription) {
        this.id = id;
        this.chatID = chatID;
        this.state = state;
        this.subscription = subscription;
    }

    public User(Long chatID, State state, List<String> subscription) {
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

    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatID) {
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

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, chatID='%d', state={type='%s', step='%s'}]",
                id, chatID, state.getType(), state.getStep());
    }
}
