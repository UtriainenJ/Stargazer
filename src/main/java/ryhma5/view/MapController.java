package ryhma5.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
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

    // Store markers with their latitude and longitude
    private final List<Marker> markers = new ArrayList<>();

    @FXML
    public void initialize() {
        // Initialize SVGMap
        svgMap = new SVGMap(SVG_FILE_PATH);

        // Load the map image and bind it to the Pane's size
        mapImageView = svgMap.loadMap(mapPane);
        if (mapImageView != null) {
            mapPane.getChildren().add(mapImageView);

            // Add mouse click event handler to the image
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

        // Add a marker (red dot) at the clicked location
        Circle marker = (Circle) svgMap.addMarker(latLong[0], latLong[1], mapImageView);
        markers.add(new Marker(marker, latLong[0], latLong[1])); // Save the marker with its lat/long
        mapPane.getChildren().add(marker);
    }

    // Update the marker positions when the image size changes
    private void updateMarkers() {
        for (Marker marker : markers) {
            // Recalculate the marker's position based on the new image size
            double[] newCoords = svgMap.latLongToXY(marker.getLatitude(), marker.getLongitude(), mapImageView);
            marker.getCircle().setCenterX(newCoords[0]);
            marker.getCircle().setCenterY(newCoords[1]);
        }
    }

    // Helper class to store markers with their geographical data
    public static class Marker {
        private final Circle circle;
        private final double latitude;
        private final double longitude;

        public Marker(Circle circle, double latitude, double longitude) {
            this.circle = circle;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Circle getCircle() {
            return circle;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
