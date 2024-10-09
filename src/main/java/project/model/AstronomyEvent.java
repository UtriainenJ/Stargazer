package project.model;

public class AstronomyEvent {
    private String latitude;
    private String longitude;
    private String elevation;
    private String date;
    private String time;

    // Constructor
    public AstronomyEvent(String latitude, String longitude, String elevation, String date, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AstronomyEvent{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", elevation='" + elevation + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

