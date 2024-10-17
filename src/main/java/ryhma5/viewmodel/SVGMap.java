package ryhma5.viewmodel;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ryhma5.model.*;

import java.util.ArrayList;
import java.util.List;

public class SVGMap {

    private final SVGLoader svgLoader;
    private Projector projector;
    private final List<Marker> markers = new ArrayList<>();

    public SVGMap(String svgFilePath, Projections projection) {
        this.svgLoader = new SVGLoader(svgFilePath);
        setProjection(projection);  // Set the initial projection
    }

    // Set the projection type
    public void setProjection(Projections projection) {
        switch (projection) {
            case ROBINSON:
                projector = new RobinsonProjector();  // Use Robinson projection
                break;
            // Future case for Mercator (when it's implemented)
            case EQUIRECTANGULAR:
                projector = new EquirectangularProjector();
                break;
            default:
                throw new IllegalArgumentException("Unsupported projection type");
        }
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

            if (imageWidth > 0 && imageHeight > 0) {
                // Calculate the relative position of the marker and set size immediately
                Marker marker = new Marker(x / imageWidth, y / imageHeight, imageWidth, imageHeight);
                markers.add(marker);

                // Set the marker's initial position
                marker.getCircle().setCenterX(x);
                marker.getCircle().setCenterY(y);

                // Add the marker to the mapPane
                mapPane.getChildren().add(marker.getCircle());
            }
        });
    }



    // Update the marker positions when the window is resized
    public void updateMarkers(ImageView mapImageView) {
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        // Calculate the radius as 3% of the smaller dimension of the image
        double newRadius = Marker.calculateRadius(imageWidth, imageHeight);

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
