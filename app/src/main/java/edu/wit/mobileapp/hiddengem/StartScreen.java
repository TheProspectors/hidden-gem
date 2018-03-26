package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        final TextView startScreenText = (TextView) findViewById(R.id.startscreentext);
        final ImageView logo = (ImageView) findViewById(R.id.hiddengemslogo);
        final Animation transAnimation = AnimationUtils.loadAnimation(this, R.anim.transition);

        startScreenText.startAnimation(transAnimation);
        logo.startAnimation(transAnimation);

        final Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(7000);

                    final Intent intent = new Intent(getApplicationContext(), LocationSelectionActivity.class);
                    startActivity(intent);

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.start();
    }
}
