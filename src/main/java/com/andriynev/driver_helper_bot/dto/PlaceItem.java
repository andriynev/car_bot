package com.andriynev.driver_helper_bot.dto;

public class PlaceItem {
    private String name;
    private String address;
    private String distance;
    private String duration;
    private String rating;
    private Location location;
    private boolean openNow;

    public PlaceItem(String name, String address, String distance, String duration, String rating, Location location, boolean openNow) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.duration = duration;
        this.rating = rating;
        this.location = location;
        this.openNow = openNow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public PlaceData getPlaceData() {
        return new PlaceData(name, location.getLongitude(), location.getLatitude());
    }

    @Override
    public String toString() {
        return "PlaceItem{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                ", duration='" + duration + '\'' +
                ", rating='" + rating + '\'' +
                ", location=" + location +
                ", openNow=" + openNow +
                '}';
    }
}
