module ryhma5 {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires batik.all;
    requires javafx.swing;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.controller;
    exports ryhma5;
    opens ryhma5.controller to javafx.fxml;
    opens ryhma5.model.map to com.google.gson;
    exports ryhma5.model.api.whereTheISSAtAPI;
    opens ryhma5.model.api.whereTheISSAtAPI to com.google.gson;
    exports ryhma5.model.api.astronomyAPI;
    opens ryhma5.model.api.astronomyAPI to com.google.gson;
    exports ryhma5.model.api;
    opens ryhma5.model.api to com.google.gson;
    exports ryhma5.model.dateTimeUtils;
    opens ryhma5.model.dateTimeUtils to com.google.gson;
    exports ryhma5.model.map;
    exports ryhma5.model.json;
    opens ryhma5.model.json to com.google.gson;
}