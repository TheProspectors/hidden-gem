package edu.wit.mobileapp.hiddengem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class startScreen extends AppCompatActivity
{

    private TextView startscreentext;
    private ImageView hiddengemslogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        startscreentext = (TextView) findViewById(R.id.startscreentext);
        hiddengemslogo = (ImageView) findViewById(R.id.hiddengemslogo);

        Animation transAnimation = AnimationUtils.loadAnimation(this, R.anim.transition);

        startscreentext.startAnimation(transAnimation);
        hiddengemslogo.startAnimation(transAnimation);

        Thread timer = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(7000);

                    Intent intent = new Intent(getApplicationContext(), LocationSelectionActivity.class);
                    startActivity(intent);

                    finish();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };

        timer.start();
    }
}
