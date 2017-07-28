package com.sillaps.evaluationapp.utilities;

/**
 * DistanceTransformations is a class with multiple transformations related to a distance
 * @author ChakrounAnas
 * @version 1.0
 */

public class DistanceTransformations {

    /**
     * Return distance in kilometers
     * @param nbrOfMeters
     * Distance in meters
     * @return distance in kilometers
     */
    public static float nbrOfKilometers(long nbrOfMeters) {
        return nbrOfMeters / 1000f;
    }
}
