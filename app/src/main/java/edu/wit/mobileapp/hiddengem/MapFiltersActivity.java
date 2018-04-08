package edu.wit.mobileapp.hiddengem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by ollilaj on 3/25/2018.
 */

public class MapFiltersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_filters);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final SeekBar priceSeekBar = (SeekBar) findViewById(R.id.price_seekbar);
        final SeekBar distanceSeekBar = (SeekBar) findViewById(R.id.distance_seekbar);
        final SeekBar ratingsSeekBar = (SeekBar) findViewById(R.id.ratings_seekbar);

        navigationView.setNavigationItemSelectedListener(this);
        priceSeekBar.setOnSeekBarChangeListener(seekBarListener);
        distanceSeekBar.setOnSeekBarChangeListener(seekBarListener);
        ratingsSeekBar.setOnSeekBarChangeListener(seekBarListener);

        setSeekBarClickListener(priceSeekBar);
        setSeekBarClickListener(distanceSeekBar);
        setSeekBarClickListener(ratingsSeekBar);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                final int priceValue = priceSeekBar.getProgress();
                final int distanceValue = distanceSeekBar.getProgress();
                final int ratingsValue = ratingsSeekBar.getProgress();

                // Make request to server here
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Sets the increments for each seekBar
    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == R.id.price_seekbar || seekBar.getId() == R.id.ratings_seekbar) {
                final int inBetween = progress % 25;

                if (inBetween > 12.5) {
                    seekBar.setProgress(progress - inBetween + 25);
                } else {
                    seekBar.setProgress(progress - inBetween);
                }
            } else if (seekBar.getId() == R.id.distance_seekbar) {
                int inBetween = progress % 5;

                if (inBetween > 2.5) {
                    seekBar.setProgress(progress - inBetween + 5);
                } else {
                    seekBar.setProgress(progress - inBetween);
                }
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };


    // Prevents the click event on the navigation view from firing if the click was performed on the seekBar
    private void setSeekBarClickListener(SeekBar seekBar) {
        seekBar.setOnTouchListener(new NavigationView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow Drawer to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle seekbar touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
