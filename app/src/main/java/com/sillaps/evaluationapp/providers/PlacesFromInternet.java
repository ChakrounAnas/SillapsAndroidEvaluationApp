package com.sillaps.evaluationapp.providers;

import com.sillaps.evaluationapp.api.ContentfulPlaces;
import com.sillaps.evaluationapp.entities.Place;

import java.util.List;

/**
 * PlacesFromInternet is a class that implement PlacesProvider's interface
 * It's supposed to provide places from internet
 */

public class PlacesFromInternet implements PlacesProvider {

    /**
     * Return list of places from internet using contentful api
     * @return list of places
     * @see Place
     * @see PlacesProvider
     * @see ContentfulPlaces
     */
    @Override
    public List<Place> deliverPlaces() {
        ContentfulPlaces contentfulPlaces = ContentfulPlaces.getContentfulPlacesUniqueInstance();
        return contentfulPlaces.getAllPlaces();
    }
}