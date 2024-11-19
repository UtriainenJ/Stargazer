package ryhma5.model.api.whereTheISSAtAPI;

public class ISSResponse {
    private String name;
    private int id;
    private double latitude;
    private double longitude;
    private double altitude;
    private double velocity;
    private String visibility;
    private double footprint;
    private long timestamp;
    private double daynum;
    private double solar_lat;
    private double solar_lon;
    private String units;

    // Getters for the fields
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getVelocity() {
        return velocity;
    }

    public String getVisibility() {
        return visibility;
    }

    public double getFootprint() {
        return footprint;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getDaynum() {
        return daynum;
    }

    public double getSolar_lat() {
        return solar_lat;
    }

    public double getSolar_lon() {
        return solar_lon;
    }

    public String getUnits() {
        return units;
    }
}