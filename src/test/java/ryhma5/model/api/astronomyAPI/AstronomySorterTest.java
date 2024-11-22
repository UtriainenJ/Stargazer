package ryhma5.model.api.astronomyAPI;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AstronomySorterTest {

    @Test
    void testGetBrightestBodies() {
        // Create test data
        ArrayList<AstronomyResponse> bodyList = new ArrayList<>();

        bodyList.add(new AstronomyResponse.Builder()
                .setBodyId("MOON")
                .setBodyName("Moon")
                .setMagnitude(-12.7)
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyId("MOON")
                .setBodyName("Moon")
                .setMagnitude(-12.6)
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyId("SUN")
                .setBodyName("Sun")
                .setMagnitude(-26.74)
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyId("VENUS")
                .setBodyName("Venus")
                .setMagnitude(-4.5)
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyId("VENUS")
                .setBodyName("Venus")
                .setMagnitude(-4.3)
                .build());

        // Test the method
        ArrayList<AstronomyResponse> brightestBodies = AstronomySorter.getBrightestBodies(bodyList);

        // Validate results
        assertEquals(3, brightestBodies.size());
        assertTrue(brightestBodies.stream().anyMatch(body -> body.getBodyId().equals("MOON") && body.getMagnitude() == -12.7));
        assertTrue(brightestBodies.stream().anyMatch(body -> body.getBodyId().equals("SUN") && body.getMagnitude() == -26.74));
        assertTrue(brightestBodies.stream().anyMatch(body -> body.getBodyId().equals("VENUS") && body.getMagnitude() == -4.5));
    }

    @Test
    void testRemoveBodies() {
        // Create test data
        ArrayList<AstronomyResponse> bodyList = new ArrayList<>();

        bodyList.add(new AstronomyResponse.Builder()
                .setBodyName("Moon")
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyName("Sun")
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyName("Venus")
                .build());
        bodyList.add(new AstronomyResponse.Builder()
                .setBodyName("Mars")
                .build());

        // Call the method to remove "Moon" and "Venus"
        AstronomySorter.removeBodies(bodyList, "moon", "venus");

        // Validate results
        assertEquals(2, bodyList.size());
        assertTrue(bodyList.stream().anyMatch(body -> body.getBodyName().equals("Sun")));
        assertTrue(bodyList.stream().anyMatch(body -> body.getBodyName().equals("Mars")));
        assertFalse(bodyList.stream().anyMatch(body -> body.getBodyName().equals("Moon")));
        assertFalse(bodyList.stream().anyMatch(body -> body.getBodyName().equals("Venus")));
    }
}
