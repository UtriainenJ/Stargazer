module ryhma5 {

    requires org.controlsfx.controls;
    requires com.gluonhq.maps;
    requires com.google.gson;
    opens ryhma5.model to com.google.gson;
    requires javafx.fxml;
    requires batik.all;
    requires proj4j;
    requires javafx.swing;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.view;
    exports ryhma5;
    opens ryhma5.view to javafx.fxml;
}