package com.andriynev.driver_helper_bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class JwtAuthRequest {

    @JsonProperty("auth_date")
    @NotBlank
    private int authDate;

    @NotBlank
    @JsonProperty("first_name")
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank
    @JsonProperty("hash")
    @Size(min = 64, max = 64)
    private String hash;

    @NotBlank
    @JsonProperty("id")
    private Long id;

    @JsonProperty("last_name")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank
    @JsonProperty("username")
    @Size(min = 3, max = 50)
    private String username;

    public JwtAuthRequest() {
    }


    public int getAuthDate() {
        return authDate;
    }

    @JsonProperty("auth_date")
    public void setAuthDate(int authDate) {
        this.authDate = authDate;
    }

    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHash() {
        return hash;
    }

    @JsonProperty("hash")
    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }
}
