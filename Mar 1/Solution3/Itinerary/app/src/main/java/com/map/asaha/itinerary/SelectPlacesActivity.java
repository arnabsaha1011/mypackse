package com.map.asaha.itinerary;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SelectPlacesActivity extends FragmentActivity implements TextWatcher, View.OnFocusChangeListener {
    boolean clearMap = false;
    ArrayList<EditText> placesList;
    static Button startingTimeButton;
    ArrayList<EditText> timeTextList;
    EditText firstIntervalText;
    public static int clickCount = 0;
    public static int charCount = 0;

    public SelectPlacesActivity() {
        placesList = new ArrayList<EditText>();
        timeTextList = new ArrayList<EditText>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_places);
        startingTimeButton = (Button) findViewById(R.id.startingTime);

//        startingTimeButton.setOnClickListener(this);
//        timeTextList.add(startingTimeButton);
        firstIntervalText = (EditText) findViewById(R.id.interval1);
        firstIntervalText.setOnFocusChangeListener(this);
        firstIntervalText.addTextChangedListener(this);
        timeTextList.add(firstIntervalText);
        ((EditText) findViewById(R.id.place_1)).addTextChangedListener(this);
        findViewById(R.id.place_1).setOnFocusChangeListener(this);
        ((EditText) findViewById(R.id.place_2)).addTextChangedListener(this);
        findViewById(R.id.place_2).setOnFocusChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onResume();
    }

    public void onSearch(View view) throws IOException {
        incrementClicks();
        String location_1 = ((EditText) findViewById(R.id.place_1)).getText().toString();
        String location_2 = ((EditText) findViewById(R.id.place_2)).getText().toString();

        // refer to: http://www.androidhive.info/2011/08/how-to-switch-between-activities-in-android/
        Intent mapsScreen = new Intent(getApplicationContext(), MapsActivity.class);

        boolean clearMapData = clearMap;
        clearMap = false;
        //Sending data to another Activity
        String searchString = Utility.generateSearchString(location_1, location_2, placesList);
        String timeList = Utility.generateTimeList(startingTimeButton, timeTextList);
        mapsScreen.putExtra("places", searchString);
        mapsScreen.putExtra("clear_map", String.valueOf(clearMapData));
        mapsScreen.putExtra("times", timeList);

        startActivity(mapsScreen);
    }

    public void goToCurrentLocation(View view) {

    }

    public void onClearMap(View view) {
        clearMap = true;
    }

    public void addLocation(View view) {
        incrementClicks();
        //calculate time intervals
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.places_layout);

        final LinearLayout linearLayoutContainer = new LinearLayout(this);
        LinearLayout.LayoutParams linearLayoutContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutContainer.setLayoutParams(linearLayoutContainerParams);

        final LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.leftMargin = 15;
        final EditText timeText = new EditText(this);
        timeText.setLayoutParams(buttonParams);
        timeText.setBackgroundColor(getResources().getColor(android.R.color.white));
        timeText.setText(R.string.pick_time);
        timeText.setInputType(InputType.TYPE_CLASS_NUMBER);
        timeText.setGravity(Gravity.CENTER);
        timeText.setOnFocusChangeListener(this);
        timeText.addTextChangedListener(this);

        timeTextList.add(timeText);

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        editTextParams.leftMargin = 10;
        editTextParams.weight = 1;
        final EditText myEditText = new EditText(this);
        myEditText.setLayoutParams(editTextParams);
        myEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        myEditText.setHint("Choose destination...");
        myEditText.addTextChangedListener(this);
        myEditText.setOnFocusChangeListener(this);

//        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
//        buttonParams.leftMargin = 15;
        Button endTimeButton = new Button(this);
        endTimeButton.setLayoutParams(buttonParams);
        endTimeButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        endTimeButton.setText(R.string.pick_time);
        endTimeButton.setOnFocusChangeListener(this);

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
                incrementClicks();

            }
        });

        linearLayoutContainer.addView(timeText);
        linearLayoutContainer.addView(myEditText);
        linearLayoutContainer.addView(closeButton);

        linearLayout.addView(linearLayoutContainer);
        placesList.add(myEditText);
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        incrementClicks();

    }

    public void showTimePickerDialog(View view, Button button) {
        DialogFragment newFragment = new TimePickerFragment();
        ((TimePickerFragment) newFragment).setButton(button);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static void incrementClicks() {
        clickCount++;
        Log.d("CLICKS", "Count = " + clickCount);
    }

    public static void incrementChars() {
        charCount++;
        Log.d("CHARACTERS", "Count = " + charCount);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        incrementClicks();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        incrementChars();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

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
            incrementClicks();
            button.setText(String.format("%s:%s", String.valueOf(hourOfDay < 9 ? "0" + hourOfDay : hourOfDay), String.valueOf(minute < 9 ? "0" + minute : minute)));
        }
    }

}
