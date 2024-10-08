package ryhma5.viewmodel;

import javafx.scene.shape.Circle;

public class Marker {
    private final Circle circle;
    private final double relativeX;
    private final double relativeY;

    public Marker(Circle circle, double relativeX, double relativeY) {
        this.circle = circle;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
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
