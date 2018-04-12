package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_selection);

        final String[] labels = getResources().getStringArray(R.array.labels);
        final int[] imageIds = {R.drawable.ic_local_dining_black_24dp,
                R.drawable.ic_fitness_center_black_24dp,
                R.drawable.ic_local_movies_black_24dp,
                R.drawable.ic_local_play_black_24dp,
                R.drawable.ic_golf_course_black_24dp};
        final List<ActivityListItem> activityListItems = new ArrayList<>();

        Log.v("myapp", "ActivitySelectionActivity is called");

        final Bundle previousBundle = getIntent().getExtras();


        for (int i = 0; i < labels.length; i++) {
            final String label = labels[i];
            VectorDrawable vectorDrawable = null;
            if (i < imageIds.length) {
                vectorDrawable = (VectorDrawable) getResources().getDrawable(imageIds[i], null);
            }

            final ActivityListItem activityListItem = new ActivityListItem(vectorDrawable, label);
            activityListItems.add(activityListItem);
        }

        final ActivityListItemAdapter adapter = new ActivityListItemAdapter(this, 0, activityListItems);
        final ListView listView = (ListView) findViewById(R.id.ActivityListView);
        listView.setAdapter(adapter);

        final String[] activityType = getResources().getStringArray(R.array.label_names);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent activitySelectedIntent = new Intent();
                final Bundle activitySelectedBundle = new Bundle();
                activitySelectedIntent.setClass(ActivitySelectionActivity.this, MapFiltersActivity.class);
                activitySelectedBundle.putString("activity", activityType[position]);
                activitySelectedIntent.putExtras(activitySelectedBundle);
                activitySelectedIntent.putExtras(previousBundle);

                startActivity(activitySelectedIntent);
                finish();
            }
        });
    }
}
