package project.viewmodel;

import project.model.*;

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

    public AstronomyEvent getAstronomyEvent(String latitude, String longitude, String elevation, String date, String time){

        for (AstronomyEvent event : events) {
            if (event.getLatitude().equals(latitude) &&
                    event.getLongitude().equals(longitude) &&
                    event.getElevation().equals(elevation) &&
                    event.getDate().equals(date) &&
                    event.getTime().equals(time)) {
                // Event found, return it
                return event;
            }
        }
        try {
            AstronomyEvent newEvent = AstronomyAPI.fetchAstronomyEvent(latitude, longitude, elevation, date, time);
            // Add the new event to the vector
            events.add(newEvent);
            return newEvent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Handle errors appropriately
        }
    }
}