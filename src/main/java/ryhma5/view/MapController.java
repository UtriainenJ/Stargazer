package ryhma5.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ryhma5.model.AstronomyEvent;
import ryhma5.model.Projections;
import ryhma5.viewmodel.AstronomyViewModel;
import ryhma5.viewmodel.SVGMap;

public class MapController {

    @FXML
    private Pane mapPane;

    private final Projections PROJECTION = Projections.EQUIRECTANGULAR;
    private String MAP_FILE_PATH = PROJECTION.getMapFilePath();
    private SVGMap svgMap;
    private ImageView mapImageView;


    @FXML
    public void initialize() {
        // Initialize SVGMap with the selected projection (e.g., Robinson)
        svgMap = new SVGMap(MAP_FILE_PATH, PROJECTION);

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

        System.out.println("---------------------------- API TEST ------------------------------------");
        AstronomyViewModel avm = new AstronomyViewModel();
        AstronomyEvent eve = avm.getAstronomyEvent(
                Double.toString(latLong[0]), Double.toString(latLong[1]), "10", "2024-10-07", "12:00:00");
        System.out.println(eve.getData().getObserver().getLocation().getLatitude());
        System.out.println(eve.getData().getObserver().getLocation().getLongitude());

        System.out.println("---------------------------------------------------------------------------");
    }


}
