package com.andriynev.driver_helper_bot.dto;


import java.util.List;

public class ModeratorUpdateRequest {

    private boolean enabled;
    private List<String> permissions;

    public ModeratorUpdateRequest() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "ModeratorUpdateRequest{" +
                "enabled=" + enabled +
                ", permissions=" + permissions +
                '}';
    }
}
