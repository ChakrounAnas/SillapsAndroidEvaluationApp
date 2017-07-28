package com.sillaps.evaluationapp.utilities;

import android.content.Context;
import android.graphics.Typeface;

import com.sillaps.evaluationapp.R;

/**
 * Fonts define the fonts used in the application
 * @author ChakrounAnas
 * @version 1.0
 */

public class Fonts {

    /**
     *
     */
    public static Typeface MERRIWEATHER_BLACK;

    /**
     * Initialize the fonts
     * @param context
     * Context from which this method is called
     * @see Context
     */
    public static void init(Context context) {
        MERRIWEATHER_BLACK = Typeface.createFromAsset(context.getAssets(),
                context.getString(R.string.merriweather_black_font));
    }
}
