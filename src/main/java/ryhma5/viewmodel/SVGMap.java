package ryhma5.viewmodel;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ryhma5.model.SVGLoader;

public class SVGMap {

    private final SVGLoader svgLoader;

    public SVGMap(String svgFilePath) {
        this.svgLoader = new SVGLoader(svgFilePath);
    }

    // Load the map image and bind its size to the Pane
    public ImageView loadMap(Pane mapPane) {
        ImageView mapImageView = (ImageView) svgLoader.loadSVG();
        if (mapImageView != null) {
            // Bind the image's width and height to the Pane's width and height
            mapImageView.fitWidthProperty().bind(mapPane.widthProperty());
            mapImageView.fitHeightProperty().bind(mapPane.heightProperty());
            mapImageView.setPreserveRatio(true); // Maintain the aspect ratio
        }
        return mapImageView;
    }

    // Convert latitude and longitude to X and Y coordinates on the map
    public double[] latLongToXY(double latitude, double longitude, ImageView mapImageView) {
        double displayedWidth = mapImageView.getBoundsInParent().getWidth();
        double displayedHeight = mapImageView.getBoundsInParent().getHeight();

        double x = (longitude + 180) * (displayedWidth / 360);  // Adjust for displayed width
        double y = (90 - latitude) * (displayedHeight / 180);   // Adjust for displayed height
        return new double[]{x, y};
    }

    // Convert X and Y coordinates on the image back to latitude and longitude
    public double[] xyToLatLong(double x, double y, ImageView mapImageView) {
        double displayedWidth = mapImageView.getBoundsInParent().getWidth();
        double displayedHeight = mapImageView.getBoundsInParent().getHeight();

        // Calculate longitude and latitude
        double longitude = (x / displayedWidth) * 360 - 180;  // Reverse the X calculation
        double latitude = 90 - (y / displayedHeight) * 180;   // Reverse the Y calculation

        return new double[]{latitude, longitude};
    }

    // Add a marker at the specified latitude and longitude
    public Node addMarker(double latitude, double longitude, ImageView mapImageView) {
        double[] xy = latLongToXY(latitude, longitude, mapImageView);

        // Create a marker (circle) at the calculated X, Y coordinates
        Circle marker = new Circle(xy[0], xy[1], 5);
        marker.setFill(Color.RED);

        return marker;  // Return the marker to be added to the map
    }
}
