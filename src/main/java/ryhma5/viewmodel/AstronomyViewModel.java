package ryhma5.viewmodel;

import ryhma5.model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Vector;

public class AstronomyViewModel {

    private Vector<AstronomyEvent> events;

    public AstronomyViewModel(){
        events = new Vector<>();
    }

    public AstronomyEvent getAstronomyEvent(String latitude, String longitude, String elevation, String date, String time) {
        for (AstronomyEvent event : events) {
            AstronomyEvent.Data data = event.getData(); // Access the data object

            if (data != null &&
                    String.valueOf(data.getObserver().getLocation().getLatitude()).equals(latitude) &&
                    String.valueOf(data.getObserver().getLocation().getLongitude()).equals(longitude) &&
                    String.valueOf(data.getObserver().getLocation().getElevation()).equals(elevation) &&
                    data.getDates().getFrom().startsWith(date) && // Compare only the date part
                    time.equals(data.getTable().getRows()[0].getCells()[0].getRise())) { // Adjust this if you want a different time check
                // Event found, return it
                return event;
            }
        }

        try {
            AstronomyEvent newEvent = AstronomyAPI.fetchAstronomyEvent(latitude, longitude, elevation, date, time);

            // If the new event is not null, add it to the events vector
            if (newEvent != null) {
                events.add(newEvent);
            }
            return newEvent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Handle errors appropriately
        }
    }


}