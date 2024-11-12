package ryhma5.model;

/**
 * object representing a city. cities intended to be loaded from a json file
 */
public class City {
    private String city;
    private String lat;
    private String lng;
    private String country;
    private String country_code;

    public String getCityName() { return city; }
    public String getLat() { return lat; }
    public String getLng() { return lng; }

    @Override
    public String toString() {
        return "City{name='" + city + "', lat='" + lat + "', lng='" + lng + "'}";
    }
}
