package ryhma5.model;

public class AstronomyEvent {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        private Dates dates;
        private Observer observer;
        private Table table;

        public Dates getDates() {
            return dates;
        }

        public Observer getObserver() {
            return observer;
        }

        public Table getTable() {
            return table;
        }
    }

    public static class Dates {
        private String from;
        private String to;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }

    public static class Observer {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public static class Location {
            private double longitude;
            private double latitude;
            private int elevation;

            public double getLongitude() {
                return longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public int getElevation() {
                return elevation;
            }
        }
    }

    public static class Table {
        private String[] header;
        private Row[] rows;

        public String[] getHeader() {
            return header;
        }

        public Row[] getRows() {
            return rows;
        }

        public static class Row {
            private Entry entry;
            private Cell[] cells;

            public Entry getEntry() {
                return entry;
            }

            public Cell[] getCells() {
                return cells;
            }

            public static class Entry {
                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }

            public static class Cell {
                private String type;
                private EventHighlights eventHighlights;
                private String rise;
                private String set;
                private ExtraInfo extraInfo;

                public String getType() {
                    return type;
                }

                public EventHighlights getEventHighlights() {
                    return eventHighlights;
                }

                public String getRise() {
                    return rise;
                }

                public String getSet() {
                    return set;
                }

                public ExtraInfo getExtraInfo() {
                    return extraInfo;
                }

                public static class EventHighlights {
                    private DateDetails partialStart;
                    private DateDetails peak;
                    private DateDetails partialEnd;

                    public DateDetails getPartialStart() {
                        return partialStart;
                    }

                    public DateDetails getPeak() {
                        return peak;
                    }

                    public DateDetails getPartialEnd() {
                        return partialEnd;
                    }
                }

                public static class DateDetails {
                    private String date;
                    private double altitude;

                    public String getDate() {
                        return date;
                    }

                    public double getAltitude() {
                        return altitude;
                    }
                }

                public static class ExtraInfo {
                    private double obscuration;

                    public double getObscuration() {
                        return obscuration;
                    }
                }
            }
        }
    }
}
