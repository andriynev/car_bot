package com.andriynev.driver_helper_bot.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SendMessageRequest {

    @NotBlank
    @Size(min = 3, max = 4000)
    private String message;

    private String userId;

    public SendMessageRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "message='" + message + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
