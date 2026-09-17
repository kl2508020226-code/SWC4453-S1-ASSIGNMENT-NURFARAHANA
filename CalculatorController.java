package com.mycompany.vehicleservicecostcalculator;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class CalculatorController {

    @FXML private TextField txtCustomerName;
    @FXML private TextField txtRegNumber;
    @FXML private ComboBox<String> cbVehicleType;
    @FXML private ToggleGroup serviceToggleGroup;
    @FXML private RadioButton rbBasic, rbStandard, rbFull;
    @FXML private CheckBox chkEngineOil, chkBrakeInspection, chkACService, chkCarWash;
    @FXML private DatePicker dpServiceDate;
    @FXML private TextArea txtReceipt;

    private final double PRICE_BASIC = 80.00;
    private final double PRICE_STANDARD = 150.00;
    private final double PRICE_FULL = 250.00;

    private final double PRICE_ENGINE_OIL = 50.00;
    private final double PRICE_BRAKE_INSPECTION = 40.00;
    private final double PRICE_AC_SERVICE = 80.00;
    private final double PRICE_CAR_WASH = 20.00;

    private final double FIXED_SERVICE_CHARGE = 30.00;

    @FXML
    public void initialize() {
        if (cbVehicleType != null) {
            cbVehicleType.getItems().addAll("Motorcycle", "Car", "MPV", "SUV");
        }
        if (dpServiceDate != null) {
            dpServiceDate.setValue(LocalDate.now());
        }
    }

    @FXML
    private void handleCalculate() {
        try {
            validateInputs();

            String name = txtCustomerName.getText().trim();
            String regNo = txtRegNumber.getText().trim();
            String vehicleType = cbVehicleType.getValue();
            LocalDate serviceDate = dpServiceDate.getValue();

            double baseServiceCost = 0;
            String selectedService = "";
            if (rbBasic.isSelected()) {
                baseServiceCost = PRICE_BASIC;
                selectedService = "Basic Service";
            } else if (rbStandard.isSelected()) {
                baseServiceCost = PRICE_STANDARD;
                selectedService = "Standard Service";
            } else if (rbFull.isSelected()) {
                baseServiceCost = PRICE_FULL;
                selectedService = "Full Service";
            }

            double addonCost = 0;
            StringBuilder selectedAddons = new StringBuilder();

            if (chkEngineOil.isSelected()) {
                addonCost += PRICE_ENGINE_OIL;
                selectedAddons.append("  - Engine Oil Change (RM50)\n");
            }
            if (chkBrakeInspection.isSelected()) {
                addonCost += PRICE_BRAKE_INSPECTION;
                selectedAddons.append("  - Brake Inspection (RM40)\n");
            }
            if (chkACService.isSelected()) {
                addonCost += PRICE_AC_SERVICE;
                selectedAddons.append("  - Air-Conditioning Service (RM80)\n");
            }
            if (chkCarWash.isSelected()) {
                addonCost += PRICE_CAR_WASH;
                selectedAddons.append("  - Car Wash (RM20)\n");
            }

            if (selectedAddons.length() == 0) {
                selectedAddons.append("  - None\n");
            }

            double subtotal = baseServiceCost + addonCost;
            double discount = (subtotal > 300.00) ? (subtotal * 0.10) : 0.0;
            double finalPayable = subtotal + FIXED_SERVICE_CHARGE - discount;

            // Format teks resit mengikut sampel gambar
            StringBuilder invoice = new StringBuilder();
            invoice.append("=========================================\n");
            invoice.append("         SERVICE RECEIPT SUMMARY         \n");
            invoice.append("=========================================\n");
            invoice.append(String.format("Date: %s\n", serviceDate));
            invoice.append(String.format("Customer: %s\n", name));
            invoice.append(String.format("Reg No: %s\n", regNo));
            invoice.append(String.format("Vehicle: %s\n", vehicleType));
            invoice.append("-----------------------------------------\n");
            invoice.append(String.format("Main Service: %s (RM%.2f)\n", selectedService, baseServiceCost));
            invoice.append("Add-on Services:\n").append(selectedAddons.toString());
            invoice.append("-----------------------------------------\n");
            invoice.append(String.format("Subtotal:            RM %8.2f\n", subtotal));
            invoice.append(String.format("Service Fee:         RM %8.2f\n", FIXED_SERVICE_CHARGE));
            invoice.append(String.format("Discount (10%%):     -RM %8.2f\n", discount));
            invoice.append("-----------------------------------------\n");
            invoice.append(String.format("FINAL PAYABLE:       RM %8.2f\n", finalPayable));
            invoice.append("=========================================\n");

            txtReceipt.setText(invoice.toString());

        } catch (IllegalArgumentException ex) {
            showErrorAlert("Validation Error", ex.getMessage());
        } catch (Exception ex) {
            showErrorAlert("System Error", "Error: " + ex.getMessage());
        }
    }

    private void validateInputs() throws IllegalArgumentException {
        if (txtCustomerName.getText() == null || txtCustomerName.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Name cannot be empty.");
        }
        if (txtRegNumber.getText() == null || txtRegNumber.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle Registration Number cannot be empty.");
        }
        if (cbVehicleType.getValue() == null) {
            throw new IllegalArgumentException("Please select a Vehicle Type.");
        }
        if (serviceToggleGroup == null || serviceToggleGroup.getSelectedToggle() == null) {
            throw new IllegalArgumentException("Please select a Main Service Type.");
        }
        if (dpServiceDate.getValue() == null) {
            throw new IllegalArgumentException("Please select a valid Service Date.");
        }
    }

    @FXML
    private void handleReset() {
        txtCustomerName.clear();
        txtRegNumber.clear();
        cbVehicleType.setValue(null);
        if (serviceToggleGroup != null) {
            serviceToggleGroup.selectToggle(null);
        }
        chkEngineOil.setSelected(false);
        chkBrakeInspection.setSelected(false);
        chkACService.setSelected(false);
        chkCarWash.setSelected(false);
        dpServiceDate.setValue(LocalDate.now());

        if (txtReceipt != null) {
            txtReceipt.clear();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}