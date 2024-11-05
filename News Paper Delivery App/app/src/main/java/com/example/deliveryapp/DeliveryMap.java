package com.example.deliveryapp;

import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.deliveryapp.databinding.ActivityDeliveryMapBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeliveryMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDeliveryMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeliveryMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        new ReadJSONFeedTask().execute("http://192.168.43.157/DeliveryAndroidApp/deliveryplan.json");

    }

    public String ReadJSONFeed(String address) {
        URL url = null;
        try {
            url = new URL(address);
        }
        catch(MalformedURLException e){
            e.printStackTrace();
            Log.d("error", "populateListviewFromDB: "+ e.getMessage());
        };
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d("error", "populateListviewFromDB: "+ e.getMessage());
        }
        try {
            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) !=null){
                stringBuilder.append(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d("error", "populateListviewFromDB: "+ e.getMessage());
        }
        finally{
            urlConnection.disconnect();
        }

        return stringBuilder.toString();

    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... urls){
            return ReadJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            LatLng[] deliveryPoints;
            try {
                JSONArray jsonArray = new JSONArray(result);
                deliveryPoints = new LatLng[jsonArray.length()];
                Log.i("JSON", "Number of surveys in feed: " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    double lat = Double.valueOf(jsonObject.getString("latitude"));
                    double lng = Double.valueOf(jsonObject.getString("longitude"));
                    deliveryPoints[i] = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(deliveryPoints[i]));
                }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deliveryPoints[0], 16f));
        }
            catch (Exception e){
                e.printStackTrace();
                Log.d("error", "populateListviewFromDB: "+ e.getMessage());
            }
        }
    }
}