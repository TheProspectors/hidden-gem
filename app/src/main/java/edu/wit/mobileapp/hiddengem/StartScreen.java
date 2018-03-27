package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {
    private final Handler handler = new Handler(Looper.myLooper());

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_activity);

        final TextView startScreenText = (TextView) findViewById(R.id.startscreentext);
        final ImageView logo = (ImageView) findViewById(R.id.hiddengemslogo);
        final Animation transAnimation = AnimationUtils.loadAnimation(this, R.anim.transition);

        startScreenText.startAnimation(transAnimation);
        logo.startAnimation(transAnimation);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent(getApplicationContext(), LocationSelectionActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
