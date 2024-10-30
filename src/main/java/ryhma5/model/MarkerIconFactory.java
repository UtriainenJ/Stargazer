package ryhma5.model;

import javafx.scene.image.Image;

import java.io.InputStream;

public class MarkerIconFactory {
    private static final String ICON_PATH = "/icons/mariostar_stillnoeyes.gif";
    private static final String SELECTED_ICON_PATH = "/icons/slowerstar.gif";
    private static Image iconImage = null;
    private static Image selectedIconImage = null;
    private static boolean iconLoadAttempted = false;

    static {
        tryLoadIcons();
    }

    private static void tryLoadIcons() {
        if (!iconLoadAttempted) {
            InputStream iconStream = MarkerIconFactory.class.getResourceAsStream(ICON_PATH);
            InputStream selectedIconStream = MarkerIconFactory.class.getResourceAsStream(SELECTED_ICON_PATH);

            if (iconStream != null) {
                iconImage = new Image(iconStream);
            }
            if (selectedIconStream != null) {
                selectedIconImage = new Image(selectedIconStream);
            }

            if (iconImage == null || selectedIconImage == null) {
                System.err.println("Failed to load one or more marker icons");
            }

            iconLoadAttempted = true;
        }
    }

    public static Image getIconImage() {
        return iconImage;
    }

    public static Image getSelectedIconImage() {
        return selectedIconImage;
    }
}