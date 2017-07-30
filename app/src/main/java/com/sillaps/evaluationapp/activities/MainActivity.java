package com.sillaps.evaluationapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.SupportMapFragment;
import com.sillaps.evaluationapp.R;
import com.sillaps.evaluationapp.elements.LocationDestinationLayout;
import com.sillaps.evaluationapp.elements.MainActivityToolbar;
import com.sillaps.evaluationapp.elements.RouteInformationsLayout;
import com.sillaps.evaluationapp.map.AppMap;
import com.sillaps.evaluationapp.utilities.InternetConnection;
import com.sillaps.evaluationapp.utilities.Location;

/**
 * MainActivity represents the application's starting activity
 * @author ChakrounAnas
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivityTAG";

    /**
     * FrameLayout that contain the map fragment
     * @see FrameLayout
     * @see MainActivity#mapFragment
     */
    private FrameLayout mapFrameLayout;

    /**
     * The map fragment
     */
    private SupportMapFragment mapFragment;

    /**
     * Linear layout showed when the there is no internet connection
     * @see LinearLayout
     */
    private LinearLayout noInternetConnectionLayout;

    /**
     * Linear layout showed when the location services are not turned on
     * @see LinearLayout
     */
    private LinearLayout locationOffLayout;

    /**
     * The application map
     * @see AppMap
     */
    private AppMap appMap;

    /**
     * Responsible class of the mainActivityToolbar
     * @see MainActivityToolbar
     */
    private MainActivityToolbar mainActivityToolbar;

    /**
     * Layout that display current location address and picked location name
     * @see LocationDestinationLayout
     */
    private LocationDestinationLayout locationDestinationLayout;

    /**
     * Layout that display informations of a route
     */
    private RouteInformationsLayout routeInfosLayout;

    /**
     * Async task that check for the connection internet and take some actions based on that
     * @see CheckInternetConnectionAsyncTask
     */
    private CheckInternetConnectionAsyncTask checkInternetConnectionAsyncTask;

    /**
     * Location defines methods related to location services
     * @see Location
     */
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting root container of the activity layout
        RelativeLayout mainActivityContainer = findViewById(R.id.main_activity_container);

        // Getting the mainActivityToolbar
        Toolbar mainActivityToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainActivityToolbar);

        // Setting mainActivityToolbar content
        View mainActivityActionBarView
                = getLayoutInflater().inflate(R.layout.toolbar_content, mainActivityContainer,  false);
        getSupportActionBar().setCustomView(mainActivityActionBarView);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // Instantiating activity mainActivityToolbar class
        this.mainActivityToolbar = new MainActivityToolbar(this);

        // Getting bottom navigation and setting a listener on it's items
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                return true;
                            case R.id.navigation_discover:
                                return true;
                            case R.id.navigation_calculate_fee:
                                return true;
                            case R.id.navigation_profile:
                                return true;
                        }
                        return false;
                    }
                }
        );

        // Getting the map frame layout
        mapFrameLayout = findViewById(R.id.map_frame_layout);
        // Getting the map fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Instantiating CheckInternetConnectionAsyncTask
        checkInternetConnectionAsyncTask = new CheckInternetConnectionAsyncTask();

        // Instantiating LocationDestinationLayout object
        locationDestinationLayout = new LocationDestinationLayout(this);

        // Instantiating RouteInformationsLayout object
        routeInfosLayout = new RouteInformationsLayout(this);

        // Getting no internet connection layout
        noInternetConnectionLayout = findViewById(R.id.no_internet_connection_layout);
        // Getting location services off layout
        locationOffLayout = findViewById(R.id.location_off_layout);

        /*
            Getting the retry button belonging to no internet connection layout and setting a
            listener on it
          */
        Button noInternetConnectionRetryButton = findViewById(R.id.retry_button);
        noInternetConnectionRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If current checkInternetConnectionAsyncTask is not running then start it
                if(checkInternetConnectionAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
                    checkInternetConnectionAsyncTask.execute();
                }
            }
        });

        /*
            Getting turn on location button belonging to location off layout and setting a
            listener on it
          */
        Button turnOnLocationButton = findViewById(R.id.turn_on_location_button);
        turnOnLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send turn on location services to user
                location.sendEnableLocationRequest(MainActivity.this);
            }
        });

        // Instantiating Location object
        this.location = new Location();

        // Starting the check internet connection async task
        checkInternetConnectionAsyncTask.execute();
    }

    /**
     * Async task that check if the user is connected to internet
     * If the user is connected we verify if location services are turned on
     * if the location services are turned on we show the application's map
     * if the location services are turned off we show location off layout
     * if there is no internet connection we show no internet connection layout
     */
    private class CheckInternetConnectionAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return InternetConnection.isConnectedToInternet(MainActivity.this);
        }

        @Override
        protected void onPostExecute(Boolean connectedToInternet) {
            super.onPostExecute(connectedToInternet);
            checkInternetConnectionAsyncTask = new CheckInternetConnectionAsyncTask();
            if(connectedToInternet) {
                Log.d(TAG, "onPostExecute: Internet On");
                noInternetConnectionLayout.setVisibility(View.GONE);
                if(location.checkIfLocationIsTurnedOn(MainActivity.this)) {
                    showMap();
                } else {
                    locationOffLayout.setVisibility(View.VISIBLE);
                }
            } else {
                Log.d(TAG, "onPostExecute: Internet Off");
                noInternetConnectionLayout.setVisibility(View.VISIBLE);
                if(!location.checkIfLocationIsTurnedOn(MainActivity.this)) {
                    location.sendEnableLocationRequest(MainActivity.this);
                }
            }
        }
    }

    /**
     * Show the application map
     * @see MainActivity#appMap
     */
    private void showMap() {
        locationDestinationLayout.getCurrentPositionAndDestinationLayout().setVisibility(View.VISIBLE);
        mapFrameLayout.setVisibility(View.VISIBLE);
        appMap = new AppMap
                (MainActivity.this,
                mapFragment,
                locationDestinationLayout,
                routeInfosLayout,
                mainActivityToolbar.getTravelMode());
        mainActivityToolbar.setAppMap(appMap);
    }

    /**
     * Handling the result of a request
     * @param requestCode
     * Code of the request
     * @param resultCode
     * Status of the request
     * @param data
     * Result data
     * @see Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x1:
                // Enable location services dialog is no longer visible
                location.setShowingDialogInCurrentMoment();
                switch (resultCode) {
                    case RESULT_OK:
                        /*
                            User accept to turn on the location services so we hide the location off
                            layout and start the check internet connection async task
                          */
                        locationOffLayout.setVisibility(View.GONE);
                        checkInternetConnectionAsyncTask.execute();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            // Handle user response to access location permission request
            case 0x2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    appMap.setMapCallbacks();
                } else {
                    appMap.requestPermissions();
                }
        }
    }

    /**
     * Handling the press of back button
     */
    @Override
    public void onBackPressed() {
        if(appMap.isShowingPath()) {
            /*
                The app show actually a specific route so basically with the back press we are going
                just the get back to the initial view
              */
            appMap.getBackTheMapToTheGlobalView();
        } else {
            // Get out of the application
            super.onBackPressed();
        }
    }
}