module org.tuni.stargazer {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.gluonhq.maps;

    opens project to javafx.fxml;
    exports project.view;
    exports project;
    opens project.view to javafx.fxml;
}