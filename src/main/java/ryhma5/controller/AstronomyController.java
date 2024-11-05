package ryhma5.controller;

import ryhma5.model.*;

import java.util.ArrayList;
import java.util.List;

public class AstronomyController {

    private List<AstronomyResponse> responses;

    public AstronomyController(){
        responses = new ArrayList<>();
    }

    public ArrayList<AstronomyResponse> getAstronomyEvent(String body,
                                            String latitude,
                                            String longitude,
                                            String elevation,
                                            String fromDate,
                                            String toDate,
                                            String time) {
        ArrayList<AstronomyResponse> events = new ArrayList<>();

        try {
            ArrayList<AstronomyResponse> newEvent = AstronomyAPI.fetchAstronomyEvent(body,
                                                                        latitude,
                                                                        longitude,
                                                                        elevation,
                                                                        fromDate,
                                                                        toDate,
                                                                        time);


            events.addAll(newEvent);
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



        // Fetch new body information if not cached
        try {
            ArrayList<AstronomyResponse> newBody = AstronomyAPI.fetchAstronomyBody(body,
                                                                    latitude,
                                                                    longitude,
                                                                    elevation,
                                                                    fromDate,
                                                                    toDate,
                                                                    time);

            if (newBody != null) {
                bodies.addAll(newBody);
            }

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



        // Fetch new body information if not cached
        try {
            ArrayList<AstronomyResponse> newBody = AstronomyAPI.fetchAllBodies(latitude,
                                                                                longitude,
                                                                                elevation,
                                                                                fromDate,
                                                                                toDate,
                                                                                time);

            if (newBody != null) {
                bodies.addAll(newBody);
            }

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



}