package com.andriynev.driver_helper_bot.dto;

public class PlaceData {
    private String n;
    private double lt;
    private double lg;

    public PlaceData(String name, double longitude, double latitude) {
        n = name;
        lg = longitude;
        lt = latitude;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
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
                "n='" + n + '\'' +
                ", lt=" + lt +
                ", lg=" + lg +
                '}';
    }
}
