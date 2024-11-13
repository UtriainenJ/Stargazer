package ryhma5.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryhma5.model.EquirectangularProjector;
import ryhma5.model.ISSResponse;
import ryhma5.model.WhereISSAPI;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WhereISSController {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ImageView issImageView;
    private ImageView mapImageView;
    public void initialize() {
        // Create and configure the ISS icon ImageView directly
        Image issIconImage = WhereISSAPI.getISSIcon();
        issImageView = new ImageView(issIconImage);
        issImageView.setFitWidth(50);
        issImageView.setFitHeight(50);

        issImageView.setLayoutX(500);
        issImageView.setLayoutY(300);
    }

    protected void setMapImageView(ImageView mapImageView) {
        this.mapImageView = mapImageView;
    }
    public void updateISSPosition() {

        ISSResponse iss = getISS("kilometers");
        if (iss == null) {
            return;
        }

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        EquirectangularProjector projector = new EquirectangularProjector();
        double[] xy = projector.latLongToXY(iss.getLatitude(), iss.getLongitude(), imageWidth, imageHeight);

        issImageView.setLayoutX(xy[0]);
       issImageView.setLayoutY(xy[1]);
    }

    public void startPeriodicISSUpdate() {
        // Schedule the task to run every 60 seconds (1 minute)
        scheduler.scheduleAtFixedRate(this::updateISSPosition, 10, 60, TimeUnit.SECONDS);
    }
    public void stopPeriodicISSUpdate() {
        scheduler.shutdown();
    }

    public ImageView getISSImageView() {
        return issImageView;
    }
    public ISSResponse getISS(String units, Long timestamp) {
        try {
            return WhereISSAPI.fetchISS(units, timestamp);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null;
        }
    }
    public ISSResponse getISS(String units) {
        try {
            return WhereISSAPI.fetchISS(units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null;
        }
    }


    public List<ISSResponse> getISSPositions(List<Long> timestamps, String units) {
        try {
            return WhereISSAPI.fetchISSPositions(timestamps, units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS positions: " + e.getMessage());
            return null; // or throw a custom exception if needed
        }
    }
}