package com.map.asaha.itinerary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

/**
 * Map view
 * Created by asaha on 2/24/2016.
 */
public class MapsActivity extends FragmentActivity {
    private GoogleMap mMap;
    String location_1, location_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (mMap != null) {
            setUpMap();
        }

        Intent intent = getIntent();
        // Receiving the Data
        location_1 = intent.getStringExtra("location_1");
        location_2 = intent.getStringExtra("location_2");
        Button btnClose = (Button) findViewById(R.id.btnClose);
        try {
            onSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Binding Click event to Button
       /* btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.layout.select_places, menu);
        return true;
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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

    public void onSearch() throws IOException {
//        setContentView(R.layout.maps_view);

        if (!location_1.equals("") && !location_2.equals("")) {
            LatLng latLng1 = MapUtility.getLatLngFromLocationName(this, location_1);
            mMap.addMarker(new MarkerOptions().position(latLng1).title(location_1));

            LatLng latLng2 = MapUtility.getLatLngFromLocationName(this, location_2);
            mMap.addMarker(new MarkerOptions().position(latLng2).title(location_2));

            MapUtility.drawPath(mMap, latLng1, latLng2);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 10));
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (mMap != null)
            setUpMap();
    }

}
