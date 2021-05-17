package com.andriynev.driver_helper_bot.enums;

public enum PlaceOrderBy {
    DISTANCE("distance"),
    RATING("rating");

    private final String orderBy;

    PlaceOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return this.orderBy;
    }
}
