module ryhma5 {

    requires org.controlsfx.controls;
    requires com.google.gson;
    opens ryhma5.model to com.google.gson;
    requires javafx.fxml;
    requires batik.all;
    requires proj4j;
    requires javafx.swing;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.controller;
    exports ryhma5;
    opens ryhma5.controller to javafx.fxml;
}