package com.sillaps.evaluationapp.utilities;

/**
 * TimeTransformations is a class with multiple transformations related to a time
 * @author ChakrounAnas
 * @version 1.0
 */

public class TimeTransformations {

    /**
     * Return number of hours giving a number of seconds
     * @param nbrOfSeconds
     * Number of seconds
     * @return number of hours giving a number of seconds
     */
    public static long nbrOfHours(long nbrOfSeconds) {
        return nbrOfSeconds / 3600;
    }

    /**
     * Return number of minutes excluding the number of hours from a number of seconds
     * @param nbrOfSeconds
     * Number of seconds
     * @return number of minutes excluding the number of hours from a number of seconds
     */
    public static long nbrOfMinutesAfterRemovingHours(long nbrOfSeconds) {
        return (nbrOfSeconds - nbrOfHours(nbrOfSeconds) * 3600) / 60;
    }
}