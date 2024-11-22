package ryhma5.model.map;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EquirectangularProjectorTest {
    private static IProjector projector;

    @BeforeAll
    static void setUp() {
        projector = new EquirectangularProjector();
    }

    @Test
    void latLongToXY() {
        // test center
        double[] xy = projector.latLongToXY(0, 0, 1000, 1000);
        assertArrayEquals(new double[]{500, 500}, xy, "Center lat/long (0, 0) should map to (500, 500)");

        // top left
        xy = projector.latLongToXY(90, 180, 1000, 1000);
        assertArrayEquals(new double[]{1000, 0}, xy, "Top left lat/long (90, 180) should map to (1000, 0)");

        // top right
        xy = projector.latLongToXY(90, -180, 1000, 1000);
        assertArrayEquals(new double[]{0, 0}, xy, "Top right lat/long (90, -180) should map to (0, 0)");

        // bottom left
        xy = projector.latLongToXY(-90, 180, 1000, 1000);
        assertArrayEquals(new double[]{1000, 1000}, xy, "Bottom left lat/long (-90, 180) should map to (1000, 1000)");

        // bottom right
        xy = projector.latLongToXY(-90, -180, 1000, 1000);
        assertArrayEquals(new double[]{0, 1000}, xy, "Bottom right lat/long (-90, -180) should map to (0, 1000)");
    }

    @Test
    void latLongToXYNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            projector.latLongToXY(91, 0, 1000, 1000);
        }, "Latitude 91 is out of bounds and should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            projector.latLongToXY(-91, 0, 1000, 1000);
        }, "Latitude -91 is out of bounds and should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            projector.latLongToXY(0, 181, 1000, 1000);
        }, "Longitude 181 is out of bounds and should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            projector.latLongToXY(0, -181, 1000, 1000);
        }, "Longitude -181 is out of bounds and should throw IllegalArgumentException");
    }

    @Test
    void xyToLatLong() {
        // center
        double[] latLong = projector.xyToLatLong(500, 500, 1000, 1000);
        assertArrayEquals(new double[]{0, 0}, latLong, "Center coordinates (500, 500) should map to lat/long (0, 0)");

        // top left
        double[] xy = projector.xyToLatLong(1000, 0, 1000, 1000);
        assertArrayEquals(new double[]{90, 180}, xy, "Top left coordinates (1000, 0) should map to lat/long (90, 180)");

        // top right
        xy = projector.xyToLatLong(0, 0, 1000, 1000);
        assertArrayEquals(new double[]{90, -180}, xy, "Top right coordinates (0, 0) should map to lat/long (90, -180)");

        // bottom left
        xy = projector.xyToLatLong(1000, 1000, 1000, 1000);
        assertArrayEquals(new double[]{-90, 180}, xy, "Bottom left coordinates (1000, 1000) should map to lat/long (-90, 180)");

        // bottom right
        xy = projector.xyToLatLong(0, 1000, 1000, 1000);
        assertArrayEquals(new double[]{-90, -180}, xy, "Bottom right coordinates (0, 1000) should map to lat/long (-90, -180)");
    }

    @Test
    void xyToLatLongNegative() {
        // x out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(-1, 0, 1000, 1000);
        }, "Relative X coordinate -1 is out of bounds and should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(1001, 0, 1000, 1000);
        }, "Relative X coordinate 2 is out of bounds and should throw IllegalArgumentException");

        // y out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(0, -1, 1000, 1000);
        }, "Relative Y coordinate -1 is out of bounds and should throw IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(0, 1001, 1000, 1000);
        }, "Relative Y coordinate 2 is out of bounds and should throw IllegalArgumentException");
    }

    @Test
    void relativeXYToLatLong() {
        // center
        double[] latLong = projector.relativeXYToLatLong(0.5, 0.5);
        assertArrayEquals(new double[]{0, 0}, latLong);

        // top left
        double[] xy = projector.relativeXYToLatLong(1, 0);
        assertArrayEquals(new double[]{90, 180}, xy);

        // top right
        xy = projector.relativeXYToLatLong(0, 0);
        assertArrayEquals(new double[]{90, -180}, xy);

        // bottom left
        xy = projector.relativeXYToLatLong(1, 1);
        assertArrayEquals(new double[]{-90, 180}, xy);

        // bottom right
        xy = projector.relativeXYToLatLong(0, 1);
        assertArrayEquals(new double[]{-90, -180}, xy);
    }

    @Test
    void relativeXYToLatLongNegative() {
        // x out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.relativeXYToLatLong(-1, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.relativeXYToLatLong(2, 0);
        });

        // y out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.relativeXYToLatLong(0, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.relativeXYToLatLong(0, 2);
        });
    }
}