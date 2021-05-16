package com.andriynev.driver_helper_bot.dto;

import com.andriynev.driver_helper_bot.enums.PlaceOrderBy;
import com.andriynev.driver_helper_bot.enums.PlaceType;

public class PlacesRequest {
    private PlaceType placeType;
    private boolean openNow;
    private PlaceOrderBy orderBy;

    public PlacesRequest() {
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public PlaceOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(PlaceOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return "PlacesRequest{" +
                "placeType=" + placeType +
                ", openNow=" + openNow +
                ", orderBy=" + orderBy +
                '}';
    }
}
