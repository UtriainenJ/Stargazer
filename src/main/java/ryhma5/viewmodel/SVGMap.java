package ryhma5.viewmodel;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ryhma5.model.SVGLoader;

public class SVGMap {

    private final SVGLoader svgLoader;

    // Robinson projection lookup table (normalized Y values)
    private final double[] robinsonLookupY = {
            0.0000, 0.0620, 0.1240, 0.1860, 0.2480, 0.3100, 0.3720, 0.4340,
            0.4958, 0.5571, 0.6176, 0.6769, 0.7346, 0.7903, 0.8435, 0.8936,
            0.9394, 0.9761, 1.0000
    };

    // Latitude values corresponding to the Robinson projection lookup Y values
    private final double[] latitudes = {
            90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0
    };

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


    // Interpolate latitude using the Robinson projection lookup table
    private double interpolateLatitudeToY(double normalizedLatitude) {
        for (int i = 1; i < robinsonLookupY.length; i++) {
            if (normalizedLatitude <= robinsonLookupY[i]) {
                double y1 = robinsonLookupY[i - 1];
                double y2 = robinsonLookupY[i];
                return y1 + (normalizedLatitude - y1) * (y2 - y1) / (y2 - y1);
            }
        }
        return 1.0;  // Default to equator if something goes wrong
    }

    // Convert latitude and longitude to X and Y coordinates on the map (Robinson projection)
    public double[] latLongToXY(double latitude, double longitude, ImageView mapImageView) {
        double displayedWidth = mapImageView.getBoundsInParent().getWidth();
        double displayedHeight = mapImageView.getBoundsInParent().getHeight();

        // Longitude is linear
        double x = (longitude + 180) * (displayedWidth / 360);  // Adjust for displayed width

        // Latitude requires the Robinson projection lookup
        double normalizedLatitude = Math.abs(latitude) / 90.0;  // Normalize latitude to [0, 1]
        double y = interpolateLatitudeToY(normalizedLatitude) * displayedHeight;  // Adjust for height

        if (latitude < 0) {
            y = displayedHeight - y;  // For Southern Hemisphere
        }

        return new double[]{x, y};
    }

    // Convert X and Y coordinates on the image back to latitude and longitude (Robinson projection)
    public double[] xyToLatLong(double x, double y, ImageView mapImageView) {
        double displayedWidth = mapImageView.getBoundsInParent().getWidth();
        double displayedHeight = mapImageView.getBoundsInParent().getHeight();

        // Calculate longitude (linear)
        double longitude = (x / displayedWidth) * 360 - 180;

        // Calculate latitude (reverse Robinson projection)
        double normalizedY = y / displayedHeight;
        double latitude = reverseRobinsonYToLatitude(normalizedY);

        return new double[]{latitude, longitude};
    }

    // Reverse Robinson projection from Y coordinate to latitude
    private double reverseRobinsonYToLatitude(double normalizedY) {
        for (int i = 1; i < robinsonLookupY.length; i++) {
            if (normalizedY <= robinsonLookupY[i]) {
                double y1 = robinsonLookupY[i - 1];
                double y2 = robinsonLookupY[i];
                double lat1 = latitudes[i - 1];
                double lat2 = latitudes[i];
                return lat1 + (normalizedY - y1) * (lat2 - lat1) / (y2 - y1);
            }
        }
        return 0;  // Default to equator if something goes wrong
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
