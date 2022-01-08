package com.example.covidpassproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.covidpassproject.databinding.ActivityMapsBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.concurrent.Executor;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient mLocProv;
    private PlacesClient mPlacesClient;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private CameraPosition mCamPos;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCamPos = savedInstanceState.getParcelable(KEY_LOCATION);
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        mPlacesClient = Places.createClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (IntroActivity.isAllPermsGranted) {
            GetCurrentDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        //LatLng alexandria = new LatLng(31.2001, 29.9187);
        //mMap.addMarker(new MarkerOptions().position(alexandria).title("Marker in Alexandria"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(alexandria));
    }

    private void MoveCamera(LatLng coordinate, float zoom) {
        Log.d(TAG, "MoveCamera: Moving camera to (Latitude: " + coordinate.latitude + " Longitude: " + coordinate.longitude + ")");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
    }

    private void GetCurrentDeviceLocation() {
        Log.d(TAG, "GetCurrentDeviceLocation: Attempting to get device location");

        mLocProv = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(IntroActivity.isAllPermsGranted) {
                Task location_task = mLocProv.getLastLocation();

                location_task.addOnSuccessListener(success -> {
                    Location current_location = (Location) location_task.getResult();
                    if(current_location != null) {
                        MoveCamera(new LatLng(current_location.getLatitude(), current_location.getLongitude()), 15.0f);
                    }
                    else {
                        Log.d(TAG, "GetCurrentDeviceLocation: Location is null");
                    }
                });

                location_task.addOnFailureListener(failure -> {
                    Log.d(TAG, "GetCurrentDeviceLocation: Failed to get current location");
                    Toast.makeText(MapsActivity.this, "Could not retrieve current location", Toast.LENGTH_SHORT).show();
                });
            }
            else {
                Log.d(TAG, "GetCurrentDeviceLocation: Location permissions are not granted!");
            }
        }
        catch(SecurityException e) {
            Log.d(TAG, "GetCurrentDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

}