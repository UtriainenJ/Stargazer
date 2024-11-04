package ryhma5.model;

public class AstronomyResponse {
    private double longitude;
    private double latitude;
    private double elevation;
    private String date;
    private String bodyName;
    private String bodyId;
    private String distanceFromEarth;
    private String constellation;
    private String altitude;
    private String azimuth;
    private double elongation;
    private double magnitude;
    private String moonPhaseString;
    private double moonPhaseFraction;
    private String eventType;
    private double eventObscuration;
    private String eventBodyRise;
    private String eventBodySet;
    private String eventPeak;
    private String eventPartialStart;
    private String eventPartialEnd;
    private String eventTotalStart;
    private String eventTotalEnd;

    // Private constructor to enforce the use of the builder
    private AstronomyResponse(Builder builder) {
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
        this.elevation = builder.elevation;
        this.date = builder.date;
        this.bodyName = builder.bodyName;
        this.bodyId = builder.bodyId;
        this.distanceFromEarth = builder.distanceFromEarth;
        this.constellation = builder.constellation;
        this.altitude = builder.altitude;
        this.azimuth = builder.azimuth;
        this.elongation = builder.elongation;
        this.magnitude = builder.magnitude;
        this.moonPhaseString = builder.moonPhaseString;
        this.moonPhaseFraction = builder.moonPhaseFraction;
        this.eventType = builder.eventType;
        this.eventObscuration = builder.eventObscuration;
        this.eventBodyRise = builder.eventBodyRise;
        this.eventBodySet = builder.eventBodySet;
        this.eventPeak = builder.eventPeak;
        this.eventPartialStart = builder.eventPartialStart;
        this.eventPartialEnd = builder.eventPartialEnd;
        this.eventTotalStart = builder.eventTotalStart;
        this.eventTotalEnd = builder.eventTotalEnd;
    }

    public static class Builder {
        private double longitude;
        private double latitude;
        private double elevation;
        private String date;
        private String bodyName;
        private String bodyId;
        private String distanceFromEarth;
        private String constellation;
        private String altitude;
        private String azimuth;
        private double elongation;
        private double magnitude;
        private String moonPhaseString;
        private double moonPhaseFraction;
        private String eventType;
        private double eventObscuration;
        private String eventBodyRise;
        private String eventBodySet;
        private String eventPeak;
        private String eventPartialStart;
        private String eventPartialEnd;
        private String eventTotalStart;
        private String eventTotalEnd;

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setElevation(double elevation) {
            this.elevation = elevation;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setBodyName(String bodyName) {
            this.bodyName = bodyName;
            return this;
        }

        public Builder setBodyId(String bodyId) {
            this.bodyId = bodyId;
            return this;
        }

        public Builder setDistanceFromEarth(String distanceFromEarth) {
            this.distanceFromEarth = distanceFromEarth;
            return this;
        }

        public Builder setConstellation(String constellation) {
            this.constellation = constellation;
            return this;
        }

        public Builder setAltitude(String altitude) {
            this.altitude = altitude;
            return this;
        }

        public Builder setAzimuth(String azimuth) {
            this.azimuth = azimuth;
            return this;
        }

        public Builder setElongation(double elongation) {
            this.elongation = elongation;
            return this;
        }

        public Builder setMagnitude(double magnitude) {
            this.magnitude = magnitude;
            return this;
        }

        public Builder setMoonPhaseString(String moonPhaseString) {
            this.moonPhaseString = moonPhaseString;
            return this;
        }

        public Builder setMoonPhaseFraction(double moonPhaseFraction) {
            this.moonPhaseFraction = moonPhaseFraction;
            return this;
        }

        public Builder setEventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder setEventObscuration(double eventObscuration) {
            this.eventObscuration = eventObscuration;
            return this;
        }

        public Builder setEventBodyRise(String eventBodyRise) {
            this.eventBodyRise = eventBodyRise;
            return this;
        }

        public Builder setEventBodySet(String eventBodySet) {
            this.eventBodySet = eventBodySet;
            return this;
        }

        public Builder setEventPeak(String eventPeak) {
            this.eventPeak = eventPeak;
            return this;
        }

        public Builder setEventPartialStart(String eventPartialStart) {
            this.eventPartialStart = eventPartialStart;
            return this;
        }

        public Builder setEventPartialEnd(String eventPartialEnd) {
            this.eventPartialEnd = eventPartialEnd;
            return this;
        }

        public Builder setEventTotalStart(String eventTotalStart) {
            this.eventTotalStart = eventTotalStart;
            return this;
        }

        public Builder setEventTotalEnd(String eventTotalEnd) {
            this.eventTotalEnd = eventTotalEnd;
            return this;
        }

        public AstronomyResponse build() {
            return new AstronomyResponse(this);
        }

    }

    public String getDate() {
        return date;
    }
    public String getBodyName() {
        return bodyName;
    }
    public String getBodyId() { return bodyId; }
    public String getDistanceFromEarth() {
        return distanceFromEarth;
    }
    public String getConstellation() {
        return constellation;
    }
    public String getAltitude() {
        return altitude;
    }
    public String getAzimuth() {
        return azimuth;
    }
    public double getElongation() {
        return elongation;
    }
    public double getMagnitude() {
        return magnitude;
    }
    public String getMoonPhaseString() {
        return moonPhaseString;
    }
    public double getMoonPhaseFraction() {
        return moonPhaseFraction;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getElevation() {
        return elevation;
    }
    public String getEventType() {
        return eventType;
    }
    public double getEventObscuration() {
        return eventObscuration;
    }
    public String getEventBodyRise() {
        return eventBodyRise;
    }
    public String getEventBodySet() {
        return eventBodySet;
    }
    public String getEventPeak() {
        return eventPeak;
    }
    public String getEventPartialStart() {
        return eventPartialStart;
    }
    public String getEventPartialEnd() {
        return eventPartialEnd;
    }
    public String getEventTotalStart() {
        return eventTotalStart;
    }
    public String getEventTotalEnd() {
        return eventTotalEnd;
    }

}