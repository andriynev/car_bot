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

    private PlacesRequest placesRequest;

    private String firstName;
    private String lastName;
    private String userName;

    public User() {
    }

    public User(Long chatID, State state, List<String> subscriptions) {
        this.chatID = chatID;
        this.state = state;
        this.subscriptions = subscriptions;
    }

    public User(Long chatID, State state, List<String> subscriptions, String firstName, String lastName, String userName) {
        this(chatID, state, subscriptions);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
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

    public String getFirstName() {
        if (firstName == null) {
            return "";
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if (lastName == null) {
            return "";
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        if (userName == null) {
            return "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public PlacesRequest getPlacesRequest() {
        if (placesRequest == null) {
            return new PlacesRequest();
        }
        return placesRequest;
    }

    public void setPlacesRequest(PlacesRequest placesRequest) {
        this.placesRequest = placesRequest;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", chatID=" + chatID +
                ", state=" + state +
                ", subscriptions=" + subscriptions +
                ", placesRequest=" + placesRequest +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
