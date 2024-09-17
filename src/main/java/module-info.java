module org.tuni.stargazer {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.gluonhq.maps;

    opens org.tuni.stargazer to javafx.fxml;
    exports org.tuni.stargazer;
}