package com.sillaps.evaluationapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Location class defines methods related to location services.
 * @author ChakrounAnas
 * @version 1.0
 */

public class Location {

    private static final String TAG = "LocationServiceTAG";

    /**
     * Showing activate location services at the current moment
     */
    private   boolean showingDialogInCurrentMoment;


    public void setShowingDialogInCurrentMoment() {
        this.showingDialogInCurrentMoment = false;
    }

    /**
     * Send a request to the user in order to activate the location services
     * The response of the user is handled in the activity on result from which the request is sent
     * @param context
     * Context from which this method is called
     * @see Context
     */
    public  void sendEnableLocationRequest(final Context context) {
        Log.d(TAG, "showingDialogInCurrentMoment: " + showingDialogInCurrentMoment);
        if(!showingDialogInCurrentMoment) {
            final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            final LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    googleApiClient.disconnect();
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult((Activity) context, 0x1);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d(TAG, "onResult: " + e.getMessage());
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
        }
    }

    /**
     * Check if location services are turned on
     * @param context
     * Context from which this method is called
     * @return whether if locations services are turned on or not
     */
    public  boolean checkIfLocationIsTurnedOn(Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}