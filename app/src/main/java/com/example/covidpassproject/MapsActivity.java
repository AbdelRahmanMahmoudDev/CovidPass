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

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
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
    private FusedLocationProviderClient mFusedLocationProviderClient;


    // Camera zoom levels
    private static final float ZOOM_WORLD = 1.0f;
    private static final float ZOOM_CONTINENT = 5.0f;
    private static final float ZOOM_CITY = 10.0f;
    private static final float ZOOM_STREETS = 15.0f;
    private static final float ZOOM_BUILDINGS = 20.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
            GetCurrentDeviceLocation(ZOOM_BUILDINGS);
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
        LatLng alexandria = new LatLng(31.2001, 29.9187);
        mMap.addMarker(new MarkerOptions().position(alexandria).title("Marker in Alexandria"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(alexandria));
    }

    private void MoveCamera(LatLng coordinate, float zoom) {
        Log.d(TAG, "MoveCamera: Moving camera to (Latitude: " + coordinate.latitude + " Longitude: " + coordinate.longitude + ")");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));

        MarkerOptions options = new MarkerOptions()
                .position((coordinate))
                .title("You");

        mMap.addMarker(options);
    }

    private void MoveCameraWithMarker(LatLng coordinate, float zoom, String marker_title) {
        Log.d(TAG, "MoveCamera: Moving camera to (Latitude: " + coordinate.latitude + " Longitude: " + coordinate.longitude + ")");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));

        MarkerOptions options = new MarkerOptions()
                .position((coordinate))
                .title(marker_title);

        mMap.addMarker(options);
    }

    private void GetCurrentDeviceLocation(float zoom) {
        Log.d(TAG, "GetCurrentDeviceLocation: Attempting to get device location");
        if (IntroActivity.isAllPermsGranted) {
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
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()) {
                        Location location = task.getResult();
                        if(location != null) {
                            Log.d(TAG, "GetCurrentDeviceLocation: " +  "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
                            MoveCamera(new LatLng(location.getLatitude(), location.getLongitude()), zoom);
                        } else {
                            GetCurrentDeviceLocation(zoom);
                        }
                    }
                }
            });
        }
    }

}