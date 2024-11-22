package ryhma5.model.dateTimeUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateConverterTest {

    @Test
    void testToString() {
        assertEquals("2021-05-01", LocalDateConverter.toString(LocalDateConverter.fromString("2021-05-01")));
        assertEquals("2021-06-01", LocalDateConverter.toString(LocalDateConverter.fromString("2021-06-01")));
        assertEquals("2021-07-01", LocalDateConverter.toString(LocalDateConverter.fromString("2021-07-01")));
        assertEquals("2021-08-01", LocalDateConverter.toString(LocalDateConverter.fromString("2021-08-01")));
        assertEquals("2021-09-01", LocalDateConverter.toString(LocalDateConverter.fromString("2021-09-01")));
    }

    @Test
    void fromString() {
        assertEquals("2021-05-01", LocalDateConverter.fromString("2021-05-01").toString());
        assertEquals("2021-06-01", LocalDateConverter.fromString("2021-06-01").toString());
        assertEquals("2021-07-01", LocalDateConverter.fromString("2021-07-01").toString());
        assertEquals("2021-08-01", LocalDateConverter.fromString("2021-08-01").toString());
        assertEquals("2021-09-01", LocalDateConverter.fromString("2021-09-01").toString());
    }
}