package com.example.covidpassproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapsFragment extends Fragment implements LocationListener {

    // Logging tag
    private static final String TAG = "GoogleMapsFragment";

    // Google Maps API class for getting user location
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // Google Maps API class for a map
    GoogleMap mMap;

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

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            Hospitals_Icon.setOnClickListener(click -> {
                String hospital_token = "hospital";
                Object transferData[] = new Object[2];
                String URL = getURL(mUserLatLng, hospital_token);
                GetNearbyPlacesTask places_task = new GetNearbyPlacesTask();
                transferData[0] = mMap;
                transferData[1] = URL;
                places_task.execute(transferData);
                Toast.makeText(getActivity(), "Searching for Nearby Hospitals...", Toast.LENGTH_SHORT).show();
            });

            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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

            if(IntroActivity.isLocGranted && IntroActivity.isInternetGranted) {
                GetCurrentDeviceLocation(ZOOM_STREETS);
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            else {
                String perm_meesage = "Required permissions for map functionality are not granted!";
                Toast.makeText(getActivity(), perm_meesage, Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        SearchField = (EditText) view.findViewById(R.id.search_field);
        Hospitals_Icon = (ImageView) view.findViewById(R.id.icon_NearestHospitals);
    }

    private String getURL(LatLng lat_lng, String token) {
        StringBuilder builder = null;
        if(IntroActivity.isLocGranted && IntroActivity.isInternetGranted) {
            builder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            builder.append("&location=" + lat_lng.latitude + "," + lat_lng.longitude);
            builder.append("&radius=" + mProximityRadius);
            builder.append("&type=" + token);

            builder.append("&key=" + getString(R.string.Google_Maps_API_Key));

            Log.d(TAG, "URL: " + builder.toString());
        }
        else {
            String perm_meesage = "Required permissions for map functionality are not granted!";
            builder = new StringBuilder(perm_meesage);
            Toast.makeText(getActivity(), perm_meesage, Toast.LENGTH_SHORT).show();
        }

        return builder.toString();
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
        if(IntroActivity.isLocGranted && IntroActivity.isInternetGranted) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        else {
            String perm_meesage = "Required permissions for map functionality are not granted!";
            Toast.makeText(getActivity(), perm_meesage, Toast.LENGTH_SHORT).show();
        }
    }

    private void SearchGeo() {
        Log.d(TAG, "SearchGeo: Searching for location");

        String search_string = SearchField.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());

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