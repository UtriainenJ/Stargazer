package ryhma5.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ryhma5.model.Projections;
import ryhma5.model.SVGMap;
import ryhma5.controller.AstronomyController;
import ryhma5.controller.WhereISSController;
import ryhma5.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    private void handleMapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        System.out.println("Clicked X: " + x + ", Y: " + y);

        svgMap.addMarker(x, y, mapImageView, mapPane);

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        double[] latLong = svgMap.getLatLongFromXY(x, y, imageWidth, imageHeight);

        System.out.println("Latitude: " + latLong[0] + ", Longitude: " + latLong[1]);

        // Call sendAPIRequests asynchronously, little like promise await in javascript.
        CompletableFuture.runAsync(() -> sendAPIRequests(latLong[0], latLong[1]));

    }

    private void sendAPIRequests(double x, double y) {

        System.out.println("---------------------------- API TEST ------------------------------------");
        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwww    EVENTS    wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");

        AstronomyController avm = new AstronomyController();
        AstronomyEvent testEvent = avm.getAstronomyEvent(
                "sun", Double.toString(x), Double.toString(y), "10",
                "2024-10-07", "2024-10-08", "12:00:00");

        System.out.println("Obsever lon: " + testEvent.getData().getObserver().getLocation().getLatitude());
        System.out.println("Observer lat: " + testEvent.getData().getObserver().getLocation().getLongitude());
        System.out.println("Test event type: " + testEvent.getData().getTable().getRows().get(0).getCells()[0].getType());

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxx       BODIES       xxxxxxxxxxxxxxxxxxxxxxxxxx");
        String bodyIdToTestFor = "saturn";
        AstronomyBody testBody = avm.getAstronomyBody(
                bodyIdToTestFor, Double.toString(x), Double.toString(y), "10",
                "2024-10-07", "2024-10-08", "12:00:00");
        System.out.println("Testing getAstronomyBody - Distance from Earth to " + bodyIdToTestFor + ":"
                + testBody.getData().getRows().get(0).getPositions().get(0).getDistance().getFromEarth().getKm());

        AstronomyBody testBody2 = avm.getAllAstronomyBodies(Double.toString(x), Double.toString(y),
                "10", "2024-10-07", "2024-10-08", "12:00:00");

        int bodyIndexToTestFor = 4;
        System.out.println("Body (" + bodyIndexToTestFor + ") from getAllBodies: "
                + testBody2.getData().getRows().get(bodyIndexToTestFor).getBody().getName());

        //String constellationChartURL = avm.getConstellationStarChart(latLong[0], latLong[1],"2024-10-07", "ori");
        //System.out.println(constellationChartURL);
        String areaChartURL = avm.getAreaStarChart(x, y, "2024-10-07", 14.83, -15.23, 9);
        System.out.println(areaChartURL);


        System.out.println("ooooooooooooooooooooooooooo     ISS    ooooooooooooooooooooooooooooooooooo");

        WhereISSController issVM = new WhereISSController();

        ISSResponse issTest = issVM.getISS("kilometers", WhereISSAPI.dateToTimestamp("2024-10-07"));
        System.out.println("ISS velocity at 2024-10-7: " + issTest.getVelocity());

        ArrayList<Long> issTestDates = new ArrayList<>();
        issTestDates.add(WhereISSAPI.dateToTimestamp("2024-10-07"));
        issTestDates.add(WhereISSAPI.dateToTimestamp("2024-10-08"));
        List<ISSResponse> issTestsList = issVM.getISSPositions(issTestDates, "kilometers");
        System.out.println("ISS altitude from get positions list: " + issTestsList.get(1).getAltitude());
        System.out.println("---------------------------------------------------------------------------");
    }

}
