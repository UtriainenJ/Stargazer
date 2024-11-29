package ryhma5.model.api.astronomyAPI;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryhma5.controller.AstronomyController;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class StarChartProxy {
    private final ImageView imageView;
    private final AstronomyController astronomyController;
    private final AstronomyResponse response;
    private final Image failedImage;

    public StarChartProxy(ImageView imageView, AstronomyController astronomyController, AstronomyResponse response) {
        this.imageView = imageView;
        this.astronomyController = astronomyController;
        this.response = response;
        Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/loading.png")));
        this.failedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/loading_failed.png")));

        // Set the placeholder image initially
        this.imageView.setImage(placeholderImage);

        // Start loading the actual image in the background
        loadStarChartImageAsync();
    }

    private void loadStarChartImageAsync() {
        double latitude = response.getObserverLatitude();
        double longitude = response.getObserverLongitude();
        String date = response.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String constellation = ConstellationConverter.getIdFromName(response.getConstellation());

        // create new thread to avoid blocking the GUI
        new Thread(() -> {
            System.out.println("sending out the starchart request");
            String starChartUrl = astronomyController.getConstellationStarChart(latitude, longitude, date, constellation);
            System.out.println("starcharturl " + starChartUrl);

            if (starChartUrl == null) {
                imageView.setImage(failedImage);
                return;
            }

            Image actualImage = new Image(starChartUrl, true);
            actualImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
                if (newProgress.doubleValue() >= 1.0) {
                    Platform.runLater(() -> imageView.setImage(actualImage));
                }
            });
        }).start();
    }
}
