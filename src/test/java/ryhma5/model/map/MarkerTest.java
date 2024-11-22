package ryhma5.model.map;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkerTest {

    private Marker marker;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        marker = new Marker(0.5, 0.5, 10.0, 60.0, 24.0);
    }

    @Test
    void testMarkerInitialization() {
        Circle circle = marker.getCircle();
        assertNotNull(circle, "Circle should be initialized");
        assertEquals(10.0, circle.getRadius(), "Circle should have the correct radius");

        // Test default fill
        if (MarkerIconManager.getIconImage() == null) {
            assertEquals(Color.RED, circle.getFill(), "Default fill should be RED when no icon image is available");
        }
    }

    @Test
    void testSelectMarker() {
        marker.selectMarker();
        Circle circle = marker.getCircle();

        if (MarkerIconManager.getSelectedIconImage() != null) {
            assertTrue(circle.getFill() instanceof javafx.scene.paint.ImagePattern, "Selected fill should use the selected icon image");
        } else {
            assertEquals(Color.YELLOW, circle.getFill(), "Default selected fill should be YELLOW when no selected icon image is available");
        }
        assertTrue(marker.isMarkerSelected(), "Marker should be marked as selected");
    }

    @Test
    void testDeselectMarker() {
        marker.selectMarker();
        marker.deSelectMarker();
        Circle circle = marker.getCircle();

        if (MarkerIconManager.getIconImage() != null) {
            assertTrue(circle.getFill() instanceof javafx.scene.paint.ImagePattern, "Deselected fill should use the default icon image");
        } else {
            assertEquals(Color.RED, circle.getFill(), "Default deselected fill should be RED when no icon image is available");
        }
        assertFalse(marker.isMarkerSelected(), "Marker should be marked as deselected");
    }

    @Test
    void testGetters() {
        assertEquals(0.5, marker.getRelativeX(), "Relative X coordinate should match the input");
        assertEquals(0.5, marker.getRelativeY(), "Relative Y coordinate should match the input");
        assertEquals(60.0, marker.getLat(), "Latitude should match the input");
        assertEquals(24.0, marker.getLong(), "Longitude should match the input");
    }
}
