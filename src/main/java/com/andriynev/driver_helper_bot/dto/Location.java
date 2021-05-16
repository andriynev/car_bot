package com.andriynev.driver_helper_bot.dto;


import javax.validation.constraints.NotNull;


public class Location {

    @NotNull
    private double longitude;

    @NotNull
    private double latitude;

    public Location() {
        this.longitude = 0.0;
        this.latitude = 0.0;
    }

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
