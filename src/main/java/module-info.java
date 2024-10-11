module ryhma5 {

    requires org.controlsfx.controls;
    requires javafx.fxml;
    requires batik.all;
    requires proj4j;
    requires javafx.swing;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.view;
    exports ryhma5;
    opens ryhma5.view to javafx.fxml;
}