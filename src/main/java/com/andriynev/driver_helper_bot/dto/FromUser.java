package com.andriynev.driver_helper_bot.dto;


import javax.validation.constraints.NotNull;


public class FromUser {

    @NotNull
    private String firstName;
    private String lastName;
    private String userName;

    public FromUser() {
        this.firstName = "";
        this.lastName = "";
        this.userName = "";
    }

    public FromUser(String firstName, String lastName, String userName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "FromUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
