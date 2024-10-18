package ryhma5.viewmodel;

import ryhma5.model.ISSInfo;
import ryhma5.model.WhereISSAPI;

public class WhereISSViewModel {
    public ISSInfo getISS(String units, Long timestamp) {
        try {
            return WhereISSAPI.fetchISS(units, timestamp);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null; // or throw a custom exception if needed
        }
    }
}