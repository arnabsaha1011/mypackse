package com.map.asaha.itinerary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

public class SelectPlacesActivity extends Activity {
    boolean clearMap = false;
    ArrayList<EditText> placesList;

    public SelectPlacesActivity() {
        placesList = new ArrayList<EditText>();
    }

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

        boolean clearMapData = clearMap;
        clearMap = false;
        //Sending data to another Activity
        mapsScreen.putExtra("places", Utility.generateSearchString(location_1, location_2, placesList));
        mapsScreen.putExtra("clear_map", String.valueOf(clearMapData));

        startActivity(mapsScreen);
    }

    public void goToCurrentLocation(View view) {

    }

    public void onClearMap(View view) {
        clearMap = true;
    }

    public void addLocation(View view) {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.places_layout);

        final LinearLayout linearLayoutContainer = new LinearLayout(this);
        LinearLayout.LayoutParams linearLayoutContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutContainer.setLayoutParams(linearLayoutContainerParams);

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        editTextParams.leftMargin = 10;
        editTextParams.weight = 1;
        final EditText myEditText = new EditText(this);
        myEditText.setLayoutParams(editTextParams);
        myEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        myEditText.setHint("Choose destination...");

        Button closeButton = new Button(this);
        LinearLayout.LayoutParams closeButtonParams = new LinearLayout.LayoutParams(165, LinearLayout.LayoutParams.WRAP_CONTENT);
        closeButtonParams.leftMargin = 5;
        closeButtonParams.rightMargin = 5;
        closeButton.setLayoutParams(closeButtonParams);
        closeButton.setBackground(getResources().getDrawable(R.drawable.places_ic_clear));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(linearLayoutContainer);
                placesList.remove(myEditText);
            }
        });

        linearLayoutContainer.addView(myEditText);
        linearLayoutContainer.addView(closeButton);

        linearLayout.addView(linearLayoutContainer);
        placesList.add(myEditText);
    }
}
