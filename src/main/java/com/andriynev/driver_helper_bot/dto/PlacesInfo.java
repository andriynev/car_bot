package com.andriynev.driver_helper_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PlacesInfo {
    @JsonIgnore
    @NotNull
    @Id
    private String id;

    private List<PlaceInfo> places;


    public PlacesInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PlaceInfo> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceInfo> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "PlacesInfo{" +
                "id='" + id + '\'' +
                ", places=" + places +
                '}';
    }
}
