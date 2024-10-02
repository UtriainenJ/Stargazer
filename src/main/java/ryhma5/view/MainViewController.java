package ryhma5.view;

import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import ryhma5.model.User;
import ryhma5.viewmodel.UserViewModel;

import java.io.IOException;

public class MainViewController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox rootPane;

    @FXML
    TextField nameField;

    @FXML
    TextField ageField;

    @FXML
    HBox hbox;



    @FXML
    private void initialize() {
        loadMap();

    }

    private void loadMap() {
        // Load and embed map-view.fxml into anchorRight
        try {
            StackPane mapview = FXMLLoader.load(getClass().getResource("map-view.fxml"));
            hbox.getChildren().add(mapview);
            // width and height of mappane should always be 16:9 aspect ratio and fill as much of hbox as possible
            mapview.prefWidthProperty().bind(hbox.widthProperty());
            mapview.prefHeightProperty().bind(hbox.heightProperty());


            //mapPane.minWidthProperty().bind(hbox.widthProperty());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

}