package com.sillaps.evaluationapp.providers;

import com.sillaps.evaluationapp.entities.Place;

import java.util.List;

/**
 * PlacesProvider is an interface that specify the methods to be implemented by a places provider.
 * @author ChakrounAnas
 * @version 1.0
 */

public interface PlacesProvider {
    /**
     * Return a list of places
     * @return list of places
     * @see Place
     */
    List<Place> deliverPlaces();
}