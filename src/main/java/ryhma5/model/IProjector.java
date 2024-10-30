package ryhma5.model;

/**
 * Projector calculates and converts geographic lat-long to XY-cordinates on the map and vice versa.
 * Different maps porjections calculate the conversion differently.
 */
public interface IProjector {

    /**
     * Converts lat-long coordinates to XY -pixel coordinates. X goes from 0 to imageWidth left to right and Y goes from 0 to imageHeight top to bottom.
     * @param latitude
     * @param longitude
     * @param imageWidth max x-coordinate of the image
     * @param imageHeight max y-coordinate of the image
     * @return
     */
    double[] latLongToXY(double latitude, double longitude, double imageWidth, double imageHeight);

    /**
     * Converts XY -pixel coordinates to lat-long coordinates.
     * @param x x-coordinate. 0 is the left edge of the image.
     * @param y y-coordinate. 0 is the top edge of the image.
     * @param imageWidth max x-coordinate of the image
     * @param imageHeight max y-coordinate of the image
     * @return
     */
    double[] xyToLatLong(double x, double y, double imageWidth, double imageHeight);
}
