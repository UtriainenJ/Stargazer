package ryhma5.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ryhma5.model.ISSResponse;
import ryhma5.model.WhereISSAPI;

import java.util.List;
import java.util.Objects;

public class WhereISSController {

    private ImageView issImageView;
    public void initialize() {
        // Create and configure the ISS icon ImageView directly
        Image issIconImage = WhereISSAPI.getISSIcon();
        issImageView = new ImageView(issIconImage);
        issImageView.setFitWidth(50);
        issImageView.setFitHeight(50);

        issImageView.setLayoutX(500);
        issImageView.setLayoutY(300);
    }

    public void updateISSPosition(double x, double y) {
        issImageView.setLayoutX(x);
        issImageView.setLayoutY(y);
    }

    public ImageView getISSImageView() {
        return issImageView;
    }
    public ISSResponse getISS(String units, Long timestamp) {
        try {
            return WhereISSAPI.fetchISS(units, timestamp);
        } catch (Exception e) {
            System.err.println("Error fetching ISS information: " + e.getMessage());
            return null; // or throw a custom exception if needed
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