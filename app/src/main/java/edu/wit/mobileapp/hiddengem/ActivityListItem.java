package edu.wit.mobileapp.hiddengem;

import android.graphics.drawable.VectorDrawable;

/**
 * Created by poliaf on 3/7/18.
 */

public class ActivityListItem {
    final VectorDrawable vectorDrawable;
    final String label;

    public ActivityListItem(final VectorDrawable image, final String label) {
        this.vectorDrawable = image;
        this.label = label;
    }
}
