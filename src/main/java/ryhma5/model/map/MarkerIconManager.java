package ryhma5.model.map;

import javafx.scene.image.Image;

import java.io.InputStream;

public class MarkerIconManager {
    // in the future, these paths could be loaded from a configuration file
    private static final String ICON_PATH = "/icons/mariostar_stillnoeyes.gif";
    private static final String SELECTED_ICON_PATH = "/icons/slowerstar.gif";
    private static Image iconImage = null;
    private static Image selectedIconImage = null;
    private static boolean iconLoadAttempted = false;

    private static volatile MarkerIconManager instance = null;

    private MarkerIconManager() {
        loadIcons();
    }

    public static MarkerIconManager getInstance() {
        if (instance == null) {
            synchronized (MarkerIconManager.class) {
                if (instance == null) {
                    instance = new MarkerIconManager();
                }
            }
        }
        return instance;
    }

    private static void loadIcons() {
        if (!iconLoadAttempted) {
            InputStream iconStream = MarkerIconManager.class.getResourceAsStream(ICON_PATH);
            InputStream selectedIconStream = MarkerIconManager.class.getResourceAsStream(SELECTED_ICON_PATH);

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