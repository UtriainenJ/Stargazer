package ryhma5.model;

import java.util.List;

public class AstronomyBody {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Dates dates;
        private Observer observer;
        private List<Row> rows;

        public Dates getDates() {
            return dates;
        }

        public void setDates(Dates dates) {
            this.dates = dates;
        }

        public Observer getObserver() {
            return observer;
        }

        public void setObserver(Observer observer) {
            this.observer = observer;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }
    }

    public static class Dates {
        private String from;
        private String to;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }

    public static class Observer {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Location {
        private double longitude;
        private double latitude;
        private double elevation;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getElevation() {
            return elevation;
        }

        public void setElevation(double elevation) {
            this.elevation = elevation;
        }
    }

    public static class Row {
        private Body body;
        private List<PositionEntry> positions;

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

        public List<PositionEntry> getPositions() {
            return positions;
        }

        public void setPositions(List<PositionEntry> positions) {
            this.positions = positions;
        }
    }

    public static class Body {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class PositionEntry {
        private String date;
        private String id;
        private String name;
        private Distance distance;
        private Position position;
        private ExtraInfo extraInfo;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public ExtraInfo getExtraInfo() {
            return extraInfo;
        }

        public void setExtraInfo(ExtraInfo extraInfo) {
            this.extraInfo = extraInfo;
        }
    }

    public static class Distance {
        private FromEarth fromEarth;

        public FromEarth getFromEarth() {
            return fromEarth;
        }

        public void setFromEarth(FromEarth fromEarth) {
            this.fromEarth = fromEarth;
        }

        public static class FromEarth {
            private String au;
            private String km;

            public String getAu() {
                return au;
            }

            public void setAu(String au) {
                this.au = au;
            }

            public String getKm() {
                return km;
            }

            public void setKm(String km) {
                this.km = km;
            }
        }
    }

    public static class Position {
        private Horizontal horizontal;
        private Equatorial equatorial;
        private Constellation constellation;

        public Horizontal getHorizontal() {
            return horizontal;
        }

        public void setHorizontal(Horizontal horizontal) {
            this.horizontal = horizontal;
        }

        public Equatorial getEquatorial() {
            return equatorial;
        }

        public void setEquatorial(Equatorial equatorial) {
            this.equatorial = equatorial;
        }

        public Constellation getConstellation() {
            return constellation;
        }

        public void setConstellation(Constellation constellation) {
            this.constellation = constellation;
        }
    }

    public static class Horizontal {
        private Altitude altitude;
        private Azimuth azimuth;

        public Altitude getAltitude() {
            return altitude;
        }

        public void setAltitude(Altitude altitude) {
            this.altitude = altitude;
        }

        public Azimuth getAzimuth() {
            return azimuth;
        }

        public void setAzimuth(Azimuth azimuth) {
            this.azimuth = azimuth;
        }
    }

    public static class Altitude {
        private String degrees;
        private String string;

        public String getDegrees() {
            return degrees;
        }

        public void setDegrees(String degrees) {
            this.degrees = degrees;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    public static class Azimuth {
        private String degrees;
        private String string;

        public String getDegrees() {
            return degrees;
        }

        public void setDegrees(String degrees) {
            this.degrees = degrees;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    public static class Equatorial {
        private RightAscension rightAscension;
        private Declination declination;

        public RightAscension getRightAscension() {
            return rightAscension;
        }

        public void setRightAscension(RightAscension rightAscension) {
            this.rightAscension = rightAscension;
        }

        public Declination getDeclination() {
            return declination;
        }

        public void setDeclination(Declination declination) {
            this.declination = declination;
        }
    }

    public static class RightAscension {
        private String hours;
        private String string;

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    public static class Declination {
        private String degrees;
        private String string;

        public String getDegrees() {
            return degrees;
        }

        public void setDegrees(String degrees) {
            this.degrees = degrees;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    public static class Constellation {
        private String id;
        private String shortName;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ExtraInfo {
        private double elongation;
        private double magnitude;
        private Phase phase;

        public double getElongation() {
            return elongation;
        }

        public void setElongation(double elongation) {
            this.elongation = elongation;
        }

        public double getMagnitude() {
            return magnitude;
        }

        public void setMagnitude(double magnitude) {
            this.magnitude = magnitude;
        }

        public Phase getPhase() {
            return phase;
        }

        public void setPhase(Phase phase) {
            this.phase = phase;
        }

        public static class Phase {
            private String angel;
            private String fraction;
            private String string;

            public String getAngel() {
                return angel;
            }

            public void setAngel(String angel) {
                this.angel = angel;
            }

            public String getFraction() {
                return fraction;
            }

            public void setFraction(String fraction) {
                this.fraction = fraction;
            }

            public String getString() {
                return string;
            }

            public void setString(String string) {
                this.string = string;
            }
        }
    }
}
