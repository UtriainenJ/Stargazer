package ryhma5.model.dateTimeUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimestampConverter {

    /**
     * Converts date and time strings to a Unix timestamp using the system's local time zone.
     *
     * @param dateString The date string in "yyyy-MM-dd" format (e.g., "2024-10-07").
     * @param timeString The time string in "HH:mm:ss" format (e.g., "12:00:00").
     * @return The Unix timestamp (seconds since the epoch).
     */
    public static long toTimestamp(String dateString, String timeString) {
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Uses the systems local time zone
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());

        return zonedDateTime.toEpochSecond();
    }
    /**
     * Converts a Unix timestamp to a formatted date-time string in the format "yyyy-MM-dd'T'HH:mmXXX".
     *
     * @param timestamp The Unix timestamp (seconds since the epoch).
     * @return A formatted date-time string (e.g., "2024-11-13T12:00-05:00").
     */
    public static String fromTimestamp(long timestamp) {
        // Convert Unix timestamp to ZonedDateTime using the system's local time zone
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        // Format ZonedDateTime to the desired string format
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return zonedDateTime.format(formatter);
    }

}