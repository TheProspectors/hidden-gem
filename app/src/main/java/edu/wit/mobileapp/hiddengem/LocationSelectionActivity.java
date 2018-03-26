package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LocationSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        final Button goButton = findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final EditText cityText = findViewById(R.id.cityText);;
                final EditText zipCodeText = findViewById(R.id.zipcodeText);

                final Intent locationData = new Intent();
                locationData.setClass(LocationSelectionActivity.this, null); //Replace null with proper class.

                final Bundle bundle = new Bundle();
                bundle.putString(cityText.toString(), zipCodeText.toString());

                locationData.putExtras(bundle);
                startActivity(locationData);
            }
        });
    }
}
