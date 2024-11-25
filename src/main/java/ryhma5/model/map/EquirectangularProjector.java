package ryhma5.model.map;


/**
 * EquirectangularProjector is a simple implementation of the Projector interface that uses an equirectangular
 * projection to convert between geographic coordinates and pixel coordinates.
 * Equirectunglar projection: simple 2:1 map with no stretching or distortion.
 */
public class EquirectangularProjector implements IProjector {

    private static final double MAX_LONGITUDE = 180.0;
    private static final double MAX_LATITUDE = 90.0;

    @Override
    public double[] latLongToXY(double latitude, double longitude, double imageWidth, double imageHeight) {
        if (latitude > MAX_LATITUDE || latitude < -MAX_LATITUDE || longitude > MAX_LONGITUDE || longitude < -MAX_LONGITUDE) {
            throw new IllegalArgumentException("Latitude or longitude out of bounds");
        }

        double x = ((longitude + MAX_LONGITUDE) / (2 * MAX_LONGITUDE)) * imageWidth;
        double y = ((MAX_LATITUDE - latitude) / (2 * MAX_LATITUDE)) * imageHeight;
        return new double[]{x, y};
    }

    @Override
    public double[] xyToLatLong(double x, double y, double maxX, double maxY) {
        if (x < 0 || x > maxX || y < 0 || y > maxY) {
            throw new IllegalArgumentException("X or Y out of bounds");
        }

        double relativeX = x / maxX;
        double relativeY = y / maxY;

        double longitude = relativeX * (2 * MAX_LONGITUDE) - MAX_LONGITUDE;
        double latitude = MAX_LATITUDE - relativeY * (2 * MAX_LATITUDE);
        return new double[]{latitude, longitude};
    }

    @Override
    public double[] relativeXYToLatLong(double relativeX, double relativeY) {
        if (relativeX < 0 || relativeX > 1 || relativeY < 0 || relativeY > 1) {
            throw new IllegalArgumentException("Relative X or Y out of bounds");
        }

        double longitude = relativeX * (2 * MAX_LONGITUDE) - MAX_LONGITUDE;
        double latitude = MAX_LATITUDE - relativeY * (2 * MAX_LATITUDE);
        return new double[]{latitude, longitude};
    }
}
