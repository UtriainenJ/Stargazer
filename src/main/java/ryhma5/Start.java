package ryhma5;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ryhma5.controller.MainViewController;

public class Start extends Application {

    // boolean to prevent both width and height listeners from running at the same time.
    // still causes some flickering :/
    private boolean adjusting = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainViewController.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);

        stage.setMinWidth(800);
        stage.setMinHeight(400);

        // Add a listener to enforce the 2:1 aspect ratio
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!adjusting) {
                adjusting = true;
                double newWidth = newVal.doubleValue();
                stage.setHeight(newWidth / 2);
                adjusting = false;
            }
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!adjusting) {
                adjusting = true;
                double newHeight = newVal.doubleValue();
                stage.setWidth(newHeight * 2);
                adjusting = false;
            }
        });


        stage.setTitle("Ryhmä 5");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
 