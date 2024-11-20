package ryhma5.model.map;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EquirectangularProjectorTest {
    private static IProjector projector;

    @BeforeAll
    static void setUp() {
        projector = new EquirectangularProjector();
    }

    @AfterAll
    static void tearDown() {
        projector = null;
    }

    @Test
    void latLongToXY() {
        // test center
        double[] xy = projector.latLongToXY(0, 0, 1000, 1000);
        assertArrayEquals(new double[]{500, 500}, xy);

        // test corners
        // top left
        xy = projector.latLongToXY(90, 180, 1000, 1000);
        assertArrayEquals(new double[]{1000, 0}, xy);

        // top right
        xy = projector.latLongToXY(90, -180, 1000, 1000);
        assertArrayEquals(new double[]{0, 0}, xy);

        // bottom left
        xy = projector.latLongToXY(-90, 180, 1000, 1000);
        assertArrayEquals(new double[]{1000, 1000}, xy);

        // bottom right
        xy = projector.latLongToXY(-90, -180, 1000, 1000);
        assertArrayEquals(new double[]{0, 1000}, xy);
    }

    @Test
    void latLongToXYNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.latLongToXY(91, 0, 1000, 1000);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.latLongToXY(-91, 0, 1000, 1000);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.latLongToXY(0, 181, 1000, 1000);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.latLongToXY(0, -181, 1000, 1000);
        });
    }

    @Test
    void xyToLatLong() {
        // center
        double[] latLong = projector.xyToLatLong(500, 500, 1000, 1000);
        assertArrayEquals(new double[]{0, 0}, latLong);

        // top left
        double[] xy = projector.xyToLatLong(1000, 0, 1000, 1000);
        assertArrayEquals(new double[]{90, 180}, xy);

        // top right
        xy = projector.xyToLatLong(0, 0, 1000, 1000);
        assertArrayEquals(new double[]{90, -180}, xy);

        // bottom left
        xy = projector.xyToLatLong(1000, 1000, 1000, 1000);
        assertArrayEquals(new double[]{-90, 180}, xy);

        // bottom right
        xy = projector.xyToLatLong(0, 1000, 1000, 1000);
        assertArrayEquals(new double[]{-90, -180}, xy);

    }

    @Test
    void xyToLatLongNegative() {
        // x out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(-1, 0, 1000, 1000);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(1001, 0, 1000, 1000);
        });

        // y out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(0, -1, 1000, 1000);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] xy = projector.xyToLatLong(0, 1001, 1000, 1000);
        });

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