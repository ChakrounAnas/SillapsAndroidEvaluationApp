package com.sillaps.evaluationapp.elements;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sillaps.evaluationapp.R;
import com.sillaps.evaluationapp.activities.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * RouteInformationsLayout represents the layout that display route informations between two places
 * @author ChakrounAnas
 * @version 1.0
 */

public class RouteInformationsLayout {

    /**
     * User destination image placed in a circle image view
     * @see ImageView
     * @see CircleImageView
     */
    private CircleImageView destinationImage;

    /**
     * Number of hours to get from current place to the destination
     * @see TextView
     */
    private TextView nbrOfHoursTextView;

    /**
     * Number of minutes to get from current place to the destination excluding the number of hours
     * @see TextView
     */
    private TextView nbrOfMinutesTextView;

    /**
     * Distance between the current place and the destination in kilometers
     */
    private TextView distanceTextView;

    /**
     * Linear layout that contains the informations about the route between current position
     * and the destination using a specific travel mode
     * @see com.sillaps.evaluationapp.entities.Route
     * @see LinearLayout
     */
    private LinearLayout routeInfosLayout;

    /**
     * Context from which the MainActivityToolbar is created <br>
     * In our case it's MainActivity
     * @see Context
     */
    private final Context context;

    public RouteInformationsLayout(Context context) {
        this.context = context;
        getElements();
    }

    /**
     * Get elements of route informations layout
     */
    private void getElements() {
        routeInfosLayout = ((MainActivity) context).findViewById(R.id.route_infos_layout);
        destinationImage = ((MainActivity) context).findViewById(R.id.destination_image);
        nbrOfHoursTextView = ((MainActivity) context).findViewById(R.id.nbr_of_hours_text_view);
        nbrOfMinutesTextView = ((MainActivity) context).findViewById(R.id.nbr_of_minutes_text_view);
        distanceTextView = ((MainActivity) context).findViewById(R.id.distance_text_view);
    }

    public CircleImageView getDestinationImage() {
        return destinationImage;
    }

    public TextView getNbrOfHoursTextView() {
        return nbrOfHoursTextView;
    }

    public TextView getNbrOfMinutesTextView() {
        return nbrOfMinutesTextView;
    }

    public TextView getDistanceTextView() {
        return distanceTextView;
    }

    public LinearLayout getRouteInfosLayout() {
        return routeInfosLayout;
    }
}