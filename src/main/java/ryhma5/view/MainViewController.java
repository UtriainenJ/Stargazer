package ryhma5.view;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainViewController {

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox sidebar;

    @FXML
    private HBox sidebarContainer;

    private boolean isSidebarVisible = false;

    public void initialize() {
        // Sidebar is moved out of the way at start
        if (sidebar != null) {
            sidebarContainer.setTranslateX(-sidebar.getPrefWidth());
        } else {
            System.out.println("Sidebar is not loaded.");
        }
        
        // These could be modified to make sure the sidebar containers don't block the map from the cursor. Couldn't get it to work simply yet

        //sidebarContainer.setMouseTransparent(true);
        //    for (Node child : sidebarContainer.getChildrenUnmodifiable()){
        //       child.setMouseTransparent(false);
        //}
    }

    @FXML
    public void toggleSidebar() {
        TranslateTransition slideTransition = new TranslateTransition();
        slideTransition.setNode(sidebarContainer);
        slideTransition.setDuration(Duration.millis(300));

        if (isSidebarVisible) {
            // Hide the sidebar
            slideTransition.setToX(-sidebar.getPrefWidth());
        } else {
            // Show the sidebar
            slideTransition.setToX(0);
        }

        // Play the animation
        slideTransition.play();

        // Toggle the state
        isSidebarVisible = !isSidebarVisible;
    }
}