package com.example.covidpassproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetGooglePlaceURL{
    public String ReadURL(String PlaceURL) throws IOException {
        String Data = "";
        InputStream inputStream = null;
        HttpURLConnection http_connection = null;

        try{
            URL url = new URL(PlaceURL);
            http_connection = (HttpURLConnection) url.openConnection();
            http_connection.connect();

            inputStream = http_connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            Data = buffer.toString();
            reader.close();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            http_connection.disconnect();
        }

        return Data;
    }
}
