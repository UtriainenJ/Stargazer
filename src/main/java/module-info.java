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
    exports ryhma5.model;
    opens ryhma5.controller to javafx.fxml;
    opens ryhma5.model.map to com.google.gson;
    exports ryhma5.model.api.whereTheISSAtAPI;
    opens ryhma5.model.api.whereTheISSAtAPI to com.google.gson;
}