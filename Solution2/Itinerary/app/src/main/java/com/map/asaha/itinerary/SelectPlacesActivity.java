package com.map.asaha.itinerary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class SelectPlacesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_places);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onResume();
    }

    public void onSearch(View view) throws IOException {
        String location_1 = ((EditText) findViewById(R.id.place_1)).getText().toString();
        String location_2 = ((EditText) findViewById(R.id.place_2)).getText().toString();

        // refer to: http://www.androidhive.info/2011/08/how-to-switch-between-activities-in-android/
        Intent mapsScreen = new Intent(getApplicationContext(), MapsActivity.class);

        //Sending data to another Activity
        mapsScreen.putExtra("location_1", location_1);
        mapsScreen.putExtra("location_2", location_2);

        Log.e("n", location_1 + " , " + location_2);

        startActivity(mapsScreen);
    }

    public void goToCurrentLocation(View view) {

    }

    public void onEdit(View view) {

    }

    public void addLocation(View view) {

    }
}
