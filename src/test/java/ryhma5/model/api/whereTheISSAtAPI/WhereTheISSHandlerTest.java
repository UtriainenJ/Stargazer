package ryhma5.model.api.whereTheISSAtAPI;

import org.junit.jupiter.api.Test;
import ryhma5.model.api.whereTheISSAtAPI.ISSResponse;
import ryhma5.model.api.whereTheISSAtAPI.WhereTheISSHandler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class WhereTheISSHandlerTest {

    @Test
    public void testFetchISSWithUnits() throws Exception {
        ISSResponse response = WhereTheISSHandler.fetchISS("kilometers");
        assertNotNull(response, "Response should not be null");
        assertEquals(25544, response.getId(), "ID should be 25544");
        assertEquals("iss", response.getName(), "Name should be 'iss'");
        assertTrue(response.getLatitude() >= -90 && response.getLatitude() <= 90, "Latitude should be between -90 and 90");
        assertTrue(response.getLongitude() >= -180 && response.getLongitude() <= 180, "Longitude should be between -180 and 180");
    }

    @Test
    public void testFetchISSWithUnitsAndTimestamp() throws Exception {
        long timestamp = Instant.now().getEpochSecond() - 3600; // 1 hour ago
        ISSResponse response = WhereTheISSHandler.fetchISS("kilometers", timestamp);
        assertNotNull(response, "Response should not be null");
        assertEquals(25544, response.getId(), "ID should be 25544");
        assertEquals("iss", response.getName(), "Name should be 'iss'");
        assertTrue(response.getLatitude() >= -90 && response.getLatitude() <= 90, "Latitude should be between -90 and 90");
        assertTrue(response.getLongitude() >= -180 && response.getLongitude() <= 180, "Longitude should be between -180 and 180");
    }

    @Test
    public void testFetchISSPositions() throws Exception {
        List<Long> timestamps = Arrays.asList(
                Instant.now().getEpochSecond() - 7200, // 2 hours ago
                Instant.now().getEpochSecond() - 3600, // 1 hour ago
                Instant.now().getEpochSecond()
        );
        List<ISSResponse> responses = WhereTheISSHandler.fetchISSPositions(timestamps, "kilometers");
        assertNotNull(responses, "Responses should not be null");
        assertEquals(3, responses.size(), "Should return 3 responses");
        for (ISSResponse response : responses) {
            assertEquals(25544, response.getId(), "ID should be 25544");
            assertEquals("iss", response.getName(), "Name should be 'iss'");
            assertTrue(response.getLatitude() >= -90 && response.getLatitude() <= 90, "Latitude should be between -90 and 90");
            assertTrue(response.getLongitude() >= -180 && response.getLongitude() <= 180, "Longitude should be between -180 and 180");
        }
    }

    @Test
    public void testGetISSIcon() {
        assertNotNull(WhereTheISSHandler.getISSIcon(), "ISS Icon should not be null");
    }
}
