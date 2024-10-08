package ryhma5.viewmodel;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
            case MERCATOR:
                // projectionHelper = new MercatorProjector();
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
        // Place the marker directly at the clicked pixel coordinates
        Circle markerCircle = new Circle(x, y, 5);  // Size 5 for the marker
        markerCircle.setFill(Color.RED);

        // Store marker with relative pixel positions
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();
        Marker marker = new Marker(markerCircle, x / imageWidth, y / imageHeight);

        markers.add(marker);
        mapPane.getChildren().add(markerCircle);
    }



    // Update the marker positions when the window is resized
    public void updateMarkers(ImageView mapImageView) {
        double imageWidth = mapImageView.getBoundsInParent().getWidth();
        double imageHeight = mapImageView.getBoundsInParent().getHeight();

        for (Marker marker : markers) {
            double newX = marker.getRelativeX() * imageWidth;
            double newY = marker.getRelativeY() * imageHeight;
            marker.getCircle().setCenterX(newX);
            marker.getCircle().setCenterY(newY);
        }
    }

    public double[] getLatLongFromXY(double x, double y, double imageWidth, double imageHeight) {
        // Delegate to the projector (use pixel coordinates and map dimensions to get lat/long)
        return projector.xyToLatLong(x, y, imageWidth, imageHeight);
    }



}
