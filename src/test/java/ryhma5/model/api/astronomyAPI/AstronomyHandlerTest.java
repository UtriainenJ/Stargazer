package ryhma5.model.api.astronomyAPI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ryhma5.model.api.astronomyAPI.AstronomyHandler;
import ryhma5.model.api.astronomyAPI.AstronomyResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AstronomyHandlerTest {

    @BeforeAll
    public static void setUp() {
        AstronomyHandler.loadAPICredentials();
    }

    @Test
    public void testFetchAstronomyEvent() throws Exception {
        String body = "moon";
        String latitude =  "48.8566";
        String longitude = " 2.3522";
        String elevation = "70";
        String fromDate = "2023-10-01";
        String toDate = "2023-10-02";
        String time = "12:00:00";

        ArrayList<AstronomyResponse> events = AstronomyHandler.fetchAstronomyEvent(
                body, latitude, longitude, elevation, fromDate, toDate, time);

        assertNotNull(events, "Events should not be null");
        assertFalse(events.isEmpty(), "Events list should not be empty");

        for (AstronomyResponse event : events) {
            assertEquals(body, event.getBodyId(), "Body ID should match the requested body");
            assertNotNull(event.getEventType(), "Event type should not be null");
            assertNotNull(event.getDateTime(), "Event date and time should not be null");
        }
    }

    @Test
    public void testFetchAstronomyBody() throws Exception {
        String body = "mars";
        String latitude =  "48.8566";
        String longitude = " 2.3522";
        String elevation = "70";
        String fromDate = "2023-10-01";
        String toDate = "2023-10-01";
        String time = "22:00:00";

        ArrayList<AstronomyResponse> positions = AstronomyHandler.fetchAstronomyBody(
                body, latitude, longitude, elevation, fromDate, toDate, time);

        assertNotNull(positions, "Positions should not be null");
        assertFalse(positions.isEmpty(), "Positions list should not be empty");

        for (AstronomyResponse position : positions) {
            assertEquals(body, position.getBodyId(), "Body ID should match the requested body");
            assertNotNull(position.getAzimuth(), "Azimuth should not be null");
            assertNotNull(position.getAltitude(), "Altitude should not be null");
            assertNotNull(position.getDateTime(), "Date and time should not be null");
        }
    }

    @Test
    public void testFetchAllBodies() throws Exception {
        String latitude =  "48.8566";
        String longitude = " 2.3522";
        String elevation = "11";
        String fromDate = "2023-10-01";
        String toDate = "2023-10-01";
        String time = "00:00:00";

        ArrayList<AstronomyResponse> responses = AstronomyHandler.fetchAllBodies(
                latitude, longitude, elevation, fromDate, toDate, time);

        assertNotNull(responses, "Responses should not be null");
        assertFalse(responses.isEmpty(), "Responses list should not be empty");

        for (AstronomyResponse response : responses) {
            assertNotNull(response.getBodyId(), "Body ID should not be null");
            assertNotNull(response.getAzimuth(), "Azimuth should not be null");
            assertNotNull(response.getAltitude(), "Altitude should not be null");
            assertNotNull(response.getDateTime(), "Date and time should not be null");
        }
    }

    /**
     * Flaky test because the API is sloooow
     *
     * @throws Exception
     */
    @Test
    public void testGenerateConstellationStarChart() throws Exception {
        double latitude = 48.8566;
        double longitude = 2.3522;
        String date = "2023-10-01";
        String constellationId = "ori";

        String imageUrl = AstronomyHandler.generateConstellationStarChart(
                latitude, longitude, date, constellationId);

        assertNotNull(imageUrl, "Image URL should not be null");
        assertTrue(imageUrl.startsWith("http"), "Image URL should start with 'http'");
    }

    /**
     * Flaky test because the API is sloooow
     *
     * @throws Exception
     */
    @Test
    public void testGenerateAreaStarChart() throws Exception {
        double latitude = 48.8566;
        double longitude = 2.3522;
        String date = "2023-10-01";
        double rightAscension = 5.0;
        double declination = -5.0;
        Integer zoom = 2;

        String imageUrl = AstronomyHandler.generateAreaStarChart(
                latitude, longitude, date, rightAscension, declination, zoom);

        assertNotNull(imageUrl, "Image URL should not be null");
        assertTrue(imageUrl.startsWith("http"), "Image URL should start with 'http'");
    }

    @Test
    public void testGenerateMoonPhaseImage() throws Exception {
        double latitude = 48.8566;
        double longitude = 2.3522;
        String date = "2023-10-01";
        String format = "png";

        String imageUrl = AstronomyHandler.generateMoonPhaseImage(
                latitude, longitude, date, format);

        assertNotNull(imageUrl, "Image URL should not be null");
        assertTrue(imageUrl.startsWith("http"), "Image URL should start with 'http'");
    }
}
