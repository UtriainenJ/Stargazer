package ryhma5.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ryhma5.model.*;
import ryhma5.viewmodel.AstronomyViewModel;
import ryhma5.viewmodel.SVGMap;
import ryhma5.viewmodel.WhereISSViewModel;

import java.util.ArrayList;
import java.util.List;

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


        /*
        System.out.println("---------------------------- API TEST ------------------------------------");
        AstronomyViewModel avm = new AstronomyViewModel();
        AstronomyEvent testEvent = avm.getAstronomyEvent(
                "sun", Double.toString(latLong[0]), Double.toString(latLong[1]), "10",
                "2024-10-07","2024-10-08", "12:00:00");

        System.out.println("Obsever lon: " + testEvent.getData().getObserver().getLocation().getLatitude());
        System.out.println("Observer lat: " + testEvent.getData().getObserver().getLocation().getLongitude());
        System.out.println("Test event type: " + testEvent.getData().getTable().getRows().get(0).getCells()[0].getType());

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxx       BODIES       xxxxxxxxxxxxxxxxxxxxxxxxxx");
        String bodyIdToTestFor = "saturn";
        AstronomyBody testBody = avm.getAstronomyBody(
                bodyIdToTestFor, Double.toString(latLong[0]), Double.toString(latLong[1]), "10",
                "2024-10-07","2024-10-08", "12:00:00");
        System.out.println("Testing getAstronomyBody - Distance from Earth to " + bodyIdToTestFor + ":"
                + testBody.getData().getRows().get(0).getPositions().get(0).getDistance().getFromEarth().getKm());

        AstronomyBody testBody2 = avm.getAllAstronomyBodies(Double.toString(latLong[0]), Double.toString(latLong[1]),
                "10", "2024-10-07","2024-10-08", "12:00:00");

        int bodyIndexToTestFor = 4;
        System.out.println("Body (" + bodyIndexToTestFor + ") from getAllBodies: "
                + testBody2.getData().getRows().get(bodyIndexToTestFor).getBody().getName());

        //String constellationChartURL = avm.getConstellationStarChart(latLong[0], latLong[1],
        //        "2024-10-07", "ori");
        String areaChartURL = avm.getAreaStarChart(latLong[0], latLong[1], "2024-10-07",
                14.83, -15.23, 3);
        //System.out.println(constellationChartURL);
        System.out.println(areaChartURL);


        System.out.println("ooooooooooooooooooooooooooooooo  ISS oooooooooooooooooooooooooooooooooooooo");

        WhereISSViewModel issVM = new WhereISSViewModel();

        ISSResponse issTest = issVM.getISS("kilometers", 1364069476L);
        System.out.println("ISS velocity at timestamp 1713240000: " + issTest.getVelocity());

        ArrayList<Long> issTestDates = new ArrayList<>();
        issTestDates.add(WhereISSAPI.dateToTimestamp("2024-10-07"));
        issTestDates.add(WhereISSAPI.dateToTimestamp("2024-10-08"));
        List<ISSResponse> issTestsList = issVM.getISSPositions(issTestDates, "kilometers");
        System.out.println("ISS altitude from get positions list: " + issTestsList.get(1).getAltitude());
        System.out.println("---------------------------------------------------------------------------");
        
         */
        

    }


}
