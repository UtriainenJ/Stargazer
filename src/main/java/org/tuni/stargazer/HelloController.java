package org.tuni.stargazer;

import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox rootPane;

    @FXML
    private void initialize() {
        welcomeText.setText("initialize");
        MapView mv = createMapView();
        rootPane.getChildren().add(mv);


    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    private MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(400, 400);
        return mapView;
    }
}