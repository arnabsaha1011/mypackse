package com.map.asaha.itinerary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

/**
 * Map view
 * Created by asaha on 2/24/2016.
 */
public class MapsActivity extends FragmentActivity {
    private GoogleMap googleMap;
    private List<String> placesList = null;
    String places, clear_map = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2)).getMap();
        }
        if (googleMap != null) {
            setUpMap();
        }

        Intent intent = getIntent();
        // Receiving the Data
        places = intent.getStringExtra("places");
        placesList = Utility.getPlacesListFromString(places);
        clear_map = intent.getStringExtra("clear_map") == null ? "false" : intent.getStringExtra("clear_map");
        try {
            onSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setUpMap() {
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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
        googleMap.setMyLocationEnabled(true);
    }

    public void onSearch() throws IOException {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Marker marker = null;
        if (!placesList.get(0).equals("") && !placesList.get(1).equals("")) {
            LatLng place0 = MapUtility.getLatLngFromLocationName(this, placesList.get(0));
            LatLng place1 = MapUtility.getLatLngFromLocationName(this, placesList.get(1));

            MapUtility mapUtility = new MapUtility();

            // draw the first marker
            marker = mapUtility.drawMarker(googleMap, place0, placesList.get(0));
            builder.include(marker.getPosition());

            marker = mapUtility.drawMarker(googleMap, place1, placesList.get(1));
            builder.include(marker.getPosition());

            mapUtility.drawPath(this, googleMap, place0, place1);

            for (int i = 2; i < placesList.size(); i++) {
                place0 = place1;
                place1 = MapUtility.getLatLngFromLocationName(this, placesList.get(i));
                marker = mapUtility.drawMarker(googleMap, place1, placesList.get(i));
                builder.include(marker.getPosition());
                mapUtility.drawPath(this, googleMap, place0, place1);
            }
        }

        LatLngBounds bounds = builder.build();
        int padding = 2; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10);
        googleMap.moveCamera(cameraUpdate);
    }

    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if (googleMap != null)
            setUpMap();
    }

    public void onBack(View view) {
        finish();
    }
}
