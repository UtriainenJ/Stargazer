package ryhma5.model;


/**
 * EquirectangularProjector is a simple implementation of the Projector interface that uses an equirectangular
 * projection to convert between geographic coordinates and pixel coordinates.
 * Equirectunglar projection: simple 2:1 map with no stretching or distortion.
 */
public class EquirectangularProjector implements Projector {

    // Define constants for the projection (standard projection covers -180 to 180 longitude, -90 to 90 latitude)
    private static final double MAX_LONGITUDE = 180.0;
    private static final double MAX_LATITUDE = 90.0;

    @Override
    public double[] latLongToXY(double latitude, double longitude, double imageWidth, double imageHeight) {
        // Scale longitude directly to X coordinate
        double x = ((longitude + MAX_LONGITUDE) / (2 * MAX_LONGITUDE)) * imageWidth;

        // Scale latitude directly to Y coordinate (invert Y because image origin is top-left)
        double y = ((MAX_LATITUDE - latitude) / (2 * MAX_LATITUDE)) * imageHeight;

        return new double[]{x, y};
    }

    @Override
    public double[] xyToLatLong(double x, double y, double imageWidth, double imageHeight) {
        // Convert X back to longitude
        double longitude = (x / imageWidth) * (2 * MAX_LONGITUDE) - MAX_LONGITUDE;

        // Convert Y back to latitude (inverted because of the top-left origin)
        double latitude = MAX_LATITUDE - (y / imageHeight) * (2 * MAX_LATITUDE);

        return new double[]{latitude, longitude};
    }
}
