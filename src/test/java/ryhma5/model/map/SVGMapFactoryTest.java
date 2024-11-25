package ryhma5.model.map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SVGMapFactoryTest {

    @Test
    public void testCreateEquirectangularMap() {
        SVGMap map = SVGMapFactory.createMap(Projections.EQUIRECTANGULAR);

        assertNotNull(map);
        assertTrue(map instanceof SVGMap);
    }

    @Test
    public void testCreateRobinsonMap() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SVGMapFactory.createMap(Projections.ROBINSON);
        });
        assertEquals("Unknown projection type: ROBINSON", exception.getMessage());
    }

    @Test
    public void testCreateMapWithUnknownProjection() {
        Projections unknownProjection = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SVGMapFactory.createMap(unknownProjection);
        });
        assertEquals("Unknown projection type: null", exception.getMessage());
    }
}
