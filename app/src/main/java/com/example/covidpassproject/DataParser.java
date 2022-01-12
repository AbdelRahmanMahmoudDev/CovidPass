package com.example.covidpassproject;

import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> GetPlace(JSONObject googlePlaceJSON) throws JSONException {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String Vicinity = "-NA-";
        String Latitude = "";
        String Longitude = "";
        String Reference = "";

        try {
            if(!googlePlaceJSON.isNull("name")) {
                NameOfPlace = googlePlaceJSON.getString("name");
            }

            if(!googlePlaceJSON.isNull("vicinity")) {
                Vicinity = googlePlaceJSON.getString("vicinity");
            }

            Latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            Longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            Reference = googlePlaceJSON.getString("reference");

            googlePlaceMap.put("place_name", NameOfPlace);
            googlePlaceMap.put("vicinity", Vicinity);
            googlePlaceMap.put("lat", Latitude);
            googlePlaceMap.put("lng", Longitude);
            googlePlaceMap.put("reference", Reference);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }

    private List<HashMap<String, String>> GetAllNearbyPlaces(JSONArray array) {
        List<HashMap<String, String>> nearbyPlacesList = new ArrayList<>();

        HashMap<String, String> NearbyPlace = null;

        for (int i = 0; i < array.length(); i++) {
            try {
                NearbyPlace = GetPlace((JSONObject) array.get(i));
                nearbyPlacesList.add(NearbyPlace);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nearbyPlacesList;
    }

    public List<HashMap<String, String>> Parse(String JSON_Data) {
        JSONArray array = null;
        JSONObject json_object;

        try {
            json_object = new JSONObject(JSON_Data);
            array = json_object.getJSONArray("results");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        assert array != null;
        return GetAllNearbyPlaces(array);
    }
}
