package ryhma5.model.map;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SVGLoaderTest {

    @Test
    void testLoadValidSVG() {
        String validSvgPath = "/maps/Equirectangular_projection_world_map_without_borders.svg";
        SVGLoader loader = new SVGLoader(validSvgPath);

        Node result = loader.loadSVG();

        assertNotNull(result, "The loaded SVG should not be null");
        assertTrue(result instanceof ImageView, "The loaded SVG should be an ImageView");
        ImageView imageView = (ImageView) result;
        assertTrue(imageView.isPreserveRatio(), "The ImageView should preserve aspect ratio");
        assertEquals(1000, imageView.getFitWidth(), "The ImageView should have a width of 1000");
    }

    @Test
    void testLoadInvalidSVG() {
        String invalidSvgPath = "/test-resources/invalid-sample.svg";
        SVGLoader loader = new SVGLoader(invalidSvgPath);

        Node result = loader.loadSVG();

        assertNull(result, "The loader should return null for an invalid SVG");
    }
}
