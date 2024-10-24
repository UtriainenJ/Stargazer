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
    private boolean isSelected = false;

    private static final String ICON_PATH = "/icons/mariostar_stillnoeyes.gif";
    private static final String SELECTED_ICON_PATH = "/icons/slowerstar.gif";
    private static Image iconImage = null;
    private static Image selectedIconImage = null;
    private static boolean iconLoadAttempted = false;

    static {
        // Try to load the icon once
        tryLoadIcons();
    }

    private static void tryLoadIcons() {
        if (!iconLoadAttempted) {
            InputStream iconStream = Marker.class.getResourceAsStream(ICON_PATH);
            InputStream selectedIconStream = Marker.class.getResourceAsStream(SELECTED_ICON_PATH);

            if (iconStream != null) {
                iconImage = new Image(iconStream);
            }
            if (selectedIconStream != null) {
                selectedIconImage = new Image(selectedIconStream);
            }

            if (iconImage == null || selectedIconImage == null) {
                System.err.println("Failed to load one or more marker icons");
            }

            iconLoadAttempted = true;
        }
    }


    public Marker(double relativeX, double relativeY, double radius) {
        this.circle = createStyledMarkerCircle(radius);
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    private Circle createStyledMarkerCircle(double radius) {

        Circle circle = new Circle(radius);

        if (iconImage != null) {
            circle.setFill(new ImagePattern(iconImage));
        } else {
            circle.setFill(Color.RED);
        }

        return circle;
    }

    public void selectMarker() {
        if (!isSelected) {
            if (selectedIconImage != null) {
                circle.setFill(new ImagePattern(selectedIconImage));
            } else {
                circle.setFill(Color.YELLOW);
            }
            isSelected = true;
        }
    }

    public void deSelectMarker() {
        if (isSelected) {
            if (iconImage != null) {
                circle.setFill(new ImagePattern(iconImage));
            } else {
                circle.setFill(Color.RED);
            }
            isSelected = false;
        }
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
}
