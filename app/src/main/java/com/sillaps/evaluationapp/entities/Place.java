package com.sillaps.evaluationapp.entities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Place is a class that represents a place in the real world <br>
 * A place is characterized by the followings properties
 * <ul>
 *     <li>Position</li>
 *     <li>Name</li>
 *     <li>Link of an image representing the place</li>
 * </ul>
 * @author ChakrounAnas
 * @version 1.0
 */

public class Place {

    /**
     * Coordinates of the place: latitude and longitude
     * @see LatLng
     */
    private final LatLng position;

    /**
     * Name of the place
     */
    private final String name;

    /**
     * Link of an image representing the place
     */
    private final String imgLink;

    /**
     * Place constructor.
     * @param position
     * Coordinates of the place
     * @param name
     * Name of the place
     * @param imgLink
     * Link of an image representing the place
     * @see LatLng
     */
    public Place(LatLng position, String name, String imgLink) {
        this.position = position;
        this.name = name;
        this.imgLink = imgLink;
    }

    /**
     * Return the coordinates of the place
     * @return coordinates
     */
    public LatLng getPosition() {
        return position;
    }

    /**
     * Return the name of the place
     * @return place's name
     */
    public String getName() {
        return name;
    }

    /**
     * Return link of the image that represents the place
     * @return link of place's image
     */
    public String getImgLink() {
        return imgLink;
    }

    /**
     * The equality of two places is defined by the matching of their names
     * @param o
     * Object of type Place
     * @return true if this place and the given place are identicals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        return name.equals(place.name);

    }

    /**
     * Return the hashCode of the place
     * @return hashCode of the place
     */
    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}