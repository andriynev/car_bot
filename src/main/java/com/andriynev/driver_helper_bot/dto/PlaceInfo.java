package com.andriynev.driver_helper_bot.dto;

import java.util.List;

public class PlaceInfo {
    private String type;
    private List<String> brands;

    public PlaceInfo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "type='" + type + '\'' +
                ", brands=" + brands +
                '}';
    }
}
