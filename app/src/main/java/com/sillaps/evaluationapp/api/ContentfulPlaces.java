package com.sillaps.evaluationapp.api;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.internal.LinkedTreeMap;
import com.sillaps.evaluationapp.entities.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * ContentfulPlaces is a class that use contentful delivery api in order to get places from contentful
 * The class implements singleton design pattern
 * @author ChakrounAnas
 * @version 1.0
 */

public class ContentfulPlaces {

    /**
     * Unique instance of ContentfulPlaces
     * @see ContentfulPlaces
     */
    private static ContentfulPlaces contentfulPlacesUniqueInstance;

    /**
     * Contentful api access token
     */
    private final String CDA_TOKEN =
            "4903b0a446524b239a087407af3fce3fea78b8408f6ae1f83781c729830b699c";

    /**
     * The id of Place entity
     */
    private final String SPACE_ID = "932f4i7fropd";

    /**
     * @see CDAClient
     */
    private final CDAClient cdaClient;

    /**
     * ContentfulPlaces constructor <br>
     * The constructor is private in order to not allow other classes to instantiate ContentfulPlaces directly. <br>
     * When constructing ContentfulPlaces "cdaClient" get instantiated using CDAClient's builder
     * and providing the access token and the id of place entity.
     */
    private ContentfulPlaces() {
        this.cdaClient = CDAClient
                .builder()
                .setToken(CDA_TOKEN)
                .setSpace(SPACE_ID)
                .build();
    }

    /**
     * Create ContentfulPlaces object if this is not done before and return it
     * @return ContentfulPlaces
     */
    public static ContentfulPlaces getContentfulPlacesUniqueInstance() {
        if(contentfulPlacesUniqueInstance == null) {
            contentfulPlacesUniqueInstance = new ContentfulPlaces();
        }
        return contentfulPlacesUniqueInstance;
    }

    /**
     * Getting all the places from contentful and then placing them in a list and return it
     * @return list of places
     * @see Place
     */
    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();
        try {
            final CDAArray placesFromContentful = cdaClient
                    .fetch(CDAEntry.class)
                    .where("content_type", "place")
                    .all();
            for (final CDAResource resource : placesFromContentful.items()) {
                final CDAEntry entry = (CDAEntry) resource;
                LinkedTreeMap location = entry.getField("location");
                String description = entry.getField("description");
                CDAAsset placeImage = entry.getField("image");
                LinkedTreeMap imageInfos = placeImage.getField("file");
                String imageLink = (String) imageInfos.get("url");
                Place place = new Place(new LatLng((double)location.get("lat"),
                        (double) location.get("lon")), description, imageLink);
                places.add(place);
            }
            return places;
        } catch (Exception e) {
            return null;
        }
    }
}