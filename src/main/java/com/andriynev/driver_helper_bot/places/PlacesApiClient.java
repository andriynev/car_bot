package com.andriynev.driver_helper_bot.places;

import com.andriynev.driver_helper_bot.dto.Location;
import com.andriynev.driver_helper_bot.dto.PlaceItem;
import com.andriynev.driver_helper_bot.dto.PlacesRequest;
import com.andriynev.driver_helper_bot.dto.PlacesSearchResultByRatingComparator;
import com.andriynev.driver_helper_bot.enums.PlaceOrderBy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.*;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlacesApiClient {
    private final int defaultRadius = 2000;
    private final int maxItems = 7;
    private final String defaultLanguage = "uk";
    private final GoogleApiConfig googleApiConfig;

    @Autowired
    public PlacesApiClient(GoogleApiConfig googleApiConfig) {
        this.googleApiConfig = googleApiConfig;
    }

    public List<PlaceItem> getPlacesByRequest(PlacesRequest request, Location location) {
        List<PlaceItem> items = new ArrayList<>();
        NearbySearchRequest req = generateNearbyRequest(request, location);
        try {
            PlacesSearchResponse response = req.await();
            List<PlacesSearchResult> results = filterResults(response.results, request.getOrderBy());
            if (results.size() == 0) {
                return items;
            }
            DistanceMatrixApiRequest distanceMatrixApiRequest = generateDistanceRequest(results, location);
            DistanceMatrix matrix = distanceMatrixApiRequest.await();
            items = getPlaceItems(results, matrix);

        } catch (Exception exception) {
            exception.printStackTrace();
        }


        return items;
    }

    private NearbySearchRequest generateNearbyRequest(PlacesRequest request, Location location) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiConfig.getApiKey()).build();
        NearbySearchRequest req = PlacesApi.nearbySearchQuery(
                context, new LatLng(location.getLatitude(), location.getLongitude())
        );

        switch (request.getPlaceType()) {
            case CAR_REPAIR:
                req.type(PlaceType.CAR_REPAIR).keyword(request.getPlaceType().getLocalizedValue());
                break;
            case CAR_WASH:
                req.type(PlaceType.CAR_WASH);
                break;
            case GAS_STATION:
                req.type(PlaceType.GAS_STATION);
                break;
        }

        if (request.isOpenNow()) {
            req.openNow(true);
        }

        switch (request.getOrderBy()) {
            case RATING:
                req.rankby(RankBy.PROMINENCE).radius(defaultRadius);
                break;
            case DISTANCE:
                req.rankby(RankBy.DISTANCE);
                break;
        }

        req.language(defaultLanguage);

        return req;
    }

    private DistanceMatrixApiRequest generateDistanceRequest(List<PlacesSearchResult> results, Location location) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiConfig.getApiKey()).build();
        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context)
                .origins(new LatLng(location.getLatitude(), location.getLongitude()))
                .mode(TravelMode.DRIVING)
                .units(Unit.METRIC)
                .language(defaultLanguage);

        LatLng[] destinations = new LatLng[results.size()];
        for (int i = 0; i < results.size(); i++) {
            destinations[i] = results.get(i).geometry.location;
        }

        req.destinations(destinations);
        return req;
    }

    private List<PlacesSearchResult> filterResults(PlacesSearchResult[] results, PlaceOrderBy orderBy) {
        List<PlacesSearchResult> filteredResults = new ArrayList<>();
        switch (orderBy){
            case DISTANCE:
                filteredResults = Arrays.stream(results)
                        .filter((placesSearchResult) -> !placesSearchResult.permanentlyClosed)
                        .limit(maxItems)
                        .collect(Collectors.toList());
                return filteredResults;
            case RATING:
                filteredResults = Arrays.stream(results)
                        .sorted(new PlacesSearchResultByRatingComparator())
                        .filter((placesSearchResult) -> !placesSearchResult.permanentlyClosed)
                        .limit(maxItems)
                        .collect(Collectors.toList());
                return filteredResults;
        }
        return filteredResults;
    }

    private List<PlaceItem> getPlaceItems(List<PlacesSearchResult> results, DistanceMatrix matrix) {
        List<PlaceItem> placeItems = new ArrayList<>();
        DistanceMatrixRow row = matrix.rows[0];
        for (int i = 0; i < matrix.rows.length; i++) {
            PlacesSearchResult result = results.get(i);
            DistanceMatrixElement element = row.elements[i];
            placeItems.add(new PlaceItem(
                    result.name,
                    result.vicinity,
                    element.distance.humanReadable,
                    element.duration.humanReadable,
                    String.valueOf(result.rating),
                    new Location(result.geometry.location.lng, result.geometry.location.lat),
                    result.openingHours.openNow
                )
            );
        }
        return placeItems;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("PlacesApiClient is running........");
        System.out.println(googleApiConfig);
        // Location{longitude=30.448627, latitude=50.441301}


        /*GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiConfig.getApiKey()).build();
        NearbySearchRequest req = PlacesApi.nearbySearchQuery(context, new LatLng(50.441301, 30.448627))
                .rankby(RankBy.PROMINENCE).radius(5000).type(PlaceType.CAR_REPAIR).language("uk");

        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiConfig.getApiKey()).build();
        NearbySearchRequest req = PlacesApi.nearbySearchQuery(context, new LatLng(50.441301, 30.448627))
                .rankby(RankBy.DISTANCE).type(PlaceType.CAR_REPAIR).keyword("сто").language("uk");


        try {
            PlacesSearchResponse response = req.await();
            System.out.println(response.nextPageToken);
            for (PlacesSearchResult result: response.results) {
                System.out.println(result);
                System.out.println("result.vicinity");
                System.out.println(result.vicinity);
                System.out.println("result.businessStatus");
                System.out.println(result.businessStatus);
                System.out.println("result.permanentlyClosed");
                System.out.println(result.permanentlyClosed);

                if (result.openingHours != null) {
                    System.out.println("result.openingHours.openNow");
                    System.out.println(result.openingHours.openNow);
                    System.out.println("result.openingHours.weekdayText");
                    if (result.openingHours.weekdayText != null) {
                        for (String day: result.openingHours.weekdayText) {
                            System.out.println(day);
                        }
                    }

                    System.out.println("periods");
                    if (result.openingHours.periods != null) {
                        for (OpeningHours.Period period: result.openingHours.periods) {
                            if (period.open != null) {
                                System.out.println("open");
                                System.out.println(period.open);
                                System.out.println(period.open.day);
                                System.out.println(period.open.time);
                            }

                            if (period.close != null) {
                                System.out.println("close");
                                System.out.println(period.close);
                                System.out.println(period.close.day);
                                System.out.println(period.close.time);
                            }
                        }
                    }
                }


                System.out.println("Photo");
                if (result.photos != null) {
                    for (Photo photo :
                            result.photos) {
                        System.out.println("height");
                        System.out.println(photo.height);
                        System.out.println("width");
                        System.out.println(photo.width);
                        System.out.println("photoReference");
                        System.out.println(photo.photoReference);
                        for (String attr :
                                photo.htmlAttributions) {
                            System.out.println(attr);
                        }
                    }
                }

            }

            DistanceMatrixApiRequest distanceMatrixApiRequest = DistanceMatrixApi.newRequest(context);
            distanceMatrixApiRequest.origins(new LatLng(50.441301, 30.448627))
                    .destinations(response.results[0].geometry.location, response.results[1].geometry.location, response.results[2].geometry.location)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .language("uk");

            DistanceMatrix matrix = distanceMatrixApiRequest.await();
            System.out.println(matrix);
            for (String origin: matrix.originAddresses) {
                System.out.println(origin);
            }

            for (String dest: matrix.destinationAddresses) {
                System.out.println(dest);
            }

            for (DistanceMatrixRow row: matrix.rows) {
                System.out.println(row);
                for (DistanceMatrixElement el: row.elements) {
                    System.out.println(el.status);
                    System.out.println(el.distance.inMeters);
                    System.out.println(el.distance.humanReadable);
                    System.out.println(el.duration.inSeconds);
                    System.out.println(el.duration.humanReadable);
                    System.out.println(el.durationInTraffic);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
