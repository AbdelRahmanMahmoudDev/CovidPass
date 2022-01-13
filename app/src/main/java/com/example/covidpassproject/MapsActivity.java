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
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

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

    // User map data
    private Location mUserLocation;
    private Marker mUserMarker;
    private LatLng mUserLatLng;
    private static final int mProximityRadius = 10000;

    // Widgets
    private EditText SearchField;
    private ImageView Hospitals_Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SearchField = (EditText) findViewById(R.id.search_field);
        Hospitals_Icon = (ImageView) findViewById(R.id.icon_NearestHospitals);

        Hospitals_Icon.setOnClickListener(click -> {
            String hospital_token = "hospital";
            Object transferData[] = new Object[2];
            String URL = getURL(mUserLatLng, hospital_token);
            GetNearbyPlacesTask places_task = new GetNearbyPlacesTask();
            transferData[0] = mMap;
            transferData[1] = URL;
            places_task.execute(transferData);
            Toast.makeText(MapsActivity.this, "Searching for Nearby Hospitals...", Toast.LENGTH_SHORT).show();
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH ||
                   actionId == EditorInfo.IME_ACTION_DONE   ||
                   event.getAction() == KeyEvent.ACTION_DOWN ||
                   event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    SearchGeo();
                }
                return false;
            }
        });
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
    private String getURL(LatLng lat_lng, String token) {
        StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        builder.append("&location=" + lat_lng.latitude + "," + lat_lng.longitude);
        builder.append("&radius=" + mProximityRadius);
        builder.append("&type=" + token);

        builder.append("&key=" + getString(R.string.Google_Maps_API_Key));

        Log.d(TAG, "URL: " + builder.toString());

        return builder.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (IntroActivity.isAllPermsGranted) {
            GetCurrentDeviceLocation(ZOOM_STREETS);
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
    }

    private void MoveCamera(LatLng coordinate, float zoom) {
        Log.d(TAG, "MoveCamera: Moving camera to (Latitude: " + coordinate.latitude + " Longitude: " + coordinate.longitude + ")");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
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
                            mUserLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        } else {
                            GetCurrentDeviceLocation(zoom);
                        }
                    }
                }
            });
        }
    }

    private void SearchGeo() {
        Log.d(TAG, "SearchGeo: Searching for location");

        String search_string = SearchField.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<Address>();

        try {
            list = geocoder.getFromLocationName(search_string, 10);

            if(list != null) {
                Log.d(TAG, "SearhGeo: list has values");
                for(int i = 0; i < list.size(); i++) {
                    Address address = list.get(i);
                    MoveCameraWithMarker(new LatLng(address.getLatitude(), address.getLongitude()), ZOOM_BUILDINGS, address.getAddressLine(0));
                }
            }
            else {
                Log.d(TAG, "SearhGeo: list is null");
            }
        }
        catch(IOException e) {
            Log.d(TAG,"SearchGeo: " + e.getMessage());
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        mUserLocation = location;

        if(mUserMarker != null) {
            mUserMarker.remove();
        }

        mUserLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLatLng));
    }
}