package ryhma5.model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import ryhma5.model.json.DataManager;

public class SVGMap implements IMap {

    SVGLoader svgLoader;
    private final IProjector projector;
    private final List<Marker> markers = new ArrayList<>();
    private Marker selectedMarker = null;

    private final double markerRelativeSize = 0.03;

    protected SVGMap(String svgFilePath, IProjector projection) {
        this.svgLoader = new SVGLoader(svgFilePath);
        this.projector = projection;
    }


    // Load the map image and bind its size to the Pane
    @Override
    public ImageView loadMap(Pane mapPane) {
        ImageView mapImageView = (ImageView) svgLoader.loadSVG();
        if (mapImageView != null) {
            mapImageView.fitWidthProperty().bind(mapPane.widthProperty());
            mapImageView.fitHeightProperty().bind(mapPane.heightProperty());
            mapImageView.setPreserveRatio(true);  // Maintain the aspect ratio
        }
        return mapImageView;
    }

    @Override
    public void addMarkerByCoordinates(double latitude, double longitude, ImageView mapImageView, Pane mapPane, TextField searchField) {

        // check if there exists a marker at the same location, if so select that instead. accurate to some decimals to account for error in conversion
        for (Marker marker : markers) {
            double[] latLong = new double[] {marker.getLat(), marker.getLong()};
            if (Math.abs(latLong[0] - latitude) < 3.0 && Math.abs(latLong[1] - longitude) < 3.0) { // check if the markers would be too close
                System.out.println("Marker already exists at this location");
                selectMarker(marker, mapPane, false);
                return;
            }
        }

        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        double[] xy = projector.latLongToXY(latitude, longitude, imageWidth, imageHeight);
        addMarker(xy[0], xy[1], mapImageView, mapPane, searchField);
    }

    @Override
    public void addMarker(double x, double y, ImageView mapImageView, Pane mapPane, TextField searchField) {
        Platform.runLater(() -> {


            double imageWidth = mapImageView.getBoundsInParent().getWidth();
            double imageHeight = mapImageView.getBoundsInParent().getHeight();

            double markerRadius = markerRelativeSize * Math.min(imageWidth, imageHeight);
            if (imageWidth > 0 && imageHeight > 0) {

                // check if there exists a marker at the same location, if so select that instead.
                // practically the same check as addmarkerbycoordinates.
                for (Marker marker : markers) {
                    double markerX = marker.getRelativeX() * imageWidth;
                    double markerY = marker.getRelativeY() * imageHeight;

                    if (Math.abs(markerX - x) < markerRadius && Math.abs(markerY - y) < markerRadius) {
                        System.out.println("Marker already exists at this location");
                        selectMarker(marker, mapPane, false);
                        return;
                    }
                }


                // delete oldest marker if more than 3 exist (number subject to change)
                if (markers.size() >= 3) {
                    Marker oldestMarker = markers.remove(0);
                    destroyMarker(oldestMarker, mapPane);
                }
                // Calculate the latitude and longitude from x and y and use it in new Marker
                double[] latLong = getLatLongFromXY(x, y, imageWidth, imageHeight);

                // Calculate the relative position of the marker and set size immediately
                Marker marker = new Marker(x / imageWidth, y / imageHeight, markerRadius, latLong[0], latLong[1]);
                markers.add(marker);

                // Set the marker's initial position
                marker.getCircle().setCenterX(x);
                marker.getCircle().setCenterY(y);

                // Create a tooltip for the marker
                String latitude = String.format(Locale.US, "%.2f", latLong[0]);
                String longitude = String.format(Locale.US, "%.2f", latLong[1]);

                String tooltipText = "Latitude: " + latitude + "\nLongitude: " + longitude;
                Tooltip markerTooltip = new Tooltip(tooltipText);
                markerTooltip.setShowDuration(Duration.INDEFINITE);
                Tooltip.install(marker.getCircle(), markerTooltip);

                // Add the marker to the mapPane
                mapPane.getChildren().add(marker.getCircle());

                selectMarker(marker, mapPane, true);
                marker.getCircle().setOnMouseClicked(event -> {
                    selectMarker(marker, mapPane, true);
                    if (markers.contains(marker)){
                        searchField.setText(latitude + ", " + longitude);
                    } 
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
     *
     * @param marker  The marker to select
     * @param mapPane The Pane containing the map
     */
    @Override
    public void selectMarker(Marker marker, Pane mapPane, boolean deleteIfReselected) {
        if (selectedMarker != null) {
            selectedMarker.deSelectMarker();
            if (selectedMarker == marker && deleteIfReselected == true) { // If re-selecting the same marker, destroy it
                destroyMarker(marker, mapPane);
                selectedMarker = null;
                return;
            }
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
    @Override
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
    @Override
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

    /**
     * save all map markers coordinates as json, by making active marker last in the ArrayList
     */
    @Override
    public void saveMarkersAsJson(){
        ArrayList<double[]> markersLatLong = new ArrayList<>();
        for (Marker marker : markers){
            if(!marker.isMarkerSelected()) {
                markersLatLong.add(0, new double[]{marker.getLat(), marker.getLong()});
            } else {
                markersLatLong.add(new double[]{marker.getLat(), marker.getLong()});
            }
        }
        DataManager.saveData(markersLatLong, "map_markers");
    }

    @Override
    public double[] getLatLongFromXY(double x, double y, double imageWidth, double imageHeight) {
        return projector.xyToLatLong(x, y, imageWidth, imageHeight);
    }

    /**
     * methodused for unit testing
     * @return
     */
    @Override
    public List<Marker> getMarkers() {
        return markers;
    }
}
