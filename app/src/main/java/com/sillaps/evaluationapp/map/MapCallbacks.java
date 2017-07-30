package com.sillaps.evaluationapp.map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * MapCallbacks handle all callbacks related to google map.
 * @author ChakrounAnas
 * @version 1.0
 */
class MapCallbacks implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapCallbacksTAG";

    /**
     * Context from which this object get instantiated
     * @see Context
     */
    private final Context context;

    /**
     * Map of the application
     * @see AppMap
     */
    private final AppMap appMap;

    /**
     * Client of google api
     * @see GoogleApiClient
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     *
     * @see FusedLocationProviderClient
     */
    private final FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback called when google services provide user location
     */
    private LocationCallback mLocationCallback;

    /**
     * MapCallbacks constructor.
     * @param context
     * Context from which this object get instantiated
     * @param appMap
     * The application map
     * @see AppMap
     * @see Context
     */
    MapCallbacks(Context context, final AppMap appMap) {
        this.context = context;
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.appMap = appMap;
        mLocationCallback = new LocationCallback() {
            /**
             * Callback called when we successfully connect to google location services <br>
             * After connecting to location services we request the user location
             * @param locationResult
             * LocationResult from which we can get user location
             * @see LocationResult
             */
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Log.d(TAG, "onLocationChanged: " + location.toString());
                LatLng currentUserPosition = new LatLng(location.getLatitude(), location.getLongitude());
                appMap.setCurrentUserPosition(currentUserPosition);
                appMap.markThePositionOfUserInTheMap();
                appMap.getAndDisplayCurrentUserPlaceAddress();
                appMap.getPlacesAroundUser();
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
        };
    }

    /**
     * Callback called when google map is ready <br>
     * First we try to get the last known location <br>
     * If we get a location then we display it in the map and then get its address and also the places
     * around it <br>
     * If we don't get a location then we communicate to google location services via the google
     * client api in order to get it
     * @param googleMap
     * Google map
     * @see GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        appMap.setMap(googleMap);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                Log.d(TAG, "onSuccess: LocationLatitude -> "
                                        + location.getLatitude() + "; LocationLongitude: "
                                        + location.getLongitude());
                                LatLng currentUserPosition
                                        = new LatLng(location.getLatitude(), location.getLongitude());
                                appMap.setCurrentUserPosition(currentUserPosition);
                                appMap.markThePositionOfUserInTheMap();
                                appMap.getAndDisplayCurrentUserPlaceAddress();
                                appMap.getPlacesAroundUser();
                            } else {
                                Log.d(TAG, "onSuccess: LocationIsNull");
                                mGoogleApiClient = new GoogleApiClient.Builder(context)
                                        .addApi(LocationServices.API)
                                        .addConnectionCallbacks(MapCallbacks.this)
                                        .addOnConnectionFailedListener(MapCallbacks.this)
                                        .build();
                                mGoogleApiClient.connect();
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.d(TAG, "onMapReady: " + e.getMessage());
        }
    }

    /**
     * Callback called when we successfully connect to google location services <br>
     * After connecting to location services we request the user location
     * @param bundle
     * Mappings from strings and keys
     * @see Bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        final LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, null);
        } catch (SecurityException e) {
            Log.d(TAG, "onConnected: " + e.getMessage());
        }
    }


    /**
     * Callback called when the connection to location services get suspended
     * @param i
     * Cause of the suspension
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    /**
     * Callback called when the connection to location services fails
     * @param connectionResult
     * Deciding what sort of error occurred and trying to resolve the error
     * @see ConnectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }
}