package com.andriynev.driver_helper_bot.dto;


public class UserUpdateRequest {

    private boolean enabled;

    public UserUpdateRequest() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserUpdateRequest{" +
                "enabled=" + enabled +
                '}';
    }
}
