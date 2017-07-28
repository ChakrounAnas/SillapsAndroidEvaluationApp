package com.sillaps.evaluationapp.map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.TravelMode;
import com.sillaps.evaluationapp.R;
import com.sillaps.evaluationapp.api.GoogleDirections;
import com.sillaps.evaluationapp.api.GoogleGeocoding;
import com.sillaps.evaluationapp.elements.LocationDestinationLayout;
import com.sillaps.evaluationapp.elements.RouteInformationsLayout;
import com.sillaps.evaluationapp.entities.Place;
import com.sillaps.evaluationapp.entities.Route;
import com.sillaps.evaluationapp.providers.PlacesFromInternet;
import com.sillaps.evaluationapp.providers.PlacesProvider;
import com.sillaps.evaluationapp.utilities.DistanceTransformations;
import com.sillaps.evaluationapp.utilities.InternetConnection;
import com.sillaps.evaluationapp.utilities.TimeTransformations;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * AppMap is the map of the application that handle all activities about the map
 * @author ChakrounAnas
 * @version 1.0
 */

public class AppMap implements GoogleMap.OnMarkerClickListener{

    private final String TAG = "AppMapTAG";

    /**
     * @see Context
     */
    private final Context context;

    /**
     * Google map
     * @see GoogleMap
     */
    private GoogleMap mMap;

    /**
     * Layout that display current location address and picked location name
     * @see LocationDestinationLayout
     */
    private final LocationDestinationLayout locationDestinationLayout;

    /**
     * User current position coordinates
     * @see LatLng
     */
    private LatLng currentUserPosition;

    /**
     * Destination place
     * @see Place
     */
    private Place currentDestinationPlace;

    /**
     * Layout that display the informations of a route
     * @see RouteInformationsLayout
     */
    private final RouteInformationsLayout routeInfosLayout;

    /**
     * Provider of places
     * @see PlacesProvider
     */
    private final PlacesProvider placesProvider;

    /**
     * Places around the user
     * @see Place
     */
    private List<Place> placesAroundUser;

    /**
     * Makers that mark each place around the user
     * @see Place
     * @see Marker
     */
    private final HashMap<Marker, Place> markers;

    /**
     * Current travel mode
     * @see TravelMode
     */
    private TravelMode currentTravelMode;

    /**
     * Previous travel mode
     * @see TravelMode
     */
    private TravelMode previousTravelMode;

    /**
     * Current route path
     * @see Polyline
     */
    private final List<Polyline> currentPathLines;

    /**
     * @see GoogleDirections
     */
    private final GoogleDirections googleDirections;

    /**
     * Async task that checks internet connection and if the user is connected it get the path
     * between current place and destination and display it in the map with other informations
     * about the route <br>
     * If the user is not connected we inform him by displaying a toast
     * @see AsyncTask
     */
    private CheckInternetConnectionAndThenDrawTheRoute checkInternetConnectionAndDrawPath;

    /**
     * @see GoogleGeocoding
     */
    private final GoogleGeocoding googleGeocoding;

    /**
     * Boolean that indicates if we are currently showing a path between the current position
     * and the destination
     */
    private boolean showingPath;

    /**
     * AppMap constructor
     * @param context
     * Context from which this class get instantiated
     * @param mapFragment
     * Fragment of google map
     * @param locationDestinationLayout
     * Layout that display current location address and picked destination name
     * @param routeInfosLayout
     * Route informations between current position and the destination
     * @param currentTravelMode
     * Travel mode that
     * @see Context
     * @see SupportMapFragment
     * @see TextView
     * @see CircleImageView
     * @see TravelMode
     */
    public AppMap(Context context, SupportMapFragment mapFragment,
                  LocationDestinationLayout locationDestinationLayout,
                  RouteInformationsLayout routeInfosLayout,
                  TravelMode currentTravelMode) {
        this.context = context;
        this.currentTravelMode = currentTravelMode;
        this.previousTravelMode = currentTravelMode;
        this.locationDestinationLayout = locationDestinationLayout;
        this.routeInfosLayout = routeInfosLayout;
        this.markers = new HashMap<>();
        this.placesProvider = new PlacesFromInternet();
        this.googleDirections = new GoogleDirections();
        this.googleGeocoding = new GoogleGeocoding();
        this.currentPathLines = new ArrayList<>();
        this.checkInternetConnectionAndDrawPath
                = new CheckInternetConnectionAndThenDrawTheRoute();
        mapFragment.getMapAsync(new MapCallbacks(context, this));
    }

    void setMap(GoogleMap map) {
        this.mMap = map;
        this.mMap.setOnMarkerClickListener(this);
    }

    void setCurrentUserPosition(LatLng position) {
        this.currentUserPosition = position;
    }

    /**
     * Indicates if showing currently a path between the current position and chosen destination
     * @return if showing path or not
     */
    public boolean isShowingPath() {
        return showingPath;
    }

    /**
     * Handling user choosing another travel mode <br>
     * If the user has already picked a destination then we recalculate route informations and trace
     * the new path using the new travel mode <br>
     * If not we display the markers of places around the user using the new travel mode
     * @param travelMode
     * Travel mode
     * @see TravelMode
     */
    public void travelModeChanged(TravelMode travelMode) {
        this.currentTravelMode = travelMode;
        if(currentDestinationPlace != null) {
            if(!currentTravelMode.equals(previousTravelMode)) {
                this.previousTravelMode = currentTravelMode;
                mMap.clear();
                routeInfosLayout.getRouteInfosLayout().setVisibility(View.GONE);
                addPlaceMarkerToMap(currentDestinationPlace.getPosition());
                drawRoutesBetweenCurrentAndTargetPlace();
            }
        } else {
            addPlacesAroundUserToTheMap();
        }
    }

    /**
     * Mark the position of the user in the map
     * @see LatLng
     */
    void markThePositionOfUserInTheMap() {
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.d(TAG, "onSuccess: " + e.getMessage());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom
                (currentUserPosition, 16));
    }

    /**
     * starting the async task that going to bring places around the user and display them in
     * the map
     */
    void getPlacesAroundUser() {
        if(placesProvider instanceof PlacesFromInternet) {
            PlacesFromInternetAsyncTask
                    placesFromInternetAsyncTask = new PlacesFromInternetAsyncTask();
            placesFromInternetAsyncTask.execute();
        }
    }

    /**
     * If no destination was picked then we determine the path and informations between current
     * position and the place marked by the marker in order to display them to the user
     * @param marker
     * Marker that mark a place in the map
     * @return whether the camera should move to marker and display informations window or not
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(currentDestinationPlace == null) {
            currentDestinationPlace = markers.get(marker);
            clearTheMap();
            addPlaceMarkerToMap(currentDestinationPlace);
            drawRoutesBetweenCurrentAndTargetPlace();
        }
        return false;
    }

    /**
     * Removing the current path displayed in the map
     */
    private void removeCurrentPathFromMap() {
        if(!currentPathLines.isEmpty()) {
            for(Polyline polyline: currentPathLines) {
                polyline.remove();
            }
            currentPathLines.clear();
        }
    }

    /**
     * Back from specific view to global view
     * The specific view is the view that shows only current location and destination with informations
     * of the route between them <br>
     * The global view is the view that shows the current place and all places around it
     */
    public void getBackTheMapToTheGlobalView() {
        showingPath = false;
        currentDestinationPlace = null;
        locationDestinationLayout.getUserDestination()
                .setTextColor(ContextCompat.getColor(context, R.color.destination_default_color));
        locationDestinationLayout.getUserDestination().setText(context.getString(R.string.choose_destination));
        routeInfosLayout.getRouteInfosLayout().setVisibility(View.GONE);
        removeCurrentPathFromMap();
        addPlacesAroundUserToTheMap();
    }

    /**
     * Async task that determines the route between current place and the destination and then
     * display the path and also the informations of the route
     */
    private class DrawRouteBetweenTwoPlacesAsyncTask
            extends AsyncTask<Void, Void, Route> {

        @Override
        protected Route doInBackground(Void... voids) {
            return googleDirections.pathBetweenTwoPlaces(context,
                    currentTravelMode, currentUserPosition, currentDestinationPlace.getPosition());
        }

        @Override
        protected void onPostExecute(Route route) {
            super.onPostExecute(route);
            if(route != null) {
                for(PolylineOptions line: route.getListOfPolylineOptions()) {
                    Polyline polyline = mMap.addPolyline(line);
                    currentPathLines.add(polyline);
                }
                showingPath = true;
                showDestinationImage();
                showRouteInfos(route);
            } else {
                Toast toast = Toast.makeText(context, context.getString(R.string.error_occurred),
                        Toast.LENGTH_SHORT);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                currentDestinationPlace = null;
                addPlacesAroundUserToTheMap();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled: ");
        }
    }

    /**
     * Load and show destination image
     */
    private void showDestinationImage() {
        Picasso.with(context)
                .load("http:" + currentDestinationPlace.getImgLink())
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(routeInfosLayout.getDestinationImage(), new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: ");
                    }
                });
    }

    /**
     * Determines and shows route informations
     * @param route
     * Route between current place and the destination
     */
    private void showRouteInfos(Route route) {
        long nbrOfHours = TimeTransformations
                .nbrOfHours(route.getDurationInSeconds());
        long nbrOfMinutes = TimeTransformations
                .nbrOfMinutesAfterRemovingHours(route.getDurationInSeconds());
        if(nbrOfHours == 1){
            routeInfosLayout.getNbrOfHoursTextView().setText(context.getString(R.string.one_hour));
        } else {
            routeInfosLayout.getNbrOfHoursTextView()
                    .setText(context.getString(R.string.many_hours, String.valueOf(nbrOfHours)));
        }

        if(nbrOfMinutes == 1) {
            routeInfosLayout.getNbrOfMinutesTextView().setText(context.getString(R.string.one_minute));
        } else {
            routeInfosLayout.getNbrOfMinutesTextView()
                    .setText(context.getString(R.string.many_minutes, String.valueOf(nbrOfMinutes)));
        }
        routeInfosLayout.getDistanceTextView().setText(context.getString(R.string.distance, String.valueOf(
                DistanceTransformations.nbrOfKilometers(route.getDistanceInMeters()))));
        routeInfosLayout.getRouteInfosLayout().setVisibility(View.VISIBLE);
    }

    /**
     * Starting the async task that determine the route between current position and the destination
     * in order to draw the path and show informations
     */
    private void drawRoutesBetweenCurrentAndTargetPlace() {
        if(checkInternetConnectionAndDrawPath.getStatus() == AsyncTask.Status.RUNNING) {
            checkInternetConnectionAndDrawPath.cancel(true);
        }
        checkInternetConnectionAndDrawPath = new CheckInternetConnectionAndThenDrawTheRoute();
        checkInternetConnectionAndDrawPath.execute();
    }

    /**
     * Check if user is connected to internet then run the async task that will draw the route and
     * display informations about it <br>
     * If there is no internet connection then display a toast to inform the user
     */
    private class CheckInternetConnectionAndThenDrawTheRoute extends
            AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return InternetConnection.isConnectedToInternet(context);
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            super.onPostExecute(isConnected);
            if(isConnected) {
                locationDestinationLayout.getUserDestination()
                        .setTextColor(ContextCompat.getColor(context, R.color.black));
                locationDestinationLayout.getUserDestination()
                        .setText(currentDestinationPlace.getName());
                DrawRouteBetweenTwoPlacesAsyncTask drawPathBetweenLocationAndDestination
                        = new DrawRouteBetweenTwoPlacesAsyncTask();
                drawPathBetweenLocationAndDestination.execute();
            } else {
                Toast.makeText(context, context.getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT).show();
                currentDestinationPlace = null;
                addPlacesAroundUserToTheMap();
            }
        }
    }

    /**
     * Gets places from internet and mark them in the map
     * It also run the function that check for new places periodically
     */
    private class PlacesFromInternetAsyncTask extends AsyncTask<Void, Void, List<Place>> {

        @Override
        protected List<Place> doInBackground(Void... voids) {
            return placesProvider.deliverPlaces();
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            if(places != null) {
                placesAroundUser = places;
                addPlacesAroundUserToTheMap();
                // Check for new places periodically
                checkNewPlacesPeriodically();
            }
        }
    }

    /**
     * Get current usr position address and display it
     */
    private class UserCurrentPlaceAddressAsyncTask extends AsyncTask<LatLng, Void, String> {

        @Override
        protected String doInBackground(LatLng... latLngs) {
            return googleGeocoding.addressOfPlace(latLngs[0]);
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if(address != null) {
                locationDestinationLayout.getUserLocationAddress().setText(address);
            }
        }
    }

    /**
     * Run the async task that get the places around user and display theme in the map
     */
    void getAndDisplayCurrentUserPlaceAddress() {
        UserCurrentPlaceAddressAsyncTask
                userCurrentPlaceAddressAsyncTask = new UserCurrentPlaceAddressAsyncTask();
        userCurrentPlaceAddressAsyncTask.execute(currentUserPosition);
    }

    /**
     * Check for new places periodically
     * If there are new places then add them into the map
     */
    private void checkNewPlacesPeriodically() {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final List<Place> places = placesProvider.deliverPlaces();
                if(places != null) {
                    places.removeAll(placesAroundUser);
                    if(places.size() != 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addNewPlacesToTheMap(places);
                            }
                        });
                    }
                }
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * Add new places to the map
     * @param places
     * List of new places
     */
    private void addNewPlacesToTheMap(List<Place> places) {
        for(Place place: places) {
            placesAroundUser.add(place);
            if(!showingPath) {
                addPlaceMarkerToMap(place);
            }
        }
    }

    /**
     * Add places around user to the map
     */
    private void addPlacesAroundUserToTheMap() {
        if(placesAroundUser != null) {
            clearTheMap();
            for(Place place: placesAroundUser) {
                addPlaceMarkerToMap(place);
            }
        }
    }

    /**
     * Clear the map
     */
    private void clearTheMap() {
        mMap.clear();
        markers.clear();
    }

    /**
     * Mark place around user by a marker in the map
     * @param position
     * Position of the place
     */
    private void addPlaceMarkerToMap(LatLng position) {
        MarkerOptions markerOptions =
                new MarkerOptions().position(position);
        markerOptions.icon(placeMapMarker());
        mMap.addMarker(markerOptions);
    }

    /**
     * Mark place around user by a marker in the map
     * Add the marker to the list of the markers
     * @param place
     * Place to mark
     * @see Place
     */
    private void addPlaceMarkerToMap(Place place) {
        MarkerOptions markerOptions =
                new MarkerOptions().position(place.getPosition());
        markerOptions.icon(placeMapMarker());
        Marker marker = mMap.addMarker(markerOptions);
        markers.put(marker, place);
    }

    /**
     * Return the icon that corresponds to current travel mode
     * @return icon of current travel mode
     */
    private BitmapDescriptor placeMapMarker() {
        switch (currentTravelMode) {
            case DRIVING:
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_car_36dp);
            case TRANSIT:
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_transit_36dp);
            case WALKING:
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_walking_36dp);
            default:
                return null;
        }
    }
}