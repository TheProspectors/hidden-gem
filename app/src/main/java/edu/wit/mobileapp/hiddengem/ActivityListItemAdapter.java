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

    private LayoutInflater mInflator;

    public ActivityListItemAdapter(Context context, int rid, List<ActivityListItem> list){
        super(context, rid, list);
        mInflator =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

        //Retrieve Data
        ActivityListItem item = getItem(position);

        //Use layout file to generate View
        View view = mInflator.inflate(R.layout.activity_list_item, null);

        //Set Image
        ImageView image;
        image = view.findViewById(R.id.image);
        if (item != null) {
            image.setImageDrawable(item.image);
        }

        //Set label
        TextView name;
        name = view.findViewById(R.id.label);
        if (item != null) {
            name.setText(item.label);
        }

        return view;
    }
}
