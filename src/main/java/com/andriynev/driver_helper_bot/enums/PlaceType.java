package com.andriynev.driver_helper_bot.enums;

public enum PlaceType {
    CAR_REPAIR("car-repair"),
    CAR_WASH("car-wash"),
    GAS_STATION("gas-station");

    private final String placeType;

    private PlaceType(String placeType) {
        this.placeType = placeType;
    }

    @Override
    public String toString() {
        return this.placeType;
    }
}
