package ryhma5.model;

public interface Projector {

    // Convert latitude and longitude to projected X, Y coordinates (for placing markers)
    double[] latLongToXY(double latitude, double longitude, double imageWidth, double imageHeight);

    // Convert pixel X, Y coordinates back to latitude and longitude (for printing real coordinates)
    double[] xyToLatLong(double x, double y, double imageWidth, double imageHeight);
}
