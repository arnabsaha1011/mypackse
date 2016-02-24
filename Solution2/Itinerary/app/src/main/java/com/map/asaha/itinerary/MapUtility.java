package com.map.asaha.itinerary;

import android.location.Address;
import android.location.Geocoder;
import android.view.ContextThemeWrapper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the utility classes for Itinerary
 * Created by asaha on 2/21/2016.
 */
public class MapUtility {
    public static LatLng getLatLngFromLocationName(String locationName) throws IOException {
        Geocoder geocoder = new Geocoder(new ContextThemeWrapper());
        List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
        Address address = addressList.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    public static void drawPath(GoogleMap googleMap, LatLng latLng1, LatLng latLng2) {
        googleMap.addPolyline(new PolylineOptions().add(latLng1, latLng2));
    }
}
