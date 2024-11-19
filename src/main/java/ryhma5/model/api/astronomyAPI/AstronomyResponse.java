package ryhma5.model.api.astronomyAPI;

import java.time.OffsetDateTime;
import java.util.Objects;
/**
 * A class representing the response from the "Astronomy API".
 */
public class AstronomyResponse {

    // date and time for events is at the events peak
    private OffsetDateTime dateTime;
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
    private String eventPartialStart;
    private String eventPartialEnd;
    private String eventTotalStart;
    private String eventTotalEnd;
    private double observerLongitude;
    private double observerLatitude;

    // Private constructor to enforce the use of the builder
    private AstronomyResponse(Builder builder) {
        this.dateTime = builder.dateTime;
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
        this.eventPartialStart = builder.eventPartialStart;
        this.eventPartialEnd = builder.eventPartialEnd;
        this.eventTotalStart = builder.eventTotalStart;
        this.eventTotalEnd = builder.eventTotalEnd;
        this.observerLongitude = builder.longitude;
        this.observerLatitude = builder.latitude;
    }

    public static class Builder {
        private OffsetDateTime dateTime;
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
        private String eventPartialStart;
        private String eventPartialEnd;
        private String eventTotalStart;
        private String eventTotalEnd;
        private double longitude;
        private double latitude;

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setDateTime(OffsetDateTime dateTime) {
            this.dateTime = dateTime;
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

        public Builder setObserverLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }
        public Builder setObserverLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public AstronomyResponse build() {
            return new AstronomyResponse(this);
        }

    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }
    public String getBodyName() {
        return bodyName;
    }
    public String getBodyId() {
        return bodyId;
    }
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
    public double getObserverLongitude() {
        return observerLongitude;
    }
    public double getObserverLatitude() {
        return observerLatitude;
    }

    @Override
    public String toString() {
        return "AstronomyResponse{" +
                "dateTime='" + dateTime + '\'' +
                ", bodyName='" + bodyName + '\'' +
                ", bodyId='" + bodyId + '\'' +
                ", distanceFromEarth='" + distanceFromEarth + '\'' +
                ", constellation='" + constellation + '\'' +
                ", altitude='" + altitude + '\'' +
                ", azimuth='" + azimuth + '\'' +
                ", elongation=" + elongation +
                ", magnitude=" + magnitude +
                ", moonPhaseString='" + moonPhaseString + '\'' +
                ", moonPhaseFraction=" + moonPhaseFraction +
                ", eventType='" + eventType + '\'' +
                ", eventObscuration=" + eventObscuration +
                ", eventBodyRise='" + eventBodyRise + '\'' +
                ", eventBodySet='" + eventBodySet + '\'' +
                ", eventPartialStart='" + eventPartialStart + '\'' +
                ", eventPartialEnd='" + eventPartialEnd + '\'' +
                ", eventTotalStart='" + eventTotalStart + '\'' +
                ", eventTotalEnd='" + eventTotalEnd + '\'' +
                ", observerLongitude=" + observerLongitude +
                ", observerLatitude=" + observerLatitude +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        // Check if the object is the same instance
        if (this == obj) return true;

        // Check if the object is an instance of AstronomyResponse
        if (!(obj instanceof AstronomyResponse)) return false;

        // Cast the object to AstronomyResponse
        AstronomyResponse other = (AstronomyResponse) obj;

        // Compare each field for equality
        return Objects.equals(dateTime, other.dateTime) &&
                Objects.equals(bodyName, other.bodyName) &&
                Objects.equals(bodyId, other.bodyId) &&
                Objects.equals(distanceFromEarth, other.distanceFromEarth) &&
                Objects.equals(constellation, other.constellation) &&
                Objects.equals(altitude, other.altitude) &&
                Objects.equals(azimuth, other.azimuth) &&
                Double.compare(elongation, other.elongation) == 0 &&
                Double.compare(magnitude, other.magnitude) == 0 &&
                Objects.equals(moonPhaseString, other.moonPhaseString) &&
                Double.compare(moonPhaseFraction, other.moonPhaseFraction) == 0 &&
                Objects.equals(eventType, other.eventType) &&
                Double.compare(eventObscuration, other.eventObscuration) == 0 &&
                Objects.equals(eventBodyRise, other.eventBodyRise) &&
                Objects.equals(eventBodySet, other.eventBodySet) &&
                Objects.equals(eventPartialStart, other.eventPartialStart) &&
                Objects.equals(eventPartialEnd, other.eventPartialEnd) &&
                Objects.equals(eventTotalStart, other.eventTotalStart) &&
                Objects.equals(eventTotalEnd, other.eventTotalEnd) &&
                Double.compare(observerLongitude, other.observerLongitude) == 0 &&
                Double.compare(observerLatitude, other.observerLatitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, bodyName, bodyId, distanceFromEarth, constellation, altitude, azimuth,
                elongation, magnitude, moonPhaseString, moonPhaseFraction, eventType,
                eventObscuration, eventBodyRise, eventBodySet,
                eventPartialStart, eventPartialEnd, eventTotalStart, eventTotalEnd,
                observerLongitude, observerLatitude);
    }




}