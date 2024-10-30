package ryhma5.model;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SVGMap {

    private final SVGLoader svgLoader;
    private IProjector projector;
    private final List<Marker> markers = new ArrayList<>();
    private Marker selectedMarker = null;

    private double markerRelativeSize = 0.03;

    protected SVGMap(String svgFilePath, IProjector projection) {
        this.svgLoader = new SVGLoader(svgFilePath);
        this.projector = projection;
    }


    // Load the map image and bind its size to the Pane
    public ImageView loadMap(Pane mapPane) {
        ImageView mapImageView = (ImageView) svgLoader.loadSVG();
        if (mapImageView != null) {
            mapImageView.fitWidthProperty().bind(mapPane.widthProperty());
            mapImageView.fitHeightProperty().bind(mapPane.heightProperty());
            mapImageView.setPreserveRatio(true);  // Maintain the aspect ratio
        }
        return mapImageView;
    }


    public void addMarker(double x, double y, ImageView mapImageView, Pane mapPane) {
        Platform.runLater(() -> {
            double imageWidth = mapImageView.getBoundsInParent().getWidth();
            double imageHeight = mapImageView.getBoundsInParent().getHeight();

            double markerRadius = markerRelativeSize * Math.min(imageWidth, imageHeight);
            if (imageWidth > 0 && imageHeight > 0) {
                // delete oldest marker if more than 3 exist (number subject to change)
                if (markers.size() >= 3) {
                    Marker oldestMarker = markers.remove(0);
                    destroyMarker(oldestMarker, mapPane);
                }

                // Calculate the relative position of the marker and set size immediately
                Marker marker = new Marker(x / imageWidth, y / imageHeight, markerRadius);
                markers.add(marker);

                // Set the marker's initial position
                marker.getCircle().setCenterX(x);
                marker.getCircle().setCenterY(y);

                // Add the marker to the mapPane
                mapPane.getChildren().add(marker.getCircle());

                selectMarker(marker, mapPane);
                marker.getCircle().setOnMouseClicked(event -> {
                    selectMarker(marker, mapPane);
                });

                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(marker.getCircle().radiusProperty(), markerRadius * 0.1)),
                        new KeyFrame(Duration.seconds(0.5), new KeyValue(marker.getCircle().radiusProperty(), markerRadius, Interpolator.LINEAR))
                );
                timeline.play();
            }
        });
    }


    /**
     * Selects a marker and deselects any other selected markers. if the marker is already selected, it will be destroyed
     * @param marker The marker to select
     * @param mapPane The Pane containing the map
     */
    public void selectMarker(Marker marker, Pane mapPane) {
        if (selectedMarker != null && selectedMarker != marker) {
            selectedMarker.deSelectMarker();
        }

        if (selectedMarker == marker) {
            destroyMarker(marker, mapPane);
            selectedMarker = null;
            return;
        }

        marker.selectMarker();
        selectedMarker = marker;
    }
    /**
     * Plays a short animation to shrink the marker and then remove it
     *
     * @param marker  The marker to remove
     * @param mapPane The Pane containing the map
     */
    public void destroyMarker(Marker marker, Pane mapPane) {
        double markerRadius = marker.getCircle().getRadius();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(marker.getCircle().radiusProperty(), markerRadius)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(marker.getCircle().radiusProperty(), 0, Interpolator.EASE_OUT))
        );

        timeline.setOnFinished(event -> {
            Platform.runLater(() -> {
                mapPane.getChildren().remove(marker.getCircle());
                markers.remove(marker);
            });
        });

        timeline.play();
    }


    // Update the marker positions when the window is resized
    public void updateMarkers(ImageView mapImageView) {
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        double newRadius = markerRelativeSize * Math.min(imageWidth, imageHeight);

        // Now update the position and radius of each marker
        for (Marker marker : markers) {
            double newX = marker.getRelativeX() * imageWidth;
            double newY = marker.getRelativeY() * imageHeight;

            // Update marker position
            marker.getCircle().setCenterX(newX);
            marker.getCircle().setCenterY(newY);

            // Update marker radius based on the new image size
            marker.getCircle().setRadius(newRadius);
        }
    }

    public double[] getLatLongFromXY(double x, double y, double imageWidth, double imageHeight) {
        // Delegate to the projector (use pixel coordinates and map dimensions to get lat/long)
        return projector.xyToLatLong(x, y, imageWidth, imageHeight);
    }


}
