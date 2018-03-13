package com.example.mohsin.tsgarbagemonitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.LENGTH_LONG;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be use
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
        String url = "http://tsgarbagemonitor.000webhostapp.com/api/getall";
        String result = "";
        HttpGetClass getRequest = new HttpGetClass();
        try {
            result = getRequest.execute(url).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,result,Toast.LENGTH_LONG).show();
        try {
            JSONArray jsonArray = new JSONArray(result);
            int len = jsonArray.length();
            for(int i =0;i<len;i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LatLng pos = new LatLng(Double.parseDouble(jsonObject.get("x").toString()),Double.parseDouble(jsonObject.get("y").toString()));
                Marker m = mMap.addMarker(new MarkerOptions().position(pos).title("Dustbin Number :"+jsonObject.get("binId").toString()));
                m.setTag(jsonObject.get("binId").toString());

                if (jsonObject.get("status").equals("FULL")) {
                    m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.full));
                    m.setSnippet("Last Empty on  :"+jsonObject.get("lastEmpty").toString());

                }
                else {
                    m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.empty));
                    m.setSnippet("Last Full on  :"+jsonObject.get("lastFull").toString());
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,27.0f));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //-Intent intent = new Intent(this,MapsActivity.class);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String tag = marker.getTag().toString();
        Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
        return false;
    }
}
