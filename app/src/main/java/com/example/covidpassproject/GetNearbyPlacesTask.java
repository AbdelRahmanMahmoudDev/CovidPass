package com.example.covidpassproject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GetNearbyPlacesTask extends AsyncTask<Object, String, String> {

    private String GooglePlacesData;
    private String URL;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        URL = (String) objects[1];

        GetGooglePlaceURL result = new GetGooglePlaceURL();
        try {
            GooglePlacesData = result.ReadURL(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GooglePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        // android.os.Debug.waitForDebugger();
        List<HashMap<String, String>> places = null;
        DataParser data_parser = new DataParser();
        places = data_parser.Parse(s);

        Log.d("Goddamn places list", places.toString());

        DisplayNearbyPlaces(places);
    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearby_places_list) {
        for(int i = 0; i < nearby_places_list.size(); i++) {
            MarkerOptions options = new MarkerOptions();

            HashMap<String, String> places = nearby_places_list.get(i);
            String name_of_place = places.get("place_name");
            String vicinity = places.get("vicinity");
            double latitude = Double.parseDouble(Objects.requireNonNull(places.get("lat")));
            double longitude = Double.parseDouble(Objects.requireNonNull(places.get("lng")));

            options.position(new LatLng(latitude, longitude));
            options.title(name_of_place + " : " + vicinity);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        }
    }
}
