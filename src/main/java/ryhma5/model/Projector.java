package ryhma5.model;

/**
 * Projector calculates and converts geographic lat-long to XY-cordinates on the map and vice versa.
 * Different maps porjections calculate the conversion differently.
 */
public interface Projector {

    double[] latLongToXY(double latitude, double longitude, double imageWidth, double imageHeight);

    double[] xyToLatLong(double x, double y, double imageWidth, double imageHeight);
}
