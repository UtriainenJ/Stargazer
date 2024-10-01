module ryhma5 {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.gluonhq.maps;

    opens ryhma5 to javafx.fxml;
    exports ryhma5.view;
    exports ryhma5;
    opens ryhma5.view to javafx.fxml;
}