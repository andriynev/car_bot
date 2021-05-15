package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.enums.ResponseType;

import java.util.ArrayList;
import java.util.List;

public class Output {
    private ResponseType type;
    private List<ReplyButton> replyButtons;
    private List<InlineButton> inlineButtons;
    private String picture;
    private State state;
    private boolean redirect;
    private MessageType messageType;
    private String message;
    private double longitude;
    private double latitude;
    private boolean showAlert;

    private List<Output> messages;

    public Output(State state, ResponseType type, String message, List<ReplyButton> replyButtons, String picture) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.replyButtons = replyButtons;
        this.picture = picture;
        this.messageType = MessageType.PLAIN;
    }

    public Output(State state, ResponseType type, String message) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.messageType = MessageType.PLAIN;
    }

    public Output(State state, ResponseType type, String message, List<InlineButton> inlineButtons) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.inlineButtons = inlineButtons;
        this.messageType = MessageType.PLAIN;
    }

    public Output(State state, ResponseType type, List<InlineButton> inlineButtons) {
        this.state = state;
        this.type = type;
        this.inlineButtons = inlineButtons;
        this.messageType = MessageType.PLAIN;
    }

    public Output(State state, double latitude, double longitude) {
        this.state = state;
        this.type = ResponseType.LOCATION;
        this.messageType = MessageType.PLAIN;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public List<ReplyButton> getReplyButtons() {
        return replyButtons;
    }

    public void setReplyButtons(List<ReplyButton> replyButtons) {
        this.replyButtons = replyButtons;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<InlineButton> getInlineButtons() {
        return inlineButtons;
    }

    public void setInlineButtons(List<InlineButton> inlineButtons) {
        this.inlineButtons = inlineButtons;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public List<Output> getMessages() {
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages;
    }

    public void setMessages(List<Output> messages) {
        this.messages = messages;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isShowAlert() {
        return showAlert;
    }

    public void setShowAlert(boolean showAlert) {
        this.showAlert = showAlert;
    }

    @Override
    public String toString() {
        return "Output{" +
                "type=" + type +
                ", replyButtons=" + replyButtons +
                ", inlineButtons=" + inlineButtons +
                ", picture='" + picture + '\'' +
                ", state=" + state +
                ", redirect=" + redirect +
                ", messageType=" + messageType +
                ", message='" + message + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", messages=" + messages +
                '}';
    }
}
