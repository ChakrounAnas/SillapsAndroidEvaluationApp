package com.sillaps.evaluationapp.entities;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Route is a class that represents a route in the real world <br>
 * A route is characterized by the following properties
 * <ul>
 *     <li>Distance in meters</li>
 *     <li>Duration in seconds</li>
 *     <li>Path of the route</li>
 * </ul>
 * @author ChakrounAnas
 * @version 1.0
 */

public class Route {

    /**
     * Distance in meters of the route
     */
    private final long distanceInMeters;

    /**
     * Duration in seconds in order to accomplish the road
     */
    private final long durationInSeconds;

    /**
     * Lines that represents the path of the route
     * @see PolylineOptions
     * @see com.google.android.gms.maps.model.Polyline
     */
    private final List<PolylineOptions> listOfPolylineOptions;

    /**
     *
     * @param distanceInMeters
     * Distance in meters
     * @param durationInSeconds
     * Duration in seconds
     * @param listOfPolylineOptions
     * Path of the route
     */
    public Route(long distanceInMeters, long durationInSeconds, List<PolylineOptions> listOfPolylineOptions) {
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
        this.listOfPolylineOptions = listOfPolylineOptions;
    }

    /**
     * Return distance in meters of the route
     * @return distance in meters of the route
     */
    public long getDistanceInMeters() {
        return distanceInMeters;
    }

    /**
     * Return duration in seconds of the route
     * @return duration in seconds of the route
     */
    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    /**
     * Return path of the route
     * @return path of the route
     */
    public List<PolylineOptions> getListOfPolylineOptions() {
        return listOfPolylineOptions;
    }
}
