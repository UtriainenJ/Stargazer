package ryhma5.model;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Uses Apache Batik to load and transcode an SVG file to a JavaFX Node (ImageView)
 */
public class SVGLoader {

    private final String svgFilePath;

    public SVGLoader(String svgFilePath) {
        this.svgFilePath = svgFilePath;
    }

    // Load the SVG and return it as a JavaFX Node (ImageView)
    public Node loadSVG() {
        InputStream svgFile = getClass().getResourceAsStream(svgFilePath);
        if (svgFile == null) {
            System.out.println("SVG file not found!");
            return null;
        }

        // Transcode the SVG to a BufferedImage
        BufferedImage bufferedImage = transcodeToImage(svgFile);

        if (bufferedImage != null) {
            // Convert the BufferedImage to a JavaFX Image and return it in an ImageView
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            ImageView imageView = new ImageView(fxImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(1000);  // Set initial width or bind later
            return imageView;
        }

        return null;
    }

    // Transcode the SVG to BufferedImage using Apache Batik
    private BufferedImage transcodeToImage(InputStream svgFile) {
        final BufferedImage[] imagePointer = new BufferedImage[1];

        ImageTranscoder transcoder = new ImageTranscoder() {
            @Override
            public BufferedImage createImage(int width, int height) {
                return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }

            @Override
            public void writeImage(BufferedImage img, TranscoderOutput output) {
                imagePointer[0] = img;
            }
        };

        TranscoderInput input = new TranscoderInput(svgFile);
        try {
            transcoder.transcode(input, null);
        } catch (TranscoderException e) {
            e.printStackTrace();
        }

        return imagePointer[0];
    }
}
