package ryhma5.model.map;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Marker {
    private final Circle circle;
    private final double relativeX;
    private final double relativeY;
    private final double latitude;
    private final double longitude;
    private boolean isSelected = false;


    public Marker(double relativeX, double relativeY, double radius, double latitude, double longitude) {
        this.circle = createStyledMarkerCircle(radius);
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Circle createStyledMarkerCircle(double radius) {
        Circle circle = new Circle(radius);
        Image iconImage = MarkerIconManager.getInstance().getIconImage();
        if (iconImage != null) {
            circle.setFill(new ImagePattern(iconImage));
        } else {
            circle.setFill(Color.RED);
        }

        return circle;
    }

    public void selectMarker() {
        if (!isSelected) {
            Image selectedIconImage = MarkerIconManager.getInstance().getSelectedIconImage();
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
            Image iconImage = MarkerIconManager.getInstance().getIconImage();
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

    public double getLat() {
        return latitude;
    }

    public double getLong() {
        return longitude;
    }

    public boolean isMarkerSelected() {
        return isSelected;
    }
}