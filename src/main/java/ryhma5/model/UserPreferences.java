package ryhma5.model;

public class UserPreferences {

    private String cityName;
    private String dateStart;
    private String dateEnd;
    private double latitude;
    private double longitude;

    public UserPreferences(String cityName, String dateStart, String dateEnd, double latitude, double longitude){
        this.cityName = cityName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getters
    public String getCityName() { return cityName; }
    public String getDateStart(){ return dateStart; }
    public String getDateEnd(){ return dateEnd; }
    public double getLatitude(){ return latitude; }
    public double getLongitude(){ return longitude; }

    // setters
    public void  setCityName (String cityName){ this.cityName = cityName; }
    public void setDateStart (String dateStart){ this.dateStart = dateStart; }
    public void setDateEnd (String dateEnd){ this.dateEnd = dateEnd; }
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }
}