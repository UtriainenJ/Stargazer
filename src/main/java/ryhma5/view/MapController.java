package ryhma5.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import ryhma5.viewmodel.SVGMap;

import java.util.ArrayList;
import java.util.List;

public class MapController {

    @FXML
    private Pane mapPane; // This is the Pane from your map-view.fxml

    private final String SVG_FILE_PATH = "/images/BlankMap_World_simple_Robinson_projection.svg";
    private SVGMap svgMap;
    private ImageView mapImageView;

    // Store markers with their relative positions (relative to image dimensions)
    private final List<Marker> markers = new ArrayList<>();

    @FXML
    public void initialize() {
        // Initialize SVGMap
        svgMap = new SVGMap(SVG_FILE_PATH);

        // Load the map image and bind it to the Pane's size
        mapImageView = svgMap.loadMap(mapPane);
        if (mapImageView != null) {
            mapPane.getChildren().add(mapImageView);

            // Add mouse click event handler to the image (register clicks on the entire map)
            mapImageView.setPickOnBounds(true);  // Ensure clicks register even in transparent areas
            mapImageView.setOnMouseClicked(this::handleMapClick);
        }

        // Reposition markers when the Pane size changes
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> updateMarkers());
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> updateMarkers());
    }

    // Method to handle clicks on the map
    private void handleMapClick(MouseEvent event) {
        // Get the X and Y position of the click relative to the ImageView
        double x = event.getX();
        double y = event.getY();

        // Convert the X, Y position to latitude and longitude
        double[] latLong = svgMap.xyToLatLong(x, y, mapImageView);

        // Print the latitude and longitude (for debugging)
        System.out.println("Clicked Latitude: " + latLong[0] + ", Longitude: " + latLong[1]);

        // Get the current image dimensions
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        // Calculate the relative position of the marker (relative to the image size)
        double relativeX = x / imageWidth;
        double relativeY = y / imageHeight;

        // Add a marker (red dot) at the clicked pixel position (X, Y)
        Circle marker = new Circle(x, y, 5);
        marker.setFill(javafx.scene.paint.Color.RED);
        markers.add(new Marker(marker, relativeX, relativeY));  // Save the marker with its relative position
        mapPane.getChildren().add(marker);
    }

    // Update the marker positions when the image size changes
    private void updateMarkers() {
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        for (Marker marker : markers) {
            // Recalculate the marker's position based on its stored relative position
            double newX = marker.getRelativeX() * imageWidth;
            double newY = marker.getRelativeY() * imageHeight;

            // Update marker's X and Y position based on the resized image
            marker.getCircle().setCenterX(newX);
            marker.getCircle().setCenterY(newY);
        }
    }

    // Helper class to store markers with their relative positions
    public static class Marker {
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
}
