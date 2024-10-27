package ryhma5.controller;

import ryhma5.model.ISSResponse;
import ryhma5.model.WhereISSAPI;

import java.util.List;

public class WhereISSController {
    public ISSResponse getISS(String units, Long timestamp) {
        try {
            return WhereISSAPI.fetchISS(units, timestamp);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null; // or throw a custom exception if needed
        }
    }

    public List<ISSResponse> getISSPositions(List<Long> timestamps, String units) {
        try {
            return WhereISSAPI.fetchISSPositions(timestamps, units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS positions: " + e.getMessage());
            return null; // or throw a custom exception if needed
        }
    }
}