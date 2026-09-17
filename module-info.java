module com.mycompany.vehicleservicecostcalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.vehicleservicecostcalculator to javafx.fxml;
    exports com.mycompany.vehicleservicecostcalculator;
}
