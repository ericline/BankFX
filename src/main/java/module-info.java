module com.example.rubank {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.rubank to javafx.fxml;
    exports com.example.rubank;
}