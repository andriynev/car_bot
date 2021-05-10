package com.andriynev.driver_helper_bot.dto;


import javax.validation.constraints.NotNull;


public class Location {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    public Location() {
        this.longitude = 0.0;
        this.latitude = 0.0;
    }

    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
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
