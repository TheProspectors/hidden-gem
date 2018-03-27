package edu.wit.mobileapp.hiddengem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by poliaf on 3/7/18.
 */

public class ActivityListItemAdapter extends ArrayAdapter<ActivityListItem> {
    private final LayoutInflater mInflater;

    public ActivityListItemAdapter(final Context context, final int rid, final List<ActivityListItem> activityListItems) {
        super(context, rid, activityListItems);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    public View getView(final int position, final View convertView, final @NonNull ViewGroup parent) {
        //Retrieve Data
        final ActivityListItem activityListItem = getItem(position);

        //Use layout file to generate View
        final View view = mInflater.inflate(R.layout.activity_list_item, null);

        //Set Image
        final ImageView imageView = view.findViewById(R.id.image);
        if (activityListItem != null) {
            imageView.setImageDrawable(activityListItem.vectorDrawable);
        }

        //Set label
        final TextView nameTextView = view.findViewById(R.id.label);
        if (activityListItem != null) {
            nameTextView.setText(activityListItem.label);
        }

        return view;
    }
}
