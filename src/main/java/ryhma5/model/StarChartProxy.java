package ryhma5.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryhma5.controller.AstronomyController;

public class StarChartProxy {
    private final ImageView imageView;
    private final AstronomyController astronomyController;
    private final double latitude;
    private final double longitude;
    private final String date;
    private final Image placeholderImage;

    public StarChartProxy(ImageView imageView, AstronomyController astronomyController,
                          double latitude, double longitude, String date) {
        this.imageView = imageView;
        this.astronomyController = astronomyController;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.placeholderImage = new Image(getClass().getResourceAsStream("/images/loading.png"));

        // Set the placeholder image initially
        this.imageView.setImage(this.placeholderImage);

        // Start loading the actual image in the background
        loadStarChartImageAsync();
    }

    private void loadStarChartImageAsync() {
        new Thread(() -> {
            // Get the URL of the star chart image asynchronously
            String starChartUrl = astronomyController.getAreaStarChart(latitude, longitude, date, 14.83, -15.23, 9);

            // Load the image from the URL
            Image actualImage = new Image(starChartUrl, true); // Asynchronously load the image
            actualImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                if (newProgress.doubleValue() >= 1.0) {
                    // Image has fully loaded, update on the JavaFX Application Thread
                    Platform.runLater(() -> imageView.setImage(actualImage));
                }
            });
        }).start();
    }
}
