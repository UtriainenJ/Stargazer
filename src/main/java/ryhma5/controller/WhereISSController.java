package ryhma5.controller;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ryhma5.model.map.EquirectangularProjector;
import ryhma5.model.api.whereTheISSAtAPI.ISSResponse;
import ryhma5.model.api.whereTheISSAtAPI.WhereTheISSHandler;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A controller class for the ISS
 */
public class WhereISSController {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ImageView issImageView;
    private ImageView mapImageView;

    private double iconScale = 0.15;
    private ISSResponse currentISS;
    private Tooltip tooltip;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        // Create and configure the ISS icon ImageView directly
        Image issIconImage = WhereTheISSHandler.getISSIcon();
        issImageView = new ImageView(issIconImage);
        issImageView.setScaleX(-1); // Flip the image horizontally

        issImageView.setFitWidth (iconScale * 400); // 400 = default height of the map image?
        issImageView.setFitHeight(iconScale * 400);

        tooltip = new Tooltip();
        Tooltip.install(issImageView, tooltip);
        tooltip.setShowDuration(Duration.INDEFINITE);
    }

    /**
     * Sets the map image view.
     * @param mapImageView The map image view to set.
     */
    protected void setMapImageView(ImageView mapImageView) {
        this.mapImageView = mapImageView;
    }

    /**
     * Updates the position of the ISS on the map.
     */
    public void updateISSPosition() {
        Platform.runLater(() -> {
            currentISS = getISS("kilometers");

            double imageWidth = mapImageView.getBoundsInParent().getWidth();
            double imageHeight = mapImageView.getBoundsInParent().getHeight();

            EquirectangularProjector projector = new EquirectangularProjector();
            double[] xy = projector.latLongToXY(currentISS.getLatitude(), currentISS.getLongitude(), imageWidth, imageHeight);

            issImageView.setLayoutX(xy[0] - issImageView.getFitWidth() / 2);
            issImageView.setLayoutY(xy[1] - issImageView.getFitHeight() / 2);

            updateTooltip();
        });
    }

    private void updateTooltip() {
        Platform.runLater(() -> {
            String latitude = String.format("%.2f", currentISS.getLatitude());
            String longitude = String.format("%.2f", currentISS.getLongitude());
            String altitude = String.format("%.2f", currentISS.getAltitude());
            String velocity = String.format("%.2f", currentISS.getVelocity());

            String newText = ("ISS live location: \n" +
                    "Latitude: " + latitude + "\n" +
                    "Longitude: " + longitude + "\n" +
                    "Altitude: " + altitude + " km\n" +
                    "Velocity: " + velocity + " km/h");

            tooltip.setText(newText);
        });
    }


    /**
     * Makes sure the ISS icon stays on the right position on the map even when resizing window.
     */
    public void adjustToWindowSize (){

        Platform.runLater(() -> {
            double windowWidth = mapImageView.getBoundsInParent().getWidth();
            double windowHeight = mapImageView.getBoundsInParent().getHeight();

            EquirectangularProjector projector = new EquirectangularProjector();
            double[] xy = projector.latLongToXY(currentISS.getLatitude(), currentISS.getLongitude(), windowWidth, windowHeight);

            issImageView.setLayoutX(xy[0] - issImageView.getFitWidth() / 2);
            issImageView.setLayoutY(xy[1] - issImageView.getFitHeight() / 2);

            double newScale = iconScale * Math.min(windowWidth, windowHeight);
            issImageView.setFitWidth(newScale);
            issImageView.setFitHeight(newScale);
        });
    }

    /**
     * Starts the periodic update of the ISS position.
     */
    public void startPeriodicISSUpdate() {
        // Schedule the task to run  periodically
        scheduler.scheduleAtFixedRate(this::updateISSPosition, 0, 30, TimeUnit.SECONDS);
    }

    /**
     * Stops the periodic update of the ISS position.
     */
    public void stopPeriodicISSUpdate() {
        scheduler.shutdown();
    }

    /**
     * Gets the ISS icon ImageView.
     * @return The ISS icon ImageView.
     */
    public ImageView getISSImageView() {
        return issImageView;
    }

    /**
     * Gets the ISS position at the specified timestamp.
     * @return The current ISS position.
     */
    public ISSResponse getISS(String units, Long timestamp) {
        try {
            return WhereTheISSHandler.fetchISS(units, timestamp);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the current ISS position.
     * @return The current ISS position.
     */
    public ISSResponse getISS(String units) {
        try {
            return WhereTheISSHandler.fetchISS(units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null;
        }
    }


    /**
     * Gets the ISS positions at the specified timestamps.
     * @return The ISS positions.
     */
    public List<ISSResponse> getISSPositions(List<Long> timestamps, String units) {
        try {
            return WhereTheISSHandler.fetchISSPositions(timestamps, units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS positions: " + e.getMessage());
            return null;
        }
    }
}