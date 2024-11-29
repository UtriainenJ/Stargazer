package ryhma5.model.map;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * IMap interface, currently implemented only by SVGMap, but in the future we could potentially replace it with an external library that implements this interface.
 */
public interface IMap {
    // Load the map image and bind its size to the Pane
    ImageView loadMap(Pane mapPane);

    void addMarkerByCoordinates(double latitude, double longitude, ImageView mapImageView, Pane mapPane, TextField searchField);

    void addMarker(double x, double y, ImageView mapImageView, Pane mapPane, TextField searchField);

    void selectMarker(Marker marker, Pane mapPane, boolean deleteIfReselected);

    void destroyMarker(Marker marker, Pane mapPane);

    // Update the marker positions when the window is resized
    void updateMarkers(ImageView mapImageView);

    void saveMarkersAsJson();

    double[] getLatLongFromXY(double x, double y, double imageWidth, double imageHeight);

    List<Marker> getMarkers();
}
