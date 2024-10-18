package ryhma5.viewmodel;

import ryhma5.model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AstronomyViewModel {

    private List<AstronomyEvent> events;
    private List<AstronomyBody> bodies;

    public AstronomyViewModel(){
        events = new ArrayList<>();
        bodies = new ArrayList<>();
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
                    !data.getTable().getRows().isEmpty() &&
                    data.getTable().getRows().get(0).getEntry().getName().equals(body)) {

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

    public AstronomyBody getAstronomyBody(String body,
                                   String latitude,
                                   String longitude,
                                   String elevation,
                                   String fromDate,
                                   String toDate,
                                   String time) {

        // Check if the body information is already cached
        for (AstronomyBody cachedBody : bodies) {
            AstronomyBody.Data data = cachedBody.getData();

            if (data != null &&
                    Double.compare(data.getObserver().getLocation().getLatitude(), Double.parseDouble(latitude)) == 0 &&
                    Double.compare(data.getObserver().getLocation().getLongitude(), Double.parseDouble(longitude)) == 0 &&
                    data.getObserver().getLocation().getElevation() == Integer.parseInt(elevation) &&
                    data.getDates().getFrom().startsWith(fromDate) &&
                    data.getDates().getTo().startsWith(toDate) &&
                    !data.getRows().isEmpty()  &&
                    data.getRows().get(0).getBody().getName().equals(body)) {


                // Matching body information found, return it
                return cachedBody;
            }
        }

        // Fetch new body information if not cached
        try {
            AstronomyBody newBody = AstronomyAPI.fetchAstronomyBody(body,
                                                                    latitude,
                                                                    longitude,
                                                                    elevation,
                                                                    fromDate,
                                                                    toDate,
                                                                    time);

            if (newBody != null) {
                bodies.add(newBody);
            }
            return newBody;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle API or parsing errors
        }
    }
    public AstronomyBody getAllAstronomyBodies(String latitude,
                                                     String longitude,
                                                     String elevation,
                                                     String fromDate,
                                                     String toDate,
                                                     String time) {


        // Fetch new body information if not cached
        try {
            AstronomyBody newBody = AstronomyAPI.fetchAllBodies(latitude,
                                                                    longitude,
                                                                    elevation,
                                                                    fromDate,
                                                                    toDate,
                                                                    time);

            if (newBody != null) {
                bodies.add(newBody);
            }
            return newBody;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle API or parsing errors
        }
    }

}