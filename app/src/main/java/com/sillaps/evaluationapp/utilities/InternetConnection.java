package com.sillaps.evaluationapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * InternetConnection determines the status of internet connection whether is on or off
 * @author ChakrounAnas
 * @version 1.0
 */

public class InternetConnection {

    private static final String TAG = "InternetConnectionTAG";

    /**
     * Determine if the user has access to internet or not
     * @param context
     * Context from which the method is called
     * @return has access to internet or not
     * @see Context
     */
    public static boolean isConnectedToInternet(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                httpURLConnection.setRequestProperty("User-Agent", "Android");
                httpURLConnection.setRequestProperty("Connection", "close");
                httpURLConnection.setConnectTimeout(1500);
                httpURLConnection.connect();
                return (httpURLConnection.getResponseCode() == 204 &&
                        httpURLConnection.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    /**
     * Determine if there is an active internet provider
     * @param context
     * Context from which the method is called
     * @return whether if there is an internet provider or not
     */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
