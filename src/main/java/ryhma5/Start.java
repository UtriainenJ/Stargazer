package ryhma5;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ryhma5.controller.MainViewController;
import ryhma5.controller.WhereISSController;

public class Start extends Application {

    boolean resizing = false;
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        this.fxmlLoader = new FXMLLoader(MainViewController.class.getResource("/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 800);


        MainViewController mainController = fxmlLoader.getController();
        WhereISSController issController = WhereISSController.getInstance();
        issController.initialize();  // Manually call initialize to set up the ImageView

        // Inject issController into mainController
        mainController.setISSController(issController);

        stage.setMinWidth(800);
        stage.setMinHeight(400);


        stage.setTitle("Ryhmä 5");
        stage.setScene(scene);
        stage.setOnCloseRequest(this::onCloseRequest);
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



    private void onCloseRequest(WindowEvent event) {
        MainViewController mainViewController = fxmlLoader.getController();
        mainViewController.saveUserData();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
