package ryhma5.view;

import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import ryhma5.model.User;
import ryhma5.viewmodel.UserViewModel;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox rootPane;

    @FXML
    TextField nameField;

    @FXML
    TextField ageField;

    @FXML
    private void initialize() {
        welcomeText.setText("initialize");
        MapView mv = createMapView();
        rootPane.getChildren().add(mv);

        User user = new User("John Doe", 30);
        UserViewModel userViewModel = new UserViewModel(user);

        nameField.textProperty().bindBidirectional(userViewModel.nameProperty());
        // Create a StringConverter for the age property
        StringConverter<Number> converter = new NumberStringConverter();
        ageField.textProperty().bindBidirectional(userViewModel.ageProperty(), converter);

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