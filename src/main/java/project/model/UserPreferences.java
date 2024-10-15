package project.model;

public class UserPreferences {

    public String dateStart;
    public String dateEnd;
    public double latitude;
    public double longitude;

    public UserPreferences(String dateStart, String dateEnd, double latitude, double longitude){
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getters
    public String getDateStart(){
        return dateStart;
    }

    public String getDateEnd(){
        return dateEnd;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    // setters
    public void setDateStart (String dateStart){
        this.dateStart = dateStart;
    }

    public void setDateEnd (String dateEnd){
        this.dateEnd = dateEnd;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
}
