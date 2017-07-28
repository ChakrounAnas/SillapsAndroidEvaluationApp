package com.sillaps.evaluationapp.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;

/**
 * GoogleGeocoding is a class that use google geocoding api in order to implements  multiple
 * functionalities related to geocoding
 * @author ChakrounAnas
 * @version 1.0
 */

public class GoogleGeocoding {

    /**
     * Google geocoding api access token
     */
    private static final String API_KEY = "AIzaSyDows8ESF8MlH759loSDzyvlxrnK08BslI";

    /**
     * @see GeoApiContext
     */
    private final GeoApiContext context;

    /**
     * GoogleGeocoding constructor. <br>
     * When constructing GoogleGeocoding object, geoApiContext get instantiated using
     * google directions api access token
     */
    public GoogleGeocoding() {
        this.context = new GeoApiContext().setApiKey(API_KEY);
    }

    /**
     * Return address of a place giving it's latitude and longitude coordinates
     * @param position
     * Position of the place
     * @return address of a giving place
     * @see LatLng
     */
    public String addressOfPlace(LatLng position) {
        GeocodingApiRequest geocodingApiRequest =
                GeocodingApi.reverseGeocode(context,
                        new com.google.maps.model.LatLng(position.latitude, position.longitude));
        try {
            GeocodingResult[] geocodingResults = geocodingApiRequest.await();
            return geocodingResults[0].formattedAddress;
        } catch (Exception e) {
            return null;
        }
    }
}
