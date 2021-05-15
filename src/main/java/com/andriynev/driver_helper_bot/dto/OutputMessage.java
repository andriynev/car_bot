package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.enums.ResponseType;

import java.util.ArrayList;
import java.util.List;

public class OutputMessage {
    private ResponseType type;
    private String message;
    private List<ReplyButton> replyButtons;
    private List<InlineButton> inlineButtons;
    private String picture;
    private Long chatID;
    private MessageType messageType;
    private List<OutputMessage> messages;

    // only for callback messages
    private String callbackQueryId;
    private Integer messageId;
    private boolean showAlert;

    // only for location messages
    private double longitude;
    private double latitude;

    private void init(Output output, Long chatID, Integer messageId) {
        this.type = output.getType();
        this.message = output.getMessage();
        this.replyButtons = output.getReplyButtons();
        this.inlineButtons = output.getInlineButtons();
        this.picture = output.getPicture();
        this.chatID = chatID;
        this.messageId = messageId;
        this.messageType = output.getMessageType();
        this.longitude = output.getLongitude();
        this.latitude = output.getLatitude();
        this.showAlert = output.isShowAlert();
    }

    public OutputMessage(Output output, Long chatID, Integer messageId) {
        init(output, chatID, messageId);
        this.messages = new ArrayList<>();
        for (Output out : output.getMessages()) {
            this.messages.add(new OutputMessage(out, chatID, messageId));
        }
    }

    public OutputMessage(Output output, Long chatID, Integer messageId, String callbackQueryId) {
        init(output, chatID, messageId);
        this.callbackQueryId = callbackQueryId;

        this.messages = new ArrayList<>();
        for (Output out : output.getMessages()) {
            this.messages.add(new OutputMessage(out, chatID, messageId, callbackQueryId));
        }
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

    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public List<InlineButton> getInlineButtons() {
        return inlineButtons;
    }

    public void setInlineButtons(List<InlineButton> inlineButtons) {
        this.inlineButtons = inlineButtons;
    }

    public String getCallbackQueryId() {
        return callbackQueryId;
    }

    public void setCallbackQueryId(String callbackQueryId) {
        this.callbackQueryId = callbackQueryId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public List<OutputMessage> getMessages() {
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages;
    }

    public void setMessages(List<OutputMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(OutputMessage message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }

        this.messages.add(message);
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
        return "OutputMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", replyButtons=" + replyButtons +
                ", inlineButtons=" + inlineButtons +
                ", picture='" + picture + '\'' +
                ", chatID=" + chatID +
                ", messageType=" + messageType +
                ", messages=" + messages +
                ", callbackQueryId='" + callbackQueryId + '\'' +
                ", messageId=" + messageId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
