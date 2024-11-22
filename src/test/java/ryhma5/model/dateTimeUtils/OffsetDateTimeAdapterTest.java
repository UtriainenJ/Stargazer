package ryhma5.model.dateTimeUtils;

import com.google.gson.*;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class OffsetDateTimeAdapterTest {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .create();

    @Test
    void testSerialize() {
        OffsetDateTime dateTime = OffsetDateTime.of(2024, 11, 20, 15, 30, 0, 0, ZoneOffset.UTC);
        String json = gson.toJson(dateTime);

        assertEquals("\"2024-11-20T15:30:00Z\"", json);
    }

    @Test
    void testDeserialize() {
        String json = "\"2024-11-20T15:30:00Z\"";
        OffsetDateTime expectedDateTime = OffsetDateTime.of(2024, 11, 20, 15, 30, 0, 0, ZoneOffset.UTC);

        OffsetDateTime deserializedDateTime = gson.fromJson(json, OffsetDateTime.class);

        assertEquals(expectedDateTime, deserializedDateTime);
    }

    @Test
    void testSerializeWithOffset() {
        OffsetDateTime dateTime = OffsetDateTime.of(2024, 11, 20, 15, 30, 0, 0, ZoneOffset.ofHours(2));
        String json = gson.toJson(dateTime);

        assertEquals("\"2024-11-20T15:30:00+02:00\"", json);
    }

    @Test
    void testDeserializeWithOffset() {
        String json = "\"2024-11-20T15:30:00+02:00\"";
        OffsetDateTime expectedDateTime = OffsetDateTime.of(2024, 11, 20, 15, 30, 0, 0, ZoneOffset.ofHours(2));

        OffsetDateTime deserializedDateTime = gson.fromJson(json, OffsetDateTime.class);

        assertEquals(expectedDateTime, deserializedDateTime);
    }

    @Test
    void testDeserializeInvalidFormat() {
        String json = "\"2024-11-20T15:30\""; // Missing offset

        assertThrows(DateTimeParseException.class, () -> gson.fromJson(json, OffsetDateTime.class));
    }


    @Test
    void testSerializeAndDeserialize() {
        OffsetDateTime originalDateTime = OffsetDateTime.of(2024, 11, 20, 15, 30, 0, 0, ZoneOffset.ofHours(-5));
        String json = gson.toJson(originalDateTime);
        OffsetDateTime deserializedDateTime = gson.fromJson(json, OffsetDateTime.class);

        assertEquals(originalDateTime, deserializedDateTime);
    }
}
