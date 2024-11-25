package ryhma5.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ryhma5.model.json.DataManager;
import ryhma5.model.map.Projections;
import ryhma5.model.map.SVGMap;
import ryhma5.model.map.SVGMapFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class MapController {
    private final MainViewController mainViewController;
    private final Projections PROJECTION;
    @FXML
    Pane mapPane;
    //private List<Marker> markers = null;
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

        Platform.runLater(()->{
            loadMapMarkers();
        });
    }

    void handleMapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        System.out.println("Clicked X: " + x + ", Y: " + y);

        svgMap.addMarker(x, y, mapImageView, mapPane, mainViewController.getSearchField());

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        double[] latLong = svgMap.getLatLongFromXY(x, y, imageWidth, imageHeight);

        DecimalFormat df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
        BigDecimal latitude = BigDecimal.valueOf(latLong[0]).setScale(2, RoundingMode.HALF_UP);
        BigDecimal longitude = BigDecimal.valueOf(latLong[1]).setScale(2, RoundingMode.HALF_UP);

        String formattedCoordinates = df.format(latitude) + ", " + df.format(longitude);

        mainViewController.setSearchField(formattedCoordinates);
    }

    /**
     * load all map markers from data file
     */
    public  void loadMapMarkers(){

        List<double[]> markersCoord = DataManager.loadDataAsList("map_markers", double[].class);

        if(markersCoord == null){return;}

        for(double[] markerCoord : markersCoord){
            System.out.println("coord, lat " + markerCoord[0] + ", long " + markerCoord[1] );
            svgMap.addMarkerByCoordinates(markerCoord[0], markerCoord[1], mapImageView, mapPane, mainViewController.getSearchField());
        }
    }

    /**
     * call svgMap to save map markers
     */
    public void saveMapMarkers() {
        svgMap.saveMarkersAsJson();
    }
}