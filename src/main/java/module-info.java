module ryhma5 {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.google.gson;
    opens ryhma5.model to com.google.gson;
    requires batik.all;
    requires proj4j;
    requires javafx.swing;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.controller;
    exports ryhma5;
    opens ryhma5.controller to javafx.fxml;
}