module org.tuni.stargazer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.tuni.stargazer to javafx.fxml;
    exports org.tuni.stargazer;
}