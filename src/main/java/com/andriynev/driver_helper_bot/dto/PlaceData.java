package com.andriynev.driver_helper_bot.dto;

public class PlaceData {
    private double lt;
    private double lg;

    public PlaceData(double longitude, double latitude) {
        lg = longitude;
        lt = latitude;
    }

    public PlaceData() {

    }

    public double getLt() {
        return lt;
    }

    public void setLt(double lt) {
        this.lt = lt;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }

    @Override
    public String toString() {
        return "PlaceData{" +
                "lt=" + lt +
                ", lg=" + lg +
                '}';
    }
}
