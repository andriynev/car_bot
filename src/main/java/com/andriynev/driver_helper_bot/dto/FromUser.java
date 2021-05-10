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
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.userName = userName != null ? userName : "";
    }

    public String getFirstName() {
        if (firstName == null) {
            return "";
        }
        return firstName;
    }

    public String getLastName() {
        if (lastName == null) {
            return "";
        }
        return lastName;
    }

    public String getUserName() {
        if (userName == null) {
            return "";
        }
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
