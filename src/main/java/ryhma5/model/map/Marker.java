package ryhma5.model.map;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Marker {
    private final Circle circle;
    private final double relativeX;
    private final double relativeY;
    private boolean isSelected = false;

    public Marker(double relativeX, double relativeY, double radius) {
        this.circle = createStyledMarkerCircle(radius);
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    private Circle createStyledMarkerCircle(double radius) {
        Circle circle = new Circle(radius);
        Image iconImage = MarkerIconManager.getIconImage();

        if (iconImage != null) {
            circle.setFill(new ImagePattern(iconImage));
        } else {
            circle.setFill(Color.RED);
        }

        return circle;
    }

    public void selectMarker() {
        if (!isSelected) {
            Image selectedIconImage = MarkerIconManager.getSelectedIconImage();
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
            Image iconImage = MarkerIconManager.getIconImage();
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