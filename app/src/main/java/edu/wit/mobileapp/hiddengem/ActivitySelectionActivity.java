package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        String[] labels = getResources().getStringArray(R.array.labels);
        int[] imageIds = {R.drawable.ic_local_dining_black_24dp,
            R.drawable.ic_fitness_center_black_24dp,
            R.drawable.ic_local_movies_black_24dp,
            R.drawable.ic_local_play_black_24dp};

        final Bundle bundle = this.getIntent().getExtras();

        List<ActivityListItem> list = new ArrayList<>();

        for (int i = 0; i < labels.length; i++){
            ActivityListItem item = new ActivityListItem();
            item.label = labels[i];
            if (i < imageIds.length){
                item.image = (VectorDrawable) getResources().getDrawable(imageIds[i], null);
            }
            list.add(item);
        }

        ActivityListItemAdapter adapter;
        adapter = new ActivityListItemAdapter(this, 0, list);

        ListView listView = (ListView)findViewById(R.id.ActivityListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent activitySelectedIntent = new Intent();

                bundle.putInt("selectedActivity", position);
                activitySelectedIntent.putExtras(bundle);
                Intent intent = new Intent(getApplicationContext(), MapFiltersActivity.class);
                startActivity(intent);
            }
        });
    }
}
