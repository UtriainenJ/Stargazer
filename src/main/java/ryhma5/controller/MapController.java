package ryhma5.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ryhma5.model.Projections;
import ryhma5.model.map.Marker;
import ryhma5.model.map.SVGMap;
import ryhma5.model.map.SVGMapFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MapController {
    private final MainViewController mainViewController;
    private final Projections PROJECTION;
    @FXML
    Pane mapPane;
    private List<Marker> markers = null;
    // map variables
    SVGMap svgMap;
    ImageView mapImageView;

    public MapController(MainViewController mainViewController, Pane mapPane, ImageView mapImageView) {
        this.mainViewController = mainViewController;
        this.mapImageView = mapImageView;
        this.mapPane = mapPane;
        // set the background image, setting it inside .fxml doesn't work well
        String imageUrl = getClass().getResource("/images/purple_dark_nebula.jpg").toExternalForm();
        mapPane.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover;");

        this.PROJECTION = Projections.EQUIRECTANGULAR;
    }

    void initializeMap() {
        svgMap = SVGMapFactory.createMap(PROJECTION);

        mapImageView = svgMap.loadMap(mapPane);
        if (mapImageView != null) {
            mapPane.getChildren().add(mapImageView);

            mapImageView.setPickOnBounds(true);
            mapImageView.setOnMouseClicked(mainViewController::handleMapClick);
        }

        // Reposition markers when the window is resized
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> svgMap.updateMarkers(mapImageView));
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> svgMap.updateMarkers(mapImageView));
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> mainViewController.getIssController().adjustToWindowSize());
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> mainViewController.getIssController().adjustToWindowSize());

        /*
        List<double[]> markersXY = dataManager.loadDataAsList("map_markers", double[].class);
        if(markersXY != null) {
            for (double[] markerXY : markersXY) {
                //System.out.println(markerXY[0] + " " + markerXY[1]);
                //svgMap.addMarker(markerXY[0], markerXY[1], mapImageView, mapPane);
            }
        }
         */
        //svgMap.addMarker(userPreferencesLoadData.getLatitude(), userPreferencesLoadData.getLongitude(), mapImageView, mapPane);
        //svgMap.addMarkerByCoordinates(userPreferencesLoadData.getLatitude(), userPreferencesLoadData.getLongitude(), mapImageView, mapPane);
    }

    void handleMapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        System.out.println("Clicked X: " + x + ", Y: " + y);

        svgMap.addMarker(x, y, mapImageView, mapPane);

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        double[] latLong = svgMap.getLatLongFromXY(x, y, imageWidth, imageHeight);

        System.out.println("Latitude: " + latLong[0] + ", Longitude: " + latLong[1]);

        CompletableFuture.runAsync(() -> mainViewController.sendAPIRequests(latLong[0], latLong[1], "2024-11-13"));
    }

    public void saveMapMarkers() {
        svgMap.saveMarkersAsJson();
    }
}