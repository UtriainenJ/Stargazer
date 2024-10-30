package ryhma5.model;

/**
 * object representing a city. cities intended to be loaded from a json file
 */
public class City {
    private String name;
    private String lat;
    private String lng;

    public String getName() { return name; }
    public String getLat() { return lat; }
    public String getLng() { return lng; }

    @Override
    public String toString() {
        return "City{name='" + name + "', lat='" + lat + "', lng='" + lng + "'}";
    }
}
