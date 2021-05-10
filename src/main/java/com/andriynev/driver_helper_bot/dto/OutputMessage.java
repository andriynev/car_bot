package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.ResponseType;

import java.util.List;

public class OutputMessage {
    private ResponseType type;
    private String message;
    private List<String> replyButtons;
    private List<InlineButton> inlineButtons;
    private String picture;
    private Long chatID;
    private OutputMessage editMessageReplyMarkup;

    // only for callback messages
    private String callbackQueryId;
    private Integer messageId;

    public OutputMessage(Output output, Long chatID, Integer messageId) {
        this.type = output.getType();
        this.message = output.getMessage();
        this.replyButtons = output.getReplyButtons();
        this.inlineButtons = output.getInlineButtons();
        this.picture = output.getPicture();
        this.chatID = chatID;
        this.messageId = messageId;
    }

    public OutputMessage(Output output, Long chatID, Integer messageId, String callbackQueryId) {
        this(output, chatID, messageId);
        this.callbackQueryId = callbackQueryId;
        if (output.getEditMessageReplyMarkup() != null) {
            this.editMessageReplyMarkup = new OutputMessage(
                    output.getEditMessageReplyMarkup(),
                    chatID,
                    messageId,
                    callbackQueryId
            );
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

    public OutputMessage getEditMessageReplyMarkup() {
        return editMessageReplyMarkup;
    }

    public void setEditMessageReplyMarkup(OutputMessage editMessageReplyMarkup) {
        this.editMessageReplyMarkup = editMessageReplyMarkup;
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
                ", editMessageReplyMarkup=" + editMessageReplyMarkup +
                ", callbackQueryId='" + callbackQueryId + '\'' +
                ", messageId=" + messageId +
                '}';
    }
}
