package com.sillaps.evaluationapp.elements;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.maps.model.TravelMode;
import com.sillaps.evaluationapp.R;
import com.sillaps.evaluationapp.activities.MainActivity;
import com.sillaps.evaluationapp.map.AppMap;
import com.sillaps.evaluationapp.utilities.Fonts;

/**
 * MainActivityToolbar is a class that holds the elements of the main activity toolbar and handle
 * the events associated to them
 * @author ChakrounAnas
 * @version 1.0
 */

public class MainActivityToolbar {

    /**
     * @see TravelMode
     */
    private TravelMode travelMode;

    /**
     * Icon of driving mode in the toolbar
     * @see ImageView
     */
    private ImageView toolbarDrivingIcon;

    /**
     * Icon of transit mode in the toolbar
     * @see ImageView
     */
    private ImageView toolbarTransitIcon;

    /**
     * Icon to walking mode ine the toolbar
     */
    private ImageView toolbarWalkingIcon;

    /**
     * Description of icon driving mode in the toolbar
     * The description is formed by the text views in a linear layout
     * @see TextView
     * @see LinearLayout
     */
    private LinearLayout toolbarDrivingIconDesc;

    /**
     * Description of icon transit mode in the toolbar
     * The description is formed by the text views in a linear layout
     * @see TextView
     * @see LinearLayout
     */
    private LinearLayout toolbarTransitIconDesc;

    /**
     * Description of icon walking mode in the toolbar
     * The description is formed by the text views in a linear layout
     * @see TextView
     * @see LinearLayout
     */
    private LinearLayout toolbarWalkingIconDesc;

    /**
     * Context from which the MainActivityToolbar is created <br>
     * In our case it's MainActivity
     * @see Context
     */
    private final Context context;

    /**
     * The application map
     * Toolbar will notify it by events that occur its elements
     * @see AppMap
     */
    private AppMap appMap;

    /**
     * MainActivityToolbar constructor
     * @param context
     * Context from which the MainActivityToolbar is created <br>
     * In our case it's MainActivity
     * @see Context
     */
     public MainActivityToolbar(Context context) {
        this.context = context;
        this.travelMode = TravelMode.DRIVING;
        styleToolbarTitle();
        getElements();
        setFocusOnCurrentTransportationToolbarIcon();
        handleIconsClick();
    }

    /**
     * Set the application map
     * @param appMap
     * The map of application
     */
     public void setAppMap(AppMap appMap) {
        this.appMap = appMap;
    }

    /**
     * Return travel mode
     * @return travel mode
     */
     public TravelMode getTravelMode() {
        return travelMode;
    }

    /**
     * Get the elements of the toolbar
     */
    private void getElements() {

        // Getting toolbar icons
        this.toolbarDrivingIcon =
                ((MainActivity) context).findViewById(R.id.toolbar_driving_icon);
        this.toolbarTransitIcon =
                ((MainActivity) context).findViewById(R.id.toolbar_transit_icon);
        this.toolbarWalkingIcon =
                ((MainActivity) context).findViewById(R.id.toolbar_walking_icon);

        // Getting toolbar icons descriptions text views
        this.toolbarDrivingIconDesc =
                ((MainActivity) context).findViewById(R.id.toolbar_driving_icon_desc);
        this.toolbarTransitIconDesc =
                ((MainActivity) context).findViewById(R.id.toolbar_transit_icon_desc);
        this.toolbarWalkingIconDesc =
                ((MainActivity) context).findViewById(R.id.toolbar_walking_icon_desc);
    }

    /**
     * Set font to toolbar title
     */
    private void styleToolbarTitle() {
        TextView appNameTextView = ((MainActivity) context).findViewById(R.id.app_name);
        appNameTextView.setTypeface(Fonts.MERRIWEATHER_BLACK);
    }

    /**
     * Handle the click of icons
     */
    private void handleIconsClick() {
        // Getting driving icon mode frame layout and setting a click listener on it
        final FrameLayout drivingIconFrameLayout =
                ((MainActivity) context).findViewById(R.id.frame_layout_driving_icon);
        drivingIconFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Setting the travel mode to driving
                travelMode = TravelMode.DRIVING;
                // Driving icon in the toolbar get the focus
                setFocusOnCurrentTransportationToolbarIcon();
                // Notifying the app map that the travel mode has changed
                if(appMap != null) {
                    appMap.travelModeChanged(travelMode);
                }
            }
        });
        // Getting transit icon mode frame layout and setting a click listener on it
        final FrameLayout transitIconFrameLayout =
                ((MainActivity)context).findViewById(R.id.frame_layout_transit_icon);
        transitIconFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Setting the travel mode to transit
                travelMode = TravelMode.TRANSIT;
                // Transit icon in the toolbar get the focus
                setFocusOnCurrentTransportationToolbarIcon();
                // Notifying the app map that the travel mode has changed
                if(appMap != null) {
                    appMap.travelModeChanged(travelMode);
                }
            }
        });
        // Getting walking icon mode frame layout and setting a click listener on it
        final FrameLayout walkingIconFrameLayout =
                ((MainActivity)context).findViewById(R.id.frame_layout_walking_icon);
        walkingIconFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Setting the travel mode to walking
                travelMode = TravelMode.WALKING;
                // Walking icon in the toolbar get the focus
                setFocusOnCurrentTransportationToolbarIcon();
                // Notifying the app map that the travel mode has changed
                if(appMap != null) {
                    appMap.travelModeChanged(travelMode);
                }
            }
        });
    }

    /**
     * Set focus on the icon that represents the current travel mode
     */
    private void setFocusOnCurrentTransportationToolbarIcon() {
        switch (travelMode) {
            case DRIVING:
                toolbarDrivingIcon.setImageResource(R.drawable.ic_car_black_24dp);
                toolbarDrivingIconDesc.setVisibility(View.VISIBLE);
                toolbarTransitIcon.setImageResource(R.drawable.ic_transit_grey_24dp);
                toolbarTransitIconDesc.setVisibility(View.INVISIBLE);
                toolbarWalkingIcon.setImageResource(R.drawable.ic_walking_grey_24dp);
                toolbarWalkingIconDesc.setVisibility(View.INVISIBLE);
                break;
            case TRANSIT:
                toolbarDrivingIcon.setImageResource(R.drawable.ic_car_grey_24dp);
                toolbarDrivingIconDesc.setVisibility(View.INVISIBLE);
                toolbarTransitIcon.setImageResource(R.drawable.ic_transit_black_24dp);
                toolbarTransitIconDesc.setVisibility(View.VISIBLE);
                toolbarWalkingIcon.setImageResource(R.drawable.ic_walking_grey_24dp);
                toolbarWalkingIconDesc.setVisibility(View.INVISIBLE);
                break;
            case WALKING:
                toolbarDrivingIcon.setImageResource(R.drawable.ic_car_grey_24dp);
                toolbarDrivingIconDesc.setVisibility(View.INVISIBLE);
                toolbarTransitIcon.setImageResource(R.drawable.ic_transit_grey_24dp);
                toolbarTransitIconDesc.setVisibility(View.INVISIBLE);
                toolbarWalkingIcon.setImageResource(R.drawable.ic_walking_black_24dp);
                toolbarWalkingIconDesc.setVisibility(View.VISIBLE);
                break;
        }
    }
}