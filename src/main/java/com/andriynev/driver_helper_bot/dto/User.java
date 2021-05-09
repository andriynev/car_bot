package com.andriynev.driver_helper_bot.dto;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    public static final List<String> allSubscriptions = new ArrayList<>(Arrays.asList("news", "laws", "articles", "service"));

    @NotNull
    @Id
    private String id;
    @NotNull
    private Long chatID;
    @NotNull
    private State state;
    private List<String> subscriptions;

    public User() {
    }

    public User(String id, Long chatID, State state, List<String> subscriptions) {
        this.id = id;
        this.chatID = chatID;
        this.state = state;
        this.subscriptions = subscriptions;
    }

    public User(Long chatID, State state, List<String> subscriptions) {
        this.chatID = chatID;
        this.state = state;
        this.subscriptions = subscriptions;
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

    public List<String> getSubscriptions() {
        if (subscriptions == null) {
            return new ArrayList<>();
        }
        return subscriptions;
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", chatID=" + chatID +
                ", state=" + state +
                ", subscriptions=" + subscriptions +
                '}';
    }
}
