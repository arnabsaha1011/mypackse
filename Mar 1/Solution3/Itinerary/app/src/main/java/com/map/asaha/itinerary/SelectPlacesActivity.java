package com.map.asaha.itinerary;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SelectPlacesActivity extends FragmentActivity {
    boolean clearMap = false;
    ArrayList<EditText> placesList;
    static Button startingTimeButton;
    ArrayList<Button> timeButtonList;

    public SelectPlacesActivity() {
        placesList = new ArrayList<EditText>();
        timeButtonList = new ArrayList<Button>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_places);
        startingTimeButton = (Button) findViewById(R.id.startingTime);
        timeButtonList.add(startingTimeButton);
        final Button firstIntervalButton = (Button) findViewById(R.id.interval1);
        firstIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v, firstIntervalButton);
            }
        });
        timeButtonList.add(firstIntervalButton);
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
        mapsScreen.putExtra("times", Utility.generateTimeList(timeButtonList));

        startActivity(mapsScreen);
    }

    public void goToCurrentLocation(View view) {

    }

    public void onClearMap(View view) {
        clearMap = true;
    }

    public void addLocation(View view) {
        //calculate time intervals
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.places_layout);

        final LinearLayout linearLayoutContainer = new LinearLayout(this);
        LinearLayout.LayoutParams linearLayoutContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutContainer.setLayoutParams(linearLayoutContainerParams);

        final LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.leftMargin = 15;
        final Button timeButton = new Button(this);
        timeButton.setLayoutParams(buttonParams);
        timeButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        timeButton.setText(R.string.pick_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              showTimePickerDialog(v, timeButton);
                                          }
                                      }
        );
        timeButtonList.add(timeButton);

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        editTextParams.leftMargin = 10;
        editTextParams.weight = 1;
        final EditText myEditText = new EditText(this);
        myEditText.setLayoutParams(editTextParams);
        myEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        myEditText.setHint("Choose destination...");

//        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
//        buttonParams.leftMargin = 15;
        Button endTimeButton = new Button(this);
        endTimeButton.setLayoutParams(buttonParams);
        endTimeButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        endTimeButton.setText(R.string.pick_time);

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

        linearLayoutContainer.addView(timeButton);
        linearLayoutContainer.addView(myEditText);
        linearLayoutContainer.addView(closeButton);

        linearLayout.addView(linearLayoutContainer);
        placesList.add(myEditText);
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialog(View view, Button button) {
        DialogFragment newFragment = new TimePickerFragment();
        ((TimePickerFragment)newFragment).setButton(button);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        int hour, minute;
        Button button;

        public void setButton(Button button) {
            this.button = button;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            if (button == null)
                return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
            else
                return new TimePickerDialog(getActivity(), this, 1, 0,
                        DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (button == null)
                button = startingTimeButton;
            button.setText(String.format("%s:%s", String.valueOf(hourOfDay < 9 ? "0" + hourOfDay : hourOfDay), String.valueOf(minute < 9 ? "0" + minute : minute)));
        }
    }
}
