package com.map.asaha.itinerary;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.text.TextUtils.split;

/**
 * General Utility class
 * Created by asaha on 2/26/2016.
 */
public class Utility {

    public static String generateSearchString(String location1, String location2, ArrayList<EditText> placesList) {
        StringBuilder placesString = new StringBuilder();
        placesString.append(location1).append(",").append(location2);
        for (EditText place : placesList) {
            placesString.append(",").append(place.getText());
        }
        return placesString.toString();
    }

    public static List<String> getPlacesListFromString(String places) {
        return Arrays.asList(places.split("\\s*,\\s*"));
    }
}
