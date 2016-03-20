package com.map.asaha.itinerary;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains all the utility classes for Itinerary
 * Created by asaha on 2/21/2016.
 */
public class MapUtility {
    List<Polyline> allPolyLines;

    public MapUtility() {
        this.allPolyLines = new ArrayList<>();
    }

    public static LatLng getLatLngFromLocationName(FragmentActivity fragmentActivity, String locationName) throws IOException {
        Geocoder geocoder = new Geocoder(fragmentActivity);
        List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
        Address address = addressList.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    public double drawPath(FragmentActivity fragmentActivity, GoogleMap googleMap, LatLng latLng1, LatLng latLng2) {
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(latLng1, latLng2);

        DownloadTask downloadTask = new DownloadTask(fragmentActivity, googleMap);
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
        return Utility.calcTime(latLng1.latitude, latLng2.latitude, latLng1.longitude, latLng2.longitude);
    }

    public Marker drawMarker(GoogleMap googleMap, LatLng latLng, String time) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(time));
        marker.showInfoWindow();
        return marker;
    }


    private static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        FragmentActivity fragmentActivity;
        GoogleMap googleMap;
        private volatile String duration;

        public DownloadTask(FragmentActivity fragmentActivity, GoogleMap googleMap) {
            this.fragmentActivity = fragmentActivity;
            this.googleMap = googleMap;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(fragmentActivity, googleMap);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
            duration = parserTask.getDuration();
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception downloading", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        FragmentActivity fragmentActivity;
        GoogleMap googleMap;
        String duration = "";

        public String getDuration() {
            return duration;
        }

        public ParserTask(FragmentActivity fragmentActivity, GoogleMap googleMap) {
            this.fragmentActivity = fragmentActivity;
            this.googleMap = googleMap;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";

            if (result.size() < 1) {
                Toast.makeText(fragmentActivity.getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                System.out.println("Duration = " + duration);
            }

            //tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            allPolyLines.add(googleMap.addPolyline(lineOptions));
        }
    }

    public void clearMap(GoogleMap googleMap) {
        for (Polyline polyline : allPolyLines) {
            polyline.remove();
        }
    }

    public static int calcTimeFromPlaces(String place1, String place2) {
        return 0;
    }
}
