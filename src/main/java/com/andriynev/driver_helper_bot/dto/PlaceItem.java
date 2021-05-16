package com.andriynev.driver_helper_bot.dto;

public class PlaceItem {
    private String name;
    private String address;
    private long distance;
    private String distanceHumanReadable;
    private long duration;
    private String durationHumanReadable;
    private String rating;
    private Location location;
    private boolean openNow;


    public PlaceItem(String name, String address, long distance, String distanceHumanReadable, long duration, String durationHumanReadable, String rating, Location location, boolean openNow) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.distanceHumanReadable = distanceHumanReadable;
        this.duration = duration;
        this.durationHumanReadable = durationHumanReadable;
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

    public String getDistanceHumanReadable() {
        return distanceHumanReadable;
    }

    public void setDistanceHumanReadable(String distanceHumanReadable) {
        this.distanceHumanReadable = distanceHumanReadable;
    }

    public String getDurationHumanReadable() {
        return durationHumanReadable;
    }

    public void setDurationHumanReadable(String durationHumanReadable) {
        this.durationHumanReadable = durationHumanReadable;
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
        return new PlaceData(location.getLongitude(), location.getLatitude());
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "PlaceItem{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                ", distanceHumanReadable='" + distanceHumanReadable + '\'' +
                ", duration=" + duration +
                ", durationHumanReadable='" + durationHumanReadable + '\'' +
                ", rating='" + rating + '\'' +
                ", location=" + location +
                ", openNow=" + openNow +
                '}';
    }
}
