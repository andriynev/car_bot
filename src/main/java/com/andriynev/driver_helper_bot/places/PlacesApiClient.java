package com.andriynev.driver_helper_bot.places;

import com.andriynev.driver_helper_bot.dto.Location;
import com.andriynev.driver_helper_bot.dto.PlaceItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.*;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PlacesApiClient {
    private final GoogleApiConfig googleApiConfig;

    @Autowired
    public PlacesApiClient(GoogleApiConfig googleApiConfig) {
        this.googleApiConfig = googleApiConfig;
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
