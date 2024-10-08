package ryhma5.viewmodel;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.WorldMapView;

public class ControlMap {

    WorldMapView worldmap;

    public ControlMap(WorldMapView worldmap) {
        this.worldmap = worldmap;
    }


    private void configureCountryAndLocationViewFactories() {
        worldmap.setCountryViewFactory(country -> {
            WorldMapView.CountryView view = new WorldMapView.CountryView(country);
            view.setStyle("-fx-fill: #00468c;"); // Night blue color
            return view;
        });

        worldmap.setLocationViewFactory(location -> {
            Image starIcon = new Image(getClass().getResourceAsStream("/icons/slowerstar.gif"));
            ImageView starIconView = new ImageView(starIcon);
            starIconView.setFitWidth(20);  // Set the width of the icon
            starIconView.setFitHeight(20); // Set the height of the icon
            starIconView.setTranslateX(-8); // Center the icon horizontally
            starIconView.setTranslateY(-8); // Center the icon vertically
            return starIconView;
        });
    }

    private void adjustAspectRatio() {
        // map should always be 198:120 aspect ratio
        // while being as large as possible
//        double width = mapPane.getWidth();
//        double height = mapPane.getHeight();
        double width = 1920;
        double height = 1080;
        if (width / height > 198.0 / 120.0) {
            worldmap.setPrefWidth(height * 198.0 / 120.0);
            worldmap.setPrefWidth(height);
        } else {
            worldmap.setPrefWidth(width);
            worldmap.setPrefHeight(width * 120.0 / 198.0);
        }

    }

    private void printCoordinates(long lat, long lon) {
        // print the coordinates of the mouse click
        //System.out.println(event.getX() + ", " + event.getY());
    }

    private void addLocation(long lat, long lon) {

        try {
            // Validate the coordinates
            if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                throw new NumberFormatException("Coordinates out of range");
            }

            // Create a new location and add it to the map
            WorldMapView.Location location = new WorldMapView.Location("Marker", lat, lon);
            worldmap.getLocations().add(location);
        } catch (NumberFormatException e) {
            //showAlert("Invalid coordinates format. Latitude must be between -90 and 90, and longitude must be between -180 and 180.§");
        }
    }

//    @FXML
//    private void setCoordinates(ActionEvent event) {
//        // when confirm button is pressed, coordinateInput is read and printed
//        event.consume();
//        System.out.println(coordinateInput.getText());
//
//        // Parse the coordinates from the input
//        String[] coords = coordinateInput.getText().split(",");
//        if (coords.length == 2) {
//            try {
//                double latitude = Double.parseDouble(coords[0].trim());
//                double longitude = Double.parseDouble(coords[1].trim());
//
//                // Validate the coordinates
//                if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
//                    throw new NumberFormatException("Coordinates out of range");
//                }
//
//                // Create a new location and add it to the map
//                WorldMapView.Location location = new WorldMapView.Location("Marker", latitude, longitude);
//                worldMapView.getLocations().add(location);
//            } catch (NumberFormatException e) {
//                showAlert("Invalid coordinates format. Latitude must be between -90 and 90, and longitude must be between -180 and 180.§");
//            }
//        } else {
//            showAlert("Invalid coordinates format. Please enter coordinates in the format: latitude, longitude.");
//        }
//
//    }
}
