package ryhma5.model.api.astronomyAPI;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class AstronomyResponseTest {

    @Test
    void testGettersAndSetters() {
        // Prepare test data
        OffsetDateTime testDateTime = OffsetDateTime.of(2024, 11, 20, 12, 0, 0, 0, ZoneOffset.UTC);
        String testBodyName = "Moon";
        String testBodyId = "MOON-001";
        String testDistanceFromEarth = "384,400 km";
        String testConstellation = "Pisces";
        String testAltitude = "45°";
        String testAzimuth = "90°";
        double testElongation = 10.5;
        double testMagnitude = -12.74;
        String testMoonPhaseString = "Full Moon";
        double testMoonPhaseFraction = 1.0;
        String testEventType = "Eclipse";
        double testEventObscuration = 0.95;
        String testEventBodyRise = "18:30";
        String testEventBodySet = "06:15";
        String testEventPartialStart = "18:45";
        String testEventPartialEnd = "06:00";
        String testEventTotalStart = "19:00";
        String testEventTotalEnd = "05:45";
        double testLongitude = 24.94;
        double testLatitude = 60.17;

        // Use the builder to construct an AstronomyResponse object
        AstronomyResponse response = new AstronomyResponse.Builder()
                .setDateTime(testDateTime)
                .setBodyName(testBodyName)
                .setBodyId(testBodyId)
                .setDistanceFromEarth(testDistanceFromEarth)
                .setConstellation(testConstellation)
                .setAltitude(testAltitude)
                .setAzimuth(testAzimuth)
                .setElongation(testElongation)
                .setMagnitude(testMagnitude)
                .setMoonPhaseString(testMoonPhaseString)
                .setMoonPhaseFraction(testMoonPhaseFraction)
                .setEventType(testEventType)
                .setEventObscuration(testEventObscuration)
                .setEventBodyRise(testEventBodyRise)
                .setEventBodySet(testEventBodySet)
                .setEventPartialStart(testEventPartialStart)
                .setEventPartialEnd(testEventPartialEnd)
                .setEventTotalStart(testEventTotalStart)
                .setEventTotalEnd(testEventTotalEnd)
                .setObserverLongitude(testLongitude)
                .setObserverLatitude(testLatitude)
                .build();

        // Validate getters
        assertEquals(testDateTime, response.getDateTime());
        assertEquals(testBodyName, response.getBodyName());
        assertEquals(testBodyId, response.getBodyId());
        assertEquals(testDistanceFromEarth, response.getDistanceFromEarth());
        assertEquals(testConstellation, response.getConstellation());
        assertEquals(testAltitude, response.getAltitude());
        assertEquals(testAzimuth, response.getAzimuth());
        assertEquals(testElongation, response.getElongation());
        assertEquals(testMagnitude, response.getMagnitude());
        assertEquals(testMoonPhaseString, response.getMoonPhaseString());
        assertEquals(testMoonPhaseFraction, response.getMoonPhaseFraction());
        assertEquals(testEventType, response.getEventType());
        assertEquals(testEventObscuration, response.getEventObscuration());
        assertEquals(testEventBodyRise, response.getEventBodyRise());
        assertEquals(testEventBodySet, response.getEventBodySet());
        assertEquals(testEventPartialStart, response.getEventPartialStart());
        assertEquals(testEventPartialEnd, response.getEventPartialEnd());
        assertEquals(testEventTotalStart, response.getEventTotalStart());
        assertEquals(testEventTotalEnd, response.getEventTotalEnd());
        assertEquals(testLongitude, response.getObserverLongitude());
        assertEquals(testLatitude, response.getObserverLatitude());
    }
}
