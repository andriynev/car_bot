package com.andriynev.driver_helper_bot.dto;

import com.google.maps.model.PlacesSearchResult;

import java.util.Comparator;

public class PlacesSearchResultByRatingComparator implements Comparator<PlacesSearchResult> {
    @Override
    public int compare(PlacesSearchResult firstPlayer, PlacesSearchResult secondPlayer) {
        return Float.compare(firstPlayer.rating, secondPlayer.rating);
    }
}
