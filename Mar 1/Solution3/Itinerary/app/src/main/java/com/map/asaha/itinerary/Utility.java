package com.map.asaha.itinerary;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static double calcTime(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance * 2;
    }

    public static List<String> getPlacesListFromString(String places) {
        return Arrays.asList(places.split("\\s*,\\s*"));
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String h = hour <= 9 ? "0" + hour : String.valueOf(hour);
        int minute = c.get(Calendar.MINUTE);
        String m = minute <= 9 ? "0" + hour : String.valueOf(minute);

        return h + ":" + m;
    }

    public static String generateTimeList(ArrayList<Button> buttonsList) {
        StringBuilder timeString = new StringBuilder();
        for (Button button : buttonsList) {
            timeString.append(button.getText()).append(",");
        }
        timeString.deleteCharAt(timeString.length() - 1);
        return timeString.toString();
    }

    /**
     *
     * @param startTime
     * @param drivingTime
     * @param stayTime
     * @return Reach at hh:mm Start at hh:mm
     */
    public static String getTimeFormattedString(String startTime, double drivingTime, String stayTime) {
        String[] previousStartTimeStrings = getSplittedTime(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(previousStartTimeStrings[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(previousStartTimeStrings[0]));

        //add driving time to get reach time
        calendar.add(Calendar.MINUTE, ((int) drivingTime));

        StringBuilder returnString = new StringBuilder();
        returnString.append("Reach at ").append(getFormattedTime(calendar));

        //calculate start time from the place
        String[] stayTimeStrings = getSplittedTime(stayTime);
        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(stayTimeStrings[0]));
        calendar.add(Calendar.MINUTE, Integer.parseInt(stayTimeStrings[1]));
        returnString.append(" ").append("Start at ").append(getFormattedTime(calendar));
        return returnString.toString();
    }

    /**
     *
     * @param calendar
     * @return hours:minutes
     */
    private static String getFormattedTime(Calendar calendar) {
        int hours = calendar.getTime().getHours();
        int minute = calendar.getTime().getMinutes();
        StringBuilder formattedTime = new StringBuilder();
        formattedTime = hours < 9 ? formattedTime.append("0").append(hours) : formattedTime.append(hours);
        formattedTime.append(":");
        formattedTime = minute < 9 ? formattedTime.append("0").append(minute) : formattedTime.append(minute);
        return formattedTime.toString();
    }

    @NonNull
    private static String[] getSplittedTime(String startTime) {
        return startTime.split(":");
    }
}
