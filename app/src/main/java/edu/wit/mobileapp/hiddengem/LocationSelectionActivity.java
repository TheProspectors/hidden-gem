package edu.wit.mobileapp.hiddengem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationSelectionActivity extends AppCompatActivity implements PlaceSelectionListener {
    final int ACCESS_LOCATION_REQUEST_CODE = 100;

    Place selectedPlace = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        final Button autocompleteClearButton = autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button);
        final Button goButton = findViewById(R.id.goButton);
        final Button useCurrentLocationButton = findViewById(R.id.currentlocation);

        // Register a listener to receive callbacks when a place has been selected
        // and register a listener to receive callbacks when a selected place is cleared
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPlaceSelected(null);
                    }
                });

        // Check for and request location permissions
        useCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(LocationSelectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    final String[] requestingPermissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(LocationSelectionActivity.this, requestingPermissions, ACCESS_LOCATION_REQUEST_CODE);
                }
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (selectedPlace != null) {
                    navigateToNextActivity(selectedPlace);
                }
            }
        });
    }

    @Override
    // If permissions for location are granted, set the current place to the user's current location
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectCurrentPlace();
                } else {
                    findViewById(R.id.currentlocation).setEnabled(false);
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    // Finds the user's most likely location
    // Only call if user has given permission for fine location
    private void selectCurrentPlace() {
        final PlaceDetectionClient placeDetectionClient = Places.getPlaceDetectionClient(this);
        final Task<PlaceLikelihoodBufferResponse> currentPlace = placeDetectionClient.getCurrentPlace(null);
        currentPlace.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                final PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                PlaceLikelihood bestLikelihood = null;
                for (final PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i("PLACE", String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                    if (bestLikelihood == null || placeLikelihood.getLikelihood() > bestLikelihood.getLikelihood()) {
                        bestLikelihood = placeLikelihood;
                    }
                }
                onPlaceSelected(bestLikelihood.getPlace());
            }
        });
    }

    private void navigateToNextActivity(final Place place) {
        final Intent locationData = new Intent();
        locationData.setClass(LocationSelectionActivity.this, null); //Replace null with proper class.

        final Bundle bundle = new Bundle();
        if (place != null) {
            bundle.putString("place", place.getAddress().toString());
        }

        locationData.putExtras(bundle);
        startActivity(locationData);
        finish();
    }

    @Override
    public void onPlaceSelected(final Place place) {
        selectedPlace = place;
        findViewById(R.id.goButton).setEnabled(selectedPlace != null);
    }

    @Override
    public void onError(Status status) {
        Log.e("PLACES ERROR", status.getStatusMessage());
    }
}
