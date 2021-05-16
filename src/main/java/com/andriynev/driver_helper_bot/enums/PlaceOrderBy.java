package com.andriynev.driver_helper_bot.enums;

public enum PlaceOrderBy {
    DISTANCE("distance", "Відстань"),
    RATING("rating", "Рейтинг");

    private final String orderBy;
    private final String localizedValue;

    PlaceOrderBy(String orderBy, String localizedValue) {
        this.orderBy = orderBy;
        this.localizedValue = localizedValue;
    }

    public String getLocalizedValue() {
        return localizedValue;
    }

    @Override
    public String toString() {
        return this.orderBy;
    }
}
