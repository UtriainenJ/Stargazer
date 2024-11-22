package ryhma5.model.dateTimeUtils;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimestampConverterTest {

    @Test
    void testToTimestamp() {
        String date = "2024-11-13";
        String time = "12:00:00";

        // Calculate the expected timestamp based on the system's time zone
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2024, 11, 13, 12, 0, 0, 0, ZoneId.systemDefault());
        long expectedTimestamp = zonedDateTime.toEpochSecond();

        long timestamp = TimestampConverter.toTimestamp(date, time);

        assertEquals(expectedTimestamp, timestamp);
    }

    @Test
    void testFromTimestamp() {
        // Use a known timestamp
        long timestamp = 1731492000L;

        // Calculate the expected date-time string based on the system's time zone
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        String expectedDateTime = zonedDateTime.format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String dateTime = TimestampConverter.fromTimestamp(timestamp);

        assertEquals(expectedDateTime, dateTime);
    }

    @Test
    void testToTimestampAndBack() {
        String date = "2024-11-13";
        String time = "12:00:00";

        long timestamp = TimestampConverter.toTimestamp(date, time);
        String dateTime = TimestampConverter.fromTimestamp(timestamp);

        // Verify the output starts with the expected date and time
        assertTrue(dateTime.startsWith(date + "T" + time.substring(0, 5)));
    }

    @Test
    void testInvalidDate() {
        String invalidDate = "2024-13-40"; // Invalid date
        String time = "12:00:00";

        assertThrows(Exception.class, () -> TimestampConverter.toTimestamp(invalidDate, time));
    }

    @Test
    void testInvalidTime() {
        String date = "2024-11-13";
        String invalidTime = "25:00:00"; // Invalid time

        assertThrows(Exception.class, () -> TimestampConverter.toTimestamp(date, invalidTime));
    }
}
