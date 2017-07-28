package com.sillaps.evaluationapp;

import android.app.Application;

import com.sillaps.evaluationapp.utilities.Fonts;

/**
 * App is the application class that maintain global app state
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the fonts
        Fonts.init(getApplicationContext());
    }
}
