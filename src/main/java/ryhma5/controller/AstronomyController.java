package ryhma5.controller;

import ryhma5.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AstronomyController {

    private List<AstronomyResponse> responses;

    public AstronomyController(){
        responses = new ArrayList<>();
        responses = DataManager.loadDataAsList("astronomy_responses", AstronomyResponse.class);
        AstronomyAPI.loadAPICredentials();
    }

    public ArrayList<AstronomyResponse> getAstronomyEvent(String body,
                                                        String latitude,
                                                        String longitude,
                                                        String elevation,
                                                        String fromDate,
                                                        String toDate,
                                                        String time) {
        ArrayList<AstronomyResponse> events = new ArrayList<>();

        ArrayList<AstronomyResponse> cachedResponses = getCachedResponses("event", body,
                Double.parseDouble(latitude), Double.parseDouble(longitude),
                fromDate, toDate, time);

        if (cachedResponses != null) {
            return cachedResponses; // Return cached responses if available
        }

        try {
            ArrayList<AstronomyResponse> newEvents = AstronomyAPI.fetchAstronomyEvent(body,
                                                                                    latitude,
                                                                                    longitude,
                                                                                    elevation,
                                                                                    fromDate,
                                                                                    toDate,
                                                                                    time);



            events.addAll(newEvents);
            responses.addAll(newEvents);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return events;
    }

    public ArrayList<AstronomyResponse> getAstronomyBody(String body,
                                                       String latitude,
                                                       String longitude,
                                                       String elevation,
                                                       String fromDate,
                                                       String toDate,
                                                       String time) {
        ArrayList<AstronomyResponse> bodies = new ArrayList<>();

        ArrayList<AstronomyResponse> cachedResponses = getCachedResponses("body", body,
                Double.parseDouble(latitude), Double.parseDouble(longitude),
                fromDate, toDate, time);

        if (cachedResponses != null) {
            return cachedResponses; // Return cached responses if available
        }

        // Fetch new body information if not cached
        try {
            ArrayList<AstronomyResponse> newBodies = AstronomyAPI.fetchAstronomyBody(body,
                                                                                latitude,
                                                                                longitude,
                                                                                elevation,
                                                                                fromDate,
                                                                                toDate,
                                                                                time);

                bodies.addAll(newBodies);
                responses.addAll(newBodies);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle API or parsing errors
        }

        return bodies;
    }
    public ArrayList<AstronomyResponse> getAllAstronomyBodies(String latitude,
                                                              String longitude,
                                                              String elevation,
                                                              String fromDate,
                                                              String toDate,
                                                              String time) {

        ArrayList<AstronomyResponse> bodies = new ArrayList<>();

        List<String> allPossibleBodies = Arrays.asList(
                "sun", "moon", "mercury", "venus", "earth", "mars", "jupiter", "saturn", "uranus", "neptune", "pluto");

        ArrayList<AstronomyResponse> allCachedBodies = new ArrayList<>();
        boolean allFound = true;
        for (String body : allPossibleBodies) {
            ArrayList<AstronomyResponse> cachedResponses = getCachedResponses("body", body,
                    Double.parseDouble(latitude), Double.parseDouble(longitude),
                    fromDate, toDate, time);

            if (cachedResponses == null){
                allFound = false;
                break;
            }
            else {
                allCachedBodies.addAll(cachedResponses);
            }
        }

        if (allFound) {
            return allCachedBodies; // Return cached responses if available
        }

        // Fetch new body information if not cached
        try {
            ArrayList<AstronomyResponse> newBodies = AstronomyAPI.fetchAllBodies(latitude,
                                                                                longitude,
                                                                                elevation,
                                                                                fromDate,
                                                                                toDate,
                                                                                time);

                bodies.addAll(newBodies);
                responses.addAll(newBodies);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle API or parsing errors
        }

        return bodies;

    }

    public String getConstellationStarChart(double latitude, double longitude, String date, String constellationId) {
        try {
            return AstronomyAPI.generateConstellationStarChart(latitude, longitude, date, constellationId);
        } catch (Exception e) {
            System.err.println("Error generating star chart: " + e.getMessage());
            return null;
        }
    }

    public String getAreaStarChart(double latitude, double longitude, String date, Double rightAscension, Double declination, Integer zoom) {
        try {
            return AstronomyAPI.generateAreaStarChart(latitude, longitude, date, rightAscension, declination, zoom);
        } catch (Exception e) {
            System.err.println("Error generating area star chart: " + e.getMessage());
            return null;
        }
    }

    public String getMoonPhaseImage(double latitude, double longitude, String date, String format) {
        try {
            return AstronomyAPI.generateMoonPhaseImage(latitude, longitude, date, format);
        } catch (Exception e) {
            System.err.println("Error generating moon phase image: " + e.getMessage());
            return null;
        }
    }


    private ArrayList<AstronomyResponse> getCachedResponses(String type, String body, Double lat, Double lon, String fromDate, String toDate, String time) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        if (!allDatesCovered(responses, from, to)) {
            return null;
        }

        ArrayList<AstronomyResponse> cachedEvents = new ArrayList<>();
            for (AstronomyResponse response : responses) {
                LocalDate responseDate = response.getDateTime().toLocalDate();


                LocalTime responseTime = response.getDateTime().toLocalTime();
                LocalTime userTime = LocalTime.parse(time);

                if (response.getBodyName().equals(body) &&
                        (responseDate.isEqual(from) || responseDate.isAfter(from)) &&
                        (responseDate.isEqual(to) || responseDate.isBefore(to)) &&
                        responseTime.equals(userTime) &&
                        response.getObserverLatitude() == lat &&
                        response.getObserverLongitude() == lon) {
                    if (type.equals("event") && response.getEventType() != null) {
                        cachedEvents.add(response);
                    }
                    else if (type.equals("body") && response.getEventType() == null) {
                        cachedEvents.add(response);
                    }
                }
            }
        return cachedEvents.isEmpty() ? null : cachedEvents;
    }

    // save all responses as json
    public void saveAstronomyResponses(){
        DataManager.saveData(responses, "astronomy_responses");
    }

    private boolean allDatesCovered(List<AstronomyResponse> cachedResponses, LocalDate fromDate, LocalDate toDate) {
        // Generate a list of all dates between fromDate and toDate (inclusive)
        List<LocalDate> allDates = Stream.iterate(fromDate, date -> date.plusDays(1))
                .limit(fromDate.until(toDate).getDays() + 1)
                .collect(Collectors.toList());

        // Extract all unique LocalDate values from the cached responses
        List<LocalDate> responseDates = cachedResponses.stream()
                .map(response -> response.getDateTime().toLocalDate())
                .distinct()
                .collect(Collectors.toList());

        // Check if all dates from fromDate to toDate are covered in the cached responses
        for (LocalDate date : allDates) {
            if (!responseDates.contains(date)) {
                return false; // Return false if any date is missing
            }
        }

        return true; // Return true if all dates are covered
    }


}