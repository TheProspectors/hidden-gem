package edu.wit.mobileapp.hiddengem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class review extends AppCompatActivity implements View.OnClickListener {

    Button likeButton;
    Button dislikeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        likeButton = findViewById(R.id.Like);
        dislikeButton = findViewById(R.id.Dislike);

        likeButton.setOnClickListener(this);
        dislikeButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Like:
                //hi
                break;
            case R.id.Dislike:
                //hi
                break;
        }
    }
}
