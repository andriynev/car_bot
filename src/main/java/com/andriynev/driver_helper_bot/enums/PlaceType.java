package com.andriynev.driver_helper_bot.enums;

public enum PlaceType {
    CAR_REPAIR("car_repair", "СТО"),
    CAR_WASH("car_wash", "Мийка"),
    GAS_STATION("gas_station", "АЗС");

    private final String placeType;
    private final String localizedValue;

    private PlaceType(String placeType, String localizedValue) {
        this.placeType = placeType;
        this.localizedValue = localizedValue;
    }

    public String getLocalizedValue() {
        return this.localizedValue;
    }

    @Override
    public String toString() {
        return this.placeType;
    }
}
