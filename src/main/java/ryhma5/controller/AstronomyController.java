package ryhma5.controller;

import com.google.gson.reflect.TypeToken;
import ryhma5.model.api.astronomyAPI.AstronomyHandler;
import ryhma5.model.api.astronomyAPI.AstronomyResponse;
import ryhma5.model.json.DataManager;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * A controller class for the Astronomy API.
 * Caches responses to avoid unnecessary API calls.
 */
public class AstronomyController {

    private List<AstronomyResponse> responses;
    private Map<String, String> urls;

    private int LAST3REQUESTS = 6006;

    /**
     * Initializes the controller.
     */
    public AstronomyController(){
        List<AstronomyResponse> loadedResponseData = DataManager.loadDataAsList("astronomy_responses", AstronomyResponse.class);
        if (!loadedResponseData.isEmpty()) {
            responses = loadedResponseData;
        }

        urls = new HashMap<>();
        // get map type for loadDataAsObject
        Type urlsType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> loadedUrlsData = DataManager.loadDataAsObject("astronomy_urls", urlsType);
        if(!loadedUrlsData.isEmpty()){
            urls = loadedUrlsData;
        }

        AstronomyHandler.loadAPICredentials();
    }

    /**
     * Fetches the astronomy event information for a specific body.
     * @param body The name of the body to fetch information for.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param elevation The observer's elevation.
     * @param fromDate The start date of the event information.
     * @param toDate The end date of the event information.
     * @param time The time of the event information.
     * @return An ArrayList of AstronomyResponse objects containing the event information.
     */
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
            ArrayList<AstronomyResponse> newEvents = AstronomyHandler.fetchAstronomyEvent(body,
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

    /**
     * Fetches the astronomy information for a specific body.
     * @param body The name of the body to fetch information for.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param elevation The observer's elevation.
     * @param fromDate The start date of the event information.
     * @param toDate The end date of the event information.
     * @param time The time of the event information.
     * @return An ArrayList of AstronomyResponse objects containing the event information.
     */
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
            ArrayList<AstronomyResponse> newBodies = AstronomyHandler.fetchAstronomyBody(body,
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
            return null;
        }

        return bodies;
    }

    /**
     * Fetches the astronomy information for all bodies.
     * @param latitude The observer's latitude.
     * @param longitude The observer's longitude.
     * @param elevation The observer's elevation.
     * @param fromDate The start date of the event information.
     * @param toDate The end date of the event information.
     * @param time The time of the event information.
     * @return An ArrayList of AstronomyResponse objects containing the event information.
     */
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
                System.out.println("No cached responses found for " + body
                        + " at position " + latitude + ", " + longitude);
                allFound = false;
                break;
            }
            else {
                allCachedBodies.add(cachedResponses.get(0));
            }
        }

        if (allFound) {
            System.out.println("All bodies found in cache for position "  + latitude + ", " + longitude);
            return allCachedBodies; // Return cached responses if available for all bodies
        }

        // Fetch new body information if not cached
        try {
            ArrayList<AstronomyResponse> newBodies = AstronomyHandler.fetchAllBodies(latitude,
                                                                                    longitude,
                                                                                    elevation,
                                                                                    fromDate,
                                                                                    toDate,
                                                                                    time);

                bodies.addAll(newBodies);
                responses.addAll(newBodies);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bodies;
    }

    /**
     * Gets the star chart image for a specific constellation
     * @param latitude
     * @param longitude
     * @param date
     * @param constellationId
     * @return url of the star chart image
     */
    public String getConstellationStarChart(double latitude, double longitude, String date, String constellationId) {
        String cacheKey = generateCacheKey(latitude, longitude, date, constellationId);

        if (urls.containsKey(cacheKey)) {
            System.out.println("Cache hit for key: " + cacheKey);
            return urls.get(cacheKey);
        }

        try {
            return AstronomyHandler.generateConstellationStarChart(latitude, longitude, date, constellationId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the star chart image for a specific area
     * @param latitude
     * @param longitude
     * @param date
     * @param rightAscension
     * @param declination
     * @param zoom
     * @return url of the star chart image
     */
    public String getAreaStarChart(double latitude, double longitude, String date, Double rightAscension, Double declination, Integer zoom) {
        String cacheKey = generateCacheKey(latitude, longitude, date, rightAscension, declination, zoom);

        if (urls.containsKey(cacheKey)) {
            System.out.println("Cache hit for key: " + cacheKey);
            return urls.get(cacheKey);
        }
        try {
            return AstronomyHandler.generateAreaStarChart(latitude, longitude, date, rightAscension, declination, zoom);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the moon phase image for a specific date
     * @param latitude
     * @param longitude
     * @param date
     * @param format
     * @return url of the moon phase image
     */
    public String getMoonPhaseImage(double latitude, double longitude, String date, String format) {
        String cacheKey = generateCacheKey(latitude, longitude, date, format);

        if (urls.containsKey(cacheKey)) {
            System.out.println("Cache hit for key: " + cacheKey);
            return urls.get(cacheKey);
        }

        try {
            return AstronomyHandler.generateMoonPhaseImage(latitude, longitude, date, format);
        } catch (Exception e) {
            return null;
        }
    }

    // Get the responses from the cache
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

                if (isCloseEnough(response.getObserverLatitude(), lat) &&
                    isCloseEnough(response.getObserverLongitude(), lon) &&
                    response.getBodyName().equalsIgnoreCase(body) &&
                    (responseDate.isEqual(from) || responseDate.isAfter(from)) &&
                    (responseDate.isEqual(to) || responseDate.isBefore(to)) &&
                    responseTime.equals(userTime)){

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

    /**
     * save last 3 requests responses as json and urls as json
     */
    public void saveAstronomyData(){
        List<AstronomyResponse> last3Requests = responses.subList(Math.max(responses.size() - LAST3REQUESTS,0), responses.size());
        DataManager.saveData(last3Requests, "astronomy_responses");

        //System.out.println(urls.size());
        DataManager.saveData(urls, "astronomy_urls");
    }

    // Check if all dates between fromDate and toDate are covered in the cached responses
    private boolean allDatesCovered(List<AstronomyResponse> cachedResponses, LocalDate fromDate, LocalDate toDate) {
        List<LocalDate> allDates = Stream.iterate(fromDate, date -> date.plusDays(1))
                .limit(fromDate.until(toDate).getDays() + 1)
                .toList();

        List<LocalDate> responseDates = cachedResponses.stream()
                .map(response -> response.getDateTime().toLocalDate())
                .distinct()
                .toList();

        for (LocalDate date : allDates) {
            if (!responseDates.contains(date)) {
                return false; // Return false if any date is missing
            }
        }
        return true; // Return true if all dates are covered
    }

    private boolean isCloseEnough(double value1, double value2) {
        return Math.abs(value1 - value2) < 0.1;
    }

    /* Generates key for url cache
        Object... params accepts any number of parameters
     */
    private String generateCacheKey(Object... parameters) {
        Stream<Object> params = Arrays.stream(parameters);

        // Convert each parameter to a string and join them with a "|" separator
        return params
                .map(String::valueOf)  // Convert each parameter to its string representation
                .reduce((a, b) -> a + "|" + b)  // Combine the strings with "|" as a separator
                .orElse("");  // Return an empty string if no parameters are provided
    }

}