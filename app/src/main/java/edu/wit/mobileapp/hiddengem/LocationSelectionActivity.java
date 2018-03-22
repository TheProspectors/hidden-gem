package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LocationSelectionActivity extends AppCompatActivity
{

    EditText cityText;
    EditText zipcodeText;
    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        ButtonListener();

    }

    public void ButtonListener()
    {
        goButton = findViewById(R.id.goButton);

        goButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cityText = findViewById(R.id.cityText);
                zipcodeText = findViewById(R.id.zipcodeText);

                Intent locationData = new Intent();
                locationData.setClass(LocationSelectionActivity.this,
                        ActivitySelectionActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString(cityText.toString(), zipcodeText.toString());

                locationData.putExtras(bundle);

                startActivity(locationData);
            }
        });
    }
}
