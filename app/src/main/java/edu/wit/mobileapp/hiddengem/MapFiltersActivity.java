package edu.wit.mobileapp.hiddengem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Created by ollilaj on 3/25/2018.
 */

public class MapFiltersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private final String TAG = "MapFiltersActivity";
    //hidden-gems-4e29c.appspot.com
    private final String SERVER_URL = "https://hidden-gems-4e29c.appspot.com/_ah/api/hiddengemPlaces/v1/places";
    private final int INITIAL_PRICE_VALUE = 0;
    private final int INITIAL_DISTANCE_VALUE = 5000;
    private final int INITIAL_RATINGS_VALUE = 0;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RequestQueue requestQueue;
    private LatLng userLocation;
    private String activityType;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_filters);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup request queue
        // Instantiate the cache
        final Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        final Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();

        // Get location data from bundle
        final Bundle previousBundle = getIntent().getExtras();
        activityType = Objects.requireNonNull(previousBundle).getString("activity");
        final double latitude = previousBundle.getDouble("place_latitude");
        final double longitude = previousBundle.getDouble("place_longitude");
        final LatLng userPlaceLatLng = new LatLng(latitude, longitude);
        userLocation = userPlaceLatLng;

        // Load map
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // App drawer components
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final SeekBar priceSeekBar = (SeekBar) findViewById(R.id.price_seekbar);
        final SeekBar distanceSeekBar = (SeekBar) findViewById(R.id.distance_seekbar);
        final SeekBar ratingsSeekBar = (SeekBar) findViewById(R.id.ratings_seekbar);

        priceSeekBar.setProgress(INITIAL_PRICE_VALUE);
        distanceSeekBar.setProgress(INITIAL_DISTANCE_VALUE);
        ratingsSeekBar.setProgress(INITIAL_RATINGS_VALUE);

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                updateMap(userPlaceLatLng);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            firebaseUser = firebaseAuth.getCurrentUser();
                            Log.i(TAG, "Firebase User ID: " + firebaseUser.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(MapFiltersActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    @SuppressLint("ClickableViewAccessibility")
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

    private void updateMap(LatLng locationToUpdateAround){
        final SeekBar priceSeekBar = (SeekBar) findViewById(R.id.price_seekbar);
        final SeekBar distanceSeekBar = (SeekBar) findViewById(R.id.distance_seekbar);
        final SeekBar ratingsSeekBar = (SeekBar) findViewById(R.id.ratings_seekbar);

        final int priceFilter = priceSeekBar.getProgress() / 20;
        final int distanceFilter = distanceSeekBar.getProgress() * 50;
        final int ratingsFilter = ratingsSeekBar.getProgress() / 20;

        getPlaces(locationToUpdateAround, priceFilter, distanceFilter, ratingsFilter);
    }

    private void getPlaces(final LatLng latLng, final int priceFilter, final int distanceFilter, final int ratingsFilter) {
        if (firebaseUser == null) {
            Log.e(TAG, "Firebase user is set to null. Cannot make request to server");
            return;
        }

        try {
            final JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("userUid", firebaseUser.getUid());
            requestJsonObject.put("latitude", latLng.latitude);
            requestJsonObject.put("longitude", latLng.longitude);
            requestJsonObject.put("priceFilter", priceFilter);
            requestJsonObject.put("distanceFilter", distanceFilter);
            requestJsonObject.put("ratingsFilter", ratingsFilter);
            requestJsonObject.put("activityCategory", activityType);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(SERVER_URL, requestJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseJsonObject) {
                    Log.d(TAG, responseJsonObject.toString());
                    Log.d(TAG, "Success!");
                    try {
                        final JSONArray placeList = responseJsonObject.getJSONArray("placeList");
                        for (int i = 0; i < placeList.length(); i++) {
                            final String placeId = placeList.getJSONObject(i).getString("placeId");
                            addMarkerToMap(placeId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG, "Error :(");
                    Log.d(TAG, String.valueOf(volleyError.getMessage()));
                    Log.d(TAG, requestJsonObject.toString());
                    volleyError.printStackTrace();
                }
            });

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMarkerToMap(final String placeId) {
        if (googleMap != null) {
            final GeoDataClient geoDataClient = Places.getGeoDataClient(this);
            geoDataClient.getPlaceById(placeId).addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onSuccess(PlaceBufferResponse places) {
                    resetMap(googleMap);
                    for (final Place place : places) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(place.getLatLng())
                                .title(place.getName().toString()));
                        Log.i(TAG, "Place found: " + place.getName());
                        Log.i(TAG, "Place id: " + place.getId());
                    }
                    places.release();
                }
            });
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        resetMap(googleMap);
        this.googleMap = googleMap;
        updateMap(userLocation);
    }

    private void resetMap(final GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(googleMap.getMaxZoomLevel() * 0.7f));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        googleMap.addMarker(new MarkerOptions().position(userLocation));
        googleMap.setBuildingsEnabled(true);
    }
}
