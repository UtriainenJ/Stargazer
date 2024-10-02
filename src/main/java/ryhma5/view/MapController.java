package ryhma5.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.WorldMapView;

public class MapController {

    @FXML
    Button confirmButton;

    @FXML
    TextField coordinateInput;

    @FXML
    AnchorPane mapPane;

    WorldMapView worldMapView = new WorldMapView();

    @FXML
    private void initialize() {
        mapPane.getChildren().add(worldMapView);

        // Disable dragging (and everything else)
        //worldMapView.setMouseTransparent(true);

        // Add listeners to adjust the aspect ratio
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> adjustAspectRatio());
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> adjustAspectRatio());
        // anchor the mapview to mappane
        AnchorPane.setBottomAnchor(worldMapView, 0.0);
        AnchorPane.setTopAnchor(worldMapView, 0.0);
        AnchorPane.setLeftAnchor(worldMapView, 0.0);
        AnchorPane.setRightAnchor(worldMapView, 0.0);


        // deep blue land
        worldMapView.setStyle("-fx-background-color: #01001f");

        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> worldMapView.setPrefWidth(newVal.doubleValue()));
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> worldMapView.setPrefHeight(newVal.doubleValue()));


        configureCountryAndLocationViewFactories();

    }

    private void configureCountryAndLocationViewFactories() {
        worldMapView.setCountryViewFactory(country -> {
            WorldMapView.CountryView view = new WorldMapView.CountryView(country);
            view.setStyle("-fx-fill: #00468c;"); // Night blue color
            return view;
        });

        worldMapView.setLocationViewFactory(location -> {
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
        double width = mapPane.getWidth();
        double height = mapPane.getHeight();

        if (width / height > 198.0 / 120.0) {
            worldMapView.setPrefWidth(height * 198.0 / 120.0);
            worldMapView.setPrefWidth(height);
        } else {
            worldMapView.setPrefWidth(width);
            worldMapView.setPrefHeight(width * 120.0 / 198.0);
        }

    }

    private void printCoordinates(MouseEvent event) {
        // print the coordinates of the mouse click
        System.out.println(event.getX() + ", " + event.getY());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void setCoordinates(ActionEvent event) {
        // when confirm button is pressed, coordinateInput is read and printed
        event.consume();
        System.out.println(coordinateInput.getText());

        // Parse the coordinates from the input
        String[] coords = coordinateInput.getText().split(",");
        if (coords.length == 2) {
            try {
                double latitude = Double.parseDouble(coords[0].trim());
                double longitude = Double.parseDouble(coords[1].trim());

                // Validate the coordinates
                if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                    throw new NumberFormatException("Coordinates out of range");
                }

                // Create a new location and add it to the map
                WorldMapView.Location location = new WorldMapView.Location("Marker", latitude, longitude);
                worldMapView.getLocations().add(location);
            } catch (NumberFormatException e) {
                showAlert("Invalid coordinates format. Latitude must be between -90 and 90, and longitude must be between -180 and 180.");
            }
        } else {
            showAlert("Invalid coordinates format. Please enter coordinates in the format: latitude, longitude.");
        }
    }
}