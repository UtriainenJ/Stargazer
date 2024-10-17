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

    public AstronomyEvent getAstronomyEvent(String body,
                                            String latitude,
                                            String longitude,
                                            String elevation,
                                            String fromDate,
                                            String toDate,
                                            String time) {

        // nasty looking comparison but way faster than another API call
        for (AstronomyEvent event : events) {
            AstronomyEvent.Data data = event.getData(); // Access the data object

            if (data != null &&
                    Double.compare(data.getObserver().getLocation().getLatitude(), Double.parseDouble(latitude)) == 0 &&
                    Double.compare(data.getObserver().getLocation().getLongitude(), Double.parseDouble(longitude)) == 0 &&
                    data.getObserver().getLocation().getElevation() == Integer.parseInt(elevation) &&
                    data.getDates().getFrom().startsWith(fromDate) &&
                    data.getDates().getTo().startsWith(toDate) &&
                    (data.getTable().getRows() != null &&
                        data.getTable().getRows().length > 0 &&
                        data.getTable().getRows()[0].getEntry().getName().equals(body))) {

                // Event found, return it
                return event;
            }

        }

        try {
            AstronomyEvent newEvent = AstronomyAPI.fetchAstronomyEvent(body,
                                                                        latitude,
                                                                        longitude,
                                                                        elevation,
                                                                        fromDate,
                                                                        toDate,
                                                                        time);

            // If the new event is not null, add it to the events vector
            if (newEvent != null) {
                events.add(newEvent);
            }
            return newEvent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}