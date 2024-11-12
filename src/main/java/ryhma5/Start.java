package ryhma5;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ryhma5.controller.MainViewController;

public class Start extends Application {

    boolean resizing = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainViewController.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);

        stage.setMinWidth(800);
        stage.setMinHeight(400);

        // set window to 70% of screen size initially
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setWidth(bounds.getWidth() * 0.7);
        stage.setHeight(bounds.getHeight() * 0.7);




        stage.setTitle("Ryhmä 5");
        stage.setScene(scene);
        stage.show();

        maintainAspectRatio(stage, scene);
    }

    private void maintainAspectRatio(Stage stage, Scene scene) {
        // decoration: title bar, window border, etc.
        double decorationWidth = stage.getWidth() - scene.getWidth();
        double decorationHeight = stage.getHeight() - scene.getHeight();

        double contentWidth = scene.getWidth();
        double contentHeight = contentWidth / 2;
        stage.setHeight(contentHeight + decorationHeight);


        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!resizing) {
                resizing = true;
                double newWidth = newVal.doubleValue();
                double contentWidth1 = newWidth - decorationWidth;
                double contentHeight1 = contentWidth1 / 2;
                stage.setHeight(contentHeight1 + decorationHeight);
                resizing = false;
            }
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!resizing) {
                resizing = true;
                double newHeight = newVal.doubleValue();
                double contentHeight1 = newHeight - decorationHeight;
                double contentWidth1 = contentHeight1 * 2;
                stage.setWidth(contentWidth1 + decorationWidth);
                resizing = false;
            }
        });
    }



    public static void main(String[] args) {
        launch();
    }
}
