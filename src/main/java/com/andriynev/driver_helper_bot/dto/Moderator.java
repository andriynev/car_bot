package com.andriynev.driver_helper_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Moderator implements UserDetails {
    @NotNull
    @Id
    private String id;
    @NotNull
    private Long telegramID;
    @NotNull
    private boolean enabled;
    private List<String> permissions;


    private String firstName;
    private String lastName;
    private String userName;

    public Moderator() {
    }

    public Moderator(Long telegramID, String firstName, String lastName, String userName) {
        this.telegramID = telegramID;
        this.enabled = false;
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

    @JsonIgnore
    public Long getTelegramID() {
        return telegramID;
    }

    public void setTelegramID(Long telegramID) {
        this.telegramID = telegramID;
    }

    public List<String> getPermissions() {
        if (permissions == null) {
            return new ArrayList<>();
        }
        return permissions;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Moderator{" +
                "id='" + id + '\'' +
                ", telegramID=" + telegramID +
                ", disabled=" + enabled +
                ", permissions=" + permissions +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : this.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
