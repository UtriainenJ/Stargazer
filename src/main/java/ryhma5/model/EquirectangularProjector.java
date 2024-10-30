package ryhma5.model;


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
        double x = ((longitude + MAX_LONGITUDE) / (2 * MAX_LONGITUDE)) * imageWidth;
        double y = ((MAX_LATITUDE - latitude) / (2 * MAX_LATITUDE)) * imageHeight;
        return new double[]{x, y};
    }

    @Override
    public double[] xyToLatLong(double x, double y, double imageWidth, double imageHeight) {
        double longitude = (x / imageWidth) * (2 * MAX_LONGITUDE) - MAX_LONGITUDE;
        double latitude = MAX_LATITUDE - (y / imageHeight) * (2 * MAX_LATITUDE);
        return new double[]{latitude, longitude};
    }
}
