package ryhma5.model.api.astronomyAPI;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryhma5.controller.AstronomyController;

import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class StarChartProxy {
    private final ImageView imageView;
    private final AstronomyController astronomyController;
    private final AstronomyResponse response;
    private final Image placeholderImage;
    private final Image failedImage;
    private static final int TIMEOUT_MS = 30000;

    public StarChartProxy(ImageView imageView, AstronomyController astronomyController, AstronomyResponse response) {
        this.imageView = imageView;
        this.astronomyController = astronomyController;
        this.response = response;
        this.placeholderImage = new Image(getClass().getResourceAsStream("/images/loading.png"));
        this.failedImage = new Image(getClass().getResourceAsStream("/images/loading_failed.png"));

        // Set the placeholder image initially
        this.imageView.setImage(this.placeholderImage);

        // Start loading the actual image in the background
        loadStarChartImageAsync();
    }

    private void loadStarChartImageAsync() {
        Timer timeoutTimer = new Timer();
        double latitude = response.getObserverLatitude();
        double longitude = response.getObserverLongitude();
        String date = response.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String constellation = ConstellationConverter.getIdFromName(response.getConstellation());

        // create new thread to avoid blocking the GUI
        new Thread(() -> {
            String starChartUrl = astronomyController.getConstellationStarChart(latitude, longitude, date, constellation);
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
