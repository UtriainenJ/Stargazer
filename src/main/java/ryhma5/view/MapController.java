package ryhma5.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ryhma5.model.Projections;
import ryhma5.viewmodel.SVGMap;

public class MapController {

    @FXML
    private Pane mapPane;

    private final String SVG_FILE_PATH = "/images/BlankMap_World_simple_Robinson_projection.svg";
    private SVGMap svgMap;
    private ImageView mapImageView;

    @FXML
    public void initialize() {
        // Initialize SVGMap with the selected projection (e.g., Robinson)
        svgMap = new SVGMap(SVG_FILE_PATH, Projections.ROBINSON);

        // Load the map and set it in the Pane
        mapImageView = svgMap.loadMap(mapPane);
        if (mapImageView != null) {
            mapPane.getChildren().add(mapImageView);

            // Register mouse click event
            mapImageView.setPickOnBounds(true);
            mapImageView.setOnMouseClicked(this::handleMapClick);
        }

        // Reposition markers when the window is resized
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> svgMap.updateMarkers(mapImageView));
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> svgMap.updateMarkers(mapImageView));
    }

    // Handle click events to place markers
    private void handleMapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Print the clicked pixel coordinates
        System.out.println("Clicked X: " + x + ", Y: " + y);

        // Delegate marker creation to SVGMap
        svgMap.addMarker(x, y, mapImageView, mapPane);

        // Get the dimensions of the image
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        // Convert pixel coordinates to latitude/longitude using the projector
        double[] latLong = svgMap.getLatLongFromXY(x, y, imageWidth, imageHeight);

        // Print the real-world coordinates
        System.out.println("Latitude: " + latLong[0] + ", Longitude: " + latLong[1]);
    }


}
