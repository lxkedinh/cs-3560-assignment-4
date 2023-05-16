package com.assignment4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import com.assignment4.controllers.CustomerController;

public class MainWindowController {
    @FXML
    private Button customerButton;
    @FXML
    private Button orderButton;

    public void onCustomerButtonClick(ActionEvent e) throws IOException {
        Stage stage = (Stage) customerButton.getScene().getWindow();
        FXMLLoader f = new FXMLLoader(MainWindowController.class.getResource("customer-view.fxml"));
        Scene newScene = new Scene(f.load());
        stage.setScene(newScene);
        stage.setTitle("Customer");
    }

    public void onOrderButtonClick(ActionEvent e) throws IOException {
        // don't go to order window if there are no customers in database
        if (CustomerController.fetchAllCustomers().isEmpty()) {
            showAlert(AlertType.INFORMATION,
                    "There are no customers in the database. Please add some before going to the order window.");
            return;
        }

        Stage stage = (Stage) orderButton.getScene().getWindow();
        FXMLLoader f = new FXMLLoader(MainWindowController.class.getResource("order-view.fxml"));
        Scene newScene = new Scene(f.load());
        stage.setScene(newScene);
        stage.setTitle("Order");
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
