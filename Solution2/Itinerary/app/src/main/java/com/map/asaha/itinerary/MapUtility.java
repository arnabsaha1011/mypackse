package com.map.asaha.itinerary;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the utility classes for Itinerary
 * Created by asaha on 2/21/2016.
 */
public class MapUtility {
    public static LatLng getLatLngFromLocationName(FragmentActivity fragmentActivity, String locationName) throws IOException {
        Geocoder geocoder = new Geocoder(fragmentActivity);
        List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
        Address address = addressList.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    /*public Barcode.GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));

            return p1;
        }
    }*/

    public static void drawPath(GoogleMap googleMap, LatLng latLng1, LatLng latLng2) {
        googleMap.addPolyline(new PolylineOptions().add(latLng1, latLng2));
    }
}
