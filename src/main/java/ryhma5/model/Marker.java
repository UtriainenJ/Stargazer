package ryhma5.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.InputStream;

public class Marker {
    private final Circle circle;
    private final double relativeX;
    private final double relativeY;

    private static final String ICON_PATH = "/images/slowerstar.gif";
    private static final double SIZE_PERCENTAGE = 0.03; // 3% of the map image's smaller dimension
    private static Image iconImage = null; // Static field to hold the icon image
    private static boolean iconLoadAttempted = false; // Track if the load was attempted

    static {
        // Try to load the icon once
        tryLoadIcon();
    }

    private static void tryLoadIcon() {
        if (!iconLoadAttempted) {
            InputStream iconStream = Marker.class.getResourceAsStream(ICON_PATH);
            if (iconStream != null) {
                iconImage = new Image(iconStream);
            } else {
                System.err.println("Failed to load marker icon from " + ICON_PATH);
            }
            iconLoadAttempted = true; // Mark that we've attempted the load
        }
    }

    public Marker(double relativeX, double relativeY, double imageWidth, double imageHeight) {
        this.circle = createStyledCircle(imageWidth, imageHeight);
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    private Circle createStyledCircle(double imageWidth, double imageHeight) {
        // Calculate the radius as 3% of the smaller dimension of the image
        double radius = SIZE_PERCENTAGE * Math.min(imageWidth, imageHeight);

        Circle circle = new Circle(radius); // Set the initial radius

        // Use the loaded icon if available, otherwise use red as fallback
        if (iconImage != null) {
            circle.setFill(new ImagePattern(iconImage));
        } else {
            circle.setFill(Color.RED);
        }

        return circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    // Static method to calculate the radius based on the map size
    public static double calculateRadius(double imageWidth, double imageHeight) {
        return SIZE_PERCENTAGE * Math.min(imageWidth, imageHeight); // 3% of the smaller dimension
    }
}
