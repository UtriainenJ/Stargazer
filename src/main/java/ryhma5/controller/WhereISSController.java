package ryhma5.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ryhma5.model.map.EquirectangularProjector;
import ryhma5.model.ISSResponse;
import ryhma5.model.WhereTheISSHandler;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WhereISSController {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ImageView issImageView;
    private ImageView mapImageView;

    private double iconScale = 0.15;
    private ISSResponse currentISS;
    public void initialize() {
        // Create and configure the ISS icon ImageView directly
        Image issIconImage = WhereTheISSHandler.getISSIcon();
        issImageView = new ImageView(issIconImage);
        issImageView.setScaleX(-1); // Flip the image horizontally

        issImageView.setFitWidth (iconScale * 400); // 400 = default height of the map image?
        issImageView.setFitHeight(iconScale * 400);
    }

    protected void setMapImageView(ImageView mapImageView) {
        this.mapImageView = mapImageView;
    }
    public void updateISSPosition() {
        currentISS = getISS("kilometers");

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        EquirectangularProjector projector = new EquirectangularProjector();
        double[] xy = projector.latLongToXY(currentISS.getLatitude(), currentISS.getLongitude(), imageWidth, imageHeight);

        issImageView.setLayoutX(xy[0] - issImageView.getFitWidth() / 2);
        issImageView.setLayoutY(xy[1] - issImageView.getFitHeight() / 2);
    }

    public void adjustToWindowSize (){
        double windowWidth = mapImageView.getBoundsInParent().getWidth();
        double windowHeight = mapImageView.getBoundsInParent().getHeight();

        EquirectangularProjector projector = new EquirectangularProjector();
        double[] xy = projector.latLongToXY(currentISS.getLatitude(), currentISS.getLongitude(), windowWidth, windowHeight);

        issImageView.setLayoutX(xy[0] - issImageView.getFitWidth() / 2);
        issImageView.setLayoutY(xy[1] - issImageView.getFitHeight() / 2);

        double newScale = iconScale * Math.min(windowWidth, windowHeight);
        issImageView.setFitWidth(newScale);
        issImageView.setFitHeight(newScale);
    }

    public void startPeriodicISSUpdate() {
        // Schedule the task to run  periodically
        scheduler.scheduleAtFixedRate(this::updateISSPosition, 0, 30, TimeUnit.SECONDS);
    }
    public void stopPeriodicISSUpdate() {
        scheduler.shutdown();
    }

    public ImageView getISSImageView() {
        return issImageView;
    }
    public ISSResponse getISS(String units, Long timestamp) {
        try {
            return WhereTheISSHandler.fetchISS(units, timestamp);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null;
        }
    }
    public ISSResponse getISS(String units) {
        try {
            return WhereTheISSHandler.fetchISS(units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null;
        }
    }


    public List<ISSResponse> getISSPositions(List<Long> timestamps, String units) {
        try {
            return WhereTheISSHandler.fetchISSPositions(timestamps, units);
        } catch (Exception e) {
            System.err.println("Error fetching ISS positions: " + e.getMessage());
            return null;
        }
    }
}