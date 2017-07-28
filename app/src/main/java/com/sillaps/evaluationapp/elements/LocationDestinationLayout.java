package com.sillaps.evaluationapp.elements;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sillaps.evaluationapp.R;
import com.sillaps.evaluationapp.activities.MainActivity;

/**
 * LocationDestinationLayout represents the layout that display current position address and
 * picked destination name
 * @author ChakrounAnas
 * @version 1.0
 */

public class LocationDestinationLayout {

    /**
     * User current location address
     * @see TextView
     */
    private TextView userLocationAddress;

    /**
     * User destination name
     * @see TextView
     */
    private TextView userDestination;

    /**
     * Linear layout that contains the current place address and the destination name
     * @see LinearLayout
     */
    private LinearLayout currentPositionAndDestinationLayout;

    /**
     * Context from which the MainActivityToolbar is created <br>
     * In our case it's MainActivity
     * @see Context
     */
    private final Context context;

    public LocationDestinationLayout(Context context) {
        this.context = context;
        getElements();
    }

    private void getElements() {
        userLocationAddress =
                ((MainActivity) context).findViewById(R.id.user_location_address);
        userDestination =
                ((MainActivity) context).findViewById(R.id.destination_text_view);
        currentPositionAndDestinationLayout =
                ((MainActivity) context).findViewById(R.id.current_position_and_destination_layout);
    }

    public TextView getUserLocationAddress() {
        return userLocationAddress;
    }

    public TextView getUserDestination() {
        return userDestination;
    }

    public LinearLayout getCurrentPositionAndDestinationLayout() {
        return currentPositionAndDestinationLayout;
    }
}
