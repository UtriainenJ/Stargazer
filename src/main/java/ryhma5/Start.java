package ryhma5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ryhma5.view.MainViewController;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Start extends Application {

    private static Locale defaultLocale = new Locale("en"); // fi or en

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainViewController.class.getResource("main-view.fxml"));

        try {
            String localizationPath = "localization." + defaultLocale.getLanguage();
            ResourceBundle bundle = ResourceBundle.getBundle(localizationPath);
            fxmlLoader.setResources(bundle);
        } catch (Exception e) {
            System.err.println("Resource bundle not found for locale: " + Locale.getDefault().getLanguage());
        }

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setMinWidth(1280);
        stage.setMinHeight(720);

        stage.setTitle("Ryhmä 5");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
