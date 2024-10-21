package ryhma5.model;

import org.locationtech.proj4j.ProjCoordinate;
import org.locationtech.proj4j.proj.RobinsonProjection;
import ryhma5.model.Projector;

/**
 * Robinson projection is a map projection which distorts the edges.
 * TODO: fix or delete
 */
public class RobinsonProjector implements Projector {

    private final RobinsonProjection projection;

    public RobinsonProjector() {
        this.projection = new RobinsonProjection();
    }

    @Override
    public double[] latLongToXY(double latitude, double longitude, double imageWidth, double imageHeight) {
        // Convert latitude and longitude to radians
        double latRadians = Math.toRadians(latitude);
        double lonRadians = Math.toRadians(longitude);

        // Create a coordinate object for the projection
        ProjCoordinate projCoordinate = new ProjCoordinate();
        projection.project(lonRadians, latRadians, projCoordinate);

        // Map the projected coordinates to pixel coordinates
        double x = projCoordinate.x * (imageWidth / 2) + (imageWidth / 2); // Centering
        double y = -(projCoordinate.y * (imageHeight / 2)) + (imageHeight / 2); // Inverting Y-axis

        return new double[]{x, y};
    }

    @Override
    public double[] xyToLatLong(double x, double y, double imageWidth, double imageHeight) {
        // Normalize pixel coordinates to range -1 to 1
        double normalizedX = (x - (imageWidth / 2)) / (imageWidth / 2);
        double normalizedY = -((y - (imageHeight / 2)) / (imageHeight / 2));

        // Create a coordinate object for inverse projection
        ProjCoordinate latLong = new ProjCoordinate();
        projection.projectInverse(normalizedX, normalizedY, latLong);

        // Convert radians to degrees
        double latitude = Math.toDegrees(latLong.y);
        double longitude = Math.toDegrees(latLong.x);

        return new double[]{latitude, longitude};
    }
}
