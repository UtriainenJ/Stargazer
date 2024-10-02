module ryhma5 {

    requires org.controlsfx.controls;
    requires com.gluonhq.maps;
    requires com.dlsc.gmapsfx;
    requires javafx.fxml;
    requires javafx.web;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.view;
    exports ryhma5;
    opens ryhma5.view to javafx.fxml;
}