package com.andriynev.driver_helper_bot.places;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.dto.Location;
import com.andriynev.driver_helper_bot.dto.PlaceItem;
import com.andriynev.driver_helper_bot.dto.PlacesRequest;
import com.andriynev.driver_helper_bot.enums.PlaceOrderBy;
import com.google.maps.*;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class PlacesApiClient {
    private final int defaultRadius = 2000;
    private final int maxItems = 5;
    private final String defaultLanguage = "uk";
    private final GoogleApiConfig googleApiConfig;
    private final MessagesProperties messagesProperties;

    @Autowired
    public PlacesApiClient(GoogleApiConfig googleApiConfig, MessagesProperties messagesProperties) {
        this.googleApiConfig = googleApiConfig;
        this.messagesProperties = messagesProperties;
    }

    public List<PlaceItem> getPlacesByRequest(PlacesRequest request, Location location) {
        List<PlaceItem> items = new ArrayList<>();
        NearbySearchRequest req = generateNearbyRequest(request, location);
        try {
            System.out.println(request);
            System.out.println(location);
            PlacesSearchResponse response = req.await();
            PlacesSearchResult[] results = filterResults(response.results, request.getOrderBy());
            if (results.length == 0) {
                return items;
            }

            DistanceMatrixApiRequest distanceMatrixApiRequest = generateDistanceRequest(results, location);
            DistanceMatrix matrix = distanceMatrixApiRequest.await();
            items = getPlaceItems(request, results, matrix);

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
                req.type(PlaceType.CAR_REPAIR).keyword(this.messagesProperties.getMessage(request.getPlaceType().toString()));
                break;
            case CAR_WASH:
                req.type(PlaceType.CAR_WASH).keyword(this.messagesProperties.getMessage(request.getPlaceType().toString()));
                break;
            case GAS_STATION:
                req.type(PlaceType.GAS_STATION).keyword(this.messagesProperties.getMessage(request.getPlaceType().toString()));
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

    private DistanceMatrixApiRequest generateDistanceRequest(PlacesSearchResult[] results, Location location) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiConfig.getApiKey()).build();
        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context)
                .origins(new LatLng(location.getLatitude(), location.getLongitude()))
                .mode(TravelMode.DRIVING)
                .units(Unit.METRIC)
                .language(defaultLanguage);

        LatLng[] destinations = new LatLng[results.length];
        for (int i = 0; i < results.length; i++) {
            destinations[i] = results[i].geometry.location;
        }

        req.destinations(destinations);
        return req;
    }

    private PlacesSearchResult[] filterResults(PlacesSearchResult[] results, PlaceOrderBy orderBy) {
        Stream<PlacesSearchResult> stream = Arrays.stream(results)
                .filter((placesSearchResult) -> !placesSearchResult.permanentlyClosed
                        && placesSearchResult.openingHours != null
                        && placesSearchResult.vicinity != null
                        && placesSearchResult.name != null
                        && placesSearchResult.geometry != null
                        && placesSearchResult.geometry.location != null
                );

        switch (orderBy){
            case DISTANCE:
                break;
            case RATING:
                stream = stream.sorted(Comparator.comparing((PlacesSearchResult p)->p.rating).reversed());
                break;
        }

        return stream.limit(maxItems)
                .toArray(PlacesSearchResult[]::new);
    }

    private List<PlaceItem> getPlaceItems(PlacesRequest request, PlacesSearchResult[] results, DistanceMatrix matrix) {
        List<PlaceItem> placeItems = new ArrayList<>();
        if (matrix.rows.length == 0) {
            return placeItems;
        }
        DistanceMatrixRow row = matrix.rows[0];
        for (int i = 0; i < matrix.rows[0].elements.length; i++) {
            PlacesSearchResult result = results[i];
            DistanceMatrixElement element = row.elements[i];
            if ((element.distance == null || element.distance.humanReadable == null)
                    || (element.duration == null || element.duration.humanReadable == null)) {
                continue;
            }

            placeItems.add(new PlaceItem(
                    result.name,
                    result.vicinity,
                    element.distance.inMeters,
                    element.distance.humanReadable,
                    element.duration.inSeconds,
                    element.duration.humanReadable,
                    String.valueOf(result.rating),
                    new Location(
                            result.geometry.location.lng,
                            result.geometry.location.lat
                            ),
                    result.openingHours.openNow
                )
            );
        }

        if (placeItems.size() == 0) {
            return placeItems;
        }

        if (request.getOrderBy().equals(PlaceOrderBy.DISTANCE)) {
            placeItems.sort(Comparator.comparing(PlaceItem::getDuration)
                    .thenComparing(PlaceItem::getDistance));
        }

        return placeItems;
    }

    private void printResponse(PlacesSearchResponse response) {
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

        }
    }

    private void printFilteredResults(PlacesSearchResult[] results) {
        for (PlacesSearchResult result: results) {
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

        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("PlacesApiClient is running........");


        /*PlacesRequest request = new PlacesRequest();
        request.setPlaceType(com.andriynev.driver_helper_bot.enums.PlaceType.CAR_WASH);
        request.setOrderBy(PlaceOrderBy.RATING);
        request.setOpenNow(true);
        List<PlaceItem> placeItems = getPlacesByRequest(request, new Location(30.449104, 50.441627));
        System.out.println("Places");
        for (PlaceItem placeItem :
                placeItems) {
            System.out.println("-------------BEGIN--------------");
            System.out.println(placeItem);
            System.out.println("-------------END--------------");
        }*/
    }
}
