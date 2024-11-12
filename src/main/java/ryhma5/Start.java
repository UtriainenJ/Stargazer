package ryhma5;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ryhma5.controller.AstronomyController;
import ryhma5.controller.MainViewController;
import ryhma5.model.AstronomyAPI;
import ryhma5.model.SVGMap;

public class Start extends Application {

    private FXMLLoader fxmlLoader;

    // boolean to prevent both width and height listeners from running at the same time.
    // still causes some flickering :/
    private boolean adjusting = false;

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(MainViewController.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 800);

        stage.setMinWidth(800);
        stage.setMinHeight(400);


        stage.setTitle("Ryhmä 5");
        stage.setScene(scene);
        stage.setOnCloseRequest(this::onCloseRequest);
        stage.show();

    }

    private void onCloseRequest(WindowEvent event) {
        AstronomyController astronomyController = new AstronomyController();
        astronomyController.saveAstronomyResponses();
        MainViewController mainViewController = fxmlLoader.getController();
        mainViewController.saveMapMarkers();
    }

    public static void main(String[] args) {
        launch();
    }
}
 