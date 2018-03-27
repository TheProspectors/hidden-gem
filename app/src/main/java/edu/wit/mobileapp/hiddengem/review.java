package edu.wit.mobileapp.hiddengem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class review extends AppCompatActivity {

    Button likeButton;
    Button dislikeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


    }

    public void ButtonListener(){
        likeButton = findViewById(R.id.Like);
        dislikeButton = findViewById(R.id.Dislike);

        
    }
}
