package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.ResponseType;

import java.util.List;

public class Output {
    private ResponseType type;
    private String message;
    private List<String> replyButtons;
    private List<InlineButton> inlineButtons;
    private String picture;
    private State state;
    private boolean redirect;
    private Output editMessageReplyMarkup;

    public Output(State state, ResponseType type, String message, List<String> replyButtons, String picture) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.replyButtons = replyButtons;
        this.picture = picture;
    }

    public Output(State state, ResponseType type, String message, List<InlineButton> inlineButtons, Output editMessageReplyMarkup) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.inlineButtons = inlineButtons;
        this.editMessageReplyMarkup = editMessageReplyMarkup;
    }

    public Output(State state, ResponseType type, String message) {
        this.state = state;
        this.type = type;
        this.message = message;
    }

    public Output(State state, ResponseType type, String message, Output editMessageReplyMarkup) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.editMessageReplyMarkup = editMessageReplyMarkup;
    }

    public Output(State state, ResponseType type, String message, List<InlineButton> inlineButtons) {
        this.state = state;
        this.type = type;
        this.message = message;
        this.inlineButtons = inlineButtons;
    }

    public Output(State state, ResponseType type, List<InlineButton> inlineButtons) {
        this.state = state;
        this.type = type;
        this.inlineButtons = inlineButtons;
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

    public List<String> getReplyButtons() {
        return replyButtons;
    }

    public void setReplyButtons(List<String> replyButtons) {
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

    public Output getEditMessageReplyMarkup() {
        return editMessageReplyMarkup;
    }

    public void setEditMessageReplyMarkup(Output editMessageReplyMarkup) {
        this.editMessageReplyMarkup = editMessageReplyMarkup;
    }

    @Override
    public String toString() {
        return "Output{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", replyButtons=" + replyButtons +
                ", inlineButtons=" + inlineButtons +
                ", picture='" + picture + '\'' +
                ", state=" + state +
                ", redirect=" + redirect +
                ", editMessageReplyMarkup=" + editMessageReplyMarkup +
                '}';
    }
}
