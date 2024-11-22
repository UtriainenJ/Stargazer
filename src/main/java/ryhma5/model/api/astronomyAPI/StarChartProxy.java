package ryhma5.model.api.astronomyAPI;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryhma5.controller.AstronomyController;

import java.util.Timer;
import java.util.TimerTask;

public class StarChartProxy {
    private final ImageView imageView;
    private final AstronomyController astronomyController;
    private final double latitude;
    private final double longitude;
    private final String date;
    private final Image placeholderImage;
    private final Image failedImage;
    private static final int TIMEOUT_MS = 20000;

    public StarChartProxy(ImageView imageView, AstronomyController astronomyController,
                          double latitude, double longitude, String date) {
        this.imageView = imageView;
        this.astronomyController = astronomyController;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.placeholderImage = new Image(getClass().getResourceAsStream("/images/loading.png"));
        this.failedImage = new Image(getClass().getResourceAsStream("/images/loading_failed.png"));

        // Set the placeholder image initially
        this.imageView.setImage(this.placeholderImage);

        // Start loading the actual image in the background
        loadStarChartImageAsync();
    }

    private void loadStarChartImageAsync() {
        Timer timeoutTimer = new Timer();

        // create new thread to avoid blocking the GUI
        new Thread(() -> {
            String starChartUrl = astronomyController.getAreaStarChart(latitude, longitude, date, 14.83, -15.23, 9);
            System.out.println("starcharturl " + starChartUrl);

            if (starChartUrl == null) {
                imageView.setImage(failedImage);
                return;
            }

            Image actualImage = new Image(starChartUrl, true);
            actualImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                if (newProgress.doubleValue() >= 1.0) {
                    timeoutTimer.cancel();
                    Platform.runLater(() -> imageView.setImage(actualImage));
                }
            });
        }).start();

        // Schedule a timeout to set the failed image if loading takes too long
        timeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (imageView.getImage() == placeholderImage) {
                        imageView.setImage(failedImage);
                    }
                });
            }
        }, TIMEOUT_MS);
    }
}
