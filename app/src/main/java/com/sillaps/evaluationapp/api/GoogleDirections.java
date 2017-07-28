package com.sillaps.evaluationapp.api;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.sillaps.evaluationapp.R;
import com.sillaps.evaluationapp.entities.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * GoogleDirections is a class that use google directions api in order to implements multiple
 * functionalities related to direction
 * @author ChakrounAnas
 * @version 1.0
 */

public class GoogleDirections {

    private final String TAG = "GoogleDirectionsTAG";

    /**
     * Google directions api access token
     */
    private static final String API_KEY = "AIzaSyA3aLf4-iJ2NfIvvE8iMAzRrs2Xhw4pY5g";

    /**
     * @see GeoApiContext
     */
    private final GeoApiContext geoApiContext;

    /**
     * GoogleDirections constructor. <br>
     * When constructing GoogleDirections object, geoApiContext get instantiated using
     * google directions api access token
     */
    public GoogleDirections() {
        this.geoApiContext = new GeoApiContext().setApiKey(API_KEY);
    }

    /**
     * Return a Route object between two places giving a traveling mode.
     * @param context
     * Context from which this methods is called
     * @param mode
     * Mode of traveling
     * @param origin
     * Origin place
     * @param destination
     * Destination place
     * @return Route
     * @see TravelMode
     * @see Context
     * @see Route
     */
    public Route pathBetweenTwoPlaces(
            Context context,
            TravelMode mode,
            com.google.android.gms.maps.model.LatLng origin,
            com.google.android.gms.maps.model.LatLng destination) {
        DirectionsApiRequest directionsApiRequest =
                DirectionsApi.newRequest(geoApiContext);
        directionsApiRequest.origin(new LatLng(origin.latitude, origin.longitude));
        directionsApiRequest.destination(new LatLng(destination.latitude, destination.longitude));
        directionsApiRequest.mode(mode);
        Log.d(TAG, "pathBetweenTwoPlaces: " + mode);
        try {
            List<PolylineOptions> polylineOptions = new ArrayList<>();
            DirectionsResult directionsResult = directionsApiRequest.await();
            long durationInSeconds = 0;
            long distanceInMeters = 0;
            for(DirectionsRoute directionRoute: directionsResult.routes) {
                for(DirectionsLeg directionLeg: directionRoute.legs) {
                    for(DirectionsStep directionStep: directionLeg.steps) {
                        durationInSeconds+= directionStep.duration.inSeconds;
                        distanceInMeters+= directionStep.distance.inMeters;
                        Log.d(TAG, "pathBetweenTwoPlaces: " + distanceInMeters);
                        List<LatLng> positions = directionStep.polyline.decodePath();
                        PolylineOptions line =
                                new PolylineOptions()
                                        .width(5f)
                                        .color(ContextCompat.getColor(context, R.color.polylineColor));
                        for(LatLng position: positions) {
                            line
                                    .add(new com.google.android.gms.maps.model.LatLng(position.lat, position.lng));
                        }
                        polylineOptions.add(line);
                    }
                }
            }
            return new Route(distanceInMeters, durationInSeconds, polylineOptions);
        } catch (Exception e) {
            return null;
        }
    }
}