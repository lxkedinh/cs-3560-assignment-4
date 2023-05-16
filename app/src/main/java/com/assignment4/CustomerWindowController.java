package com.assignment4;

import java.io.IOException;
import com.assignment4.controllers.AddressController;
import com.assignment4.controllers.CustomerController;
import com.assignment4.dao.EntityNotFoundException;
import com.assignment4.model.Address;
import com.assignment4.model.Customer;
import jakarta.persistence.NonUniqueResultException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CustomerWindowController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField zipCodeField;
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    // keep track of currently fetched customer to handle updating
    private Customer currentFetchedCustomer;

    // Search Customer operation
    public void handleSearchClick() {
        if (nameField.getText().equals("")) {
            showAlert(AlertType.ERROR,
                    "The name field should be not empty when searching for a customer.");
            return;
        }

        try {
            System.out.println(nameField.getText());
            currentFetchedCustomer = CustomerController.searchByName(nameField.getText());
            nameField.setText(currentFetchedCustomer.getName());
            phoneField.setText(currentFetchedCustomer.getPhone());
            emailField.setText(currentFetchedCustomer.getEmail());
            streetField.setText(currentFetchedCustomer.getAddress().getStreet());
            cityField.setText(currentFetchedCustomer.getAddress().getCity());
            stateField.setText(currentFetchedCustomer.getAddress().getState());

            Integer fetchedZipCode = currentFetchedCustomer.getAddress().getZipCode();
            if (fetchedZipCode != null) {
                zipCodeField.setText(fetchedZipCode.toString());
            } else {
                zipCodeField.setText("");
            }
        } catch (EntityNotFoundException e) {
            showAlert(AlertType.INFORMATION, "No records found.");
        } catch (NonUniqueResultException e) {
            showAlert(AlertType.ERROR,
                    "Your search yieled more than one result. Please be more specific.");
        }
    }

    // Add Customer operation
    public void handleAddClick() {

        if (areAnyRequiredFieldsEmpty()) {
            showAlert(AlertType.ERROR,
                    "Please fill in all required fields marked with a red * when adding a customer.");
            return;
        }

        // handle inserting address first to get its generated primary key
        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        int zipCode;
        Address address;
        try {
            zipCode = Integer.parseInt(zipCodeField.getText());
            address = new Address(street, city, state, zipCode);
        } catch (NumberFormatException e) {
            if (!zipCodeField.getText().trim().isEmpty()) {
                showAlert(AlertType.ERROR, "Please only input numbers into the zip code field");
                return;
            }

            address = new Address(street, city, state);
        }

        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        currentFetchedCustomer = new Customer(name, phone, email, address);
        CustomerController.insertCustomer(currentFetchedCustomer);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Customer successfully included");
        alert.showAndWait();
        currentFetchedCustomer = null;
        clearAllFields();
    }

    // Update Customer operation
    public void handleUpdateClick() {
        if (areAnyRequiredFieldsEmpty()) {
            showAlert(AlertType.ERROR,
                    "Please fill in all required fields marked with a red * when updating a customer.");
            return;
        }
        if (currentFetchedCustomer == null) {
            showAlert(AlertType.ERROR,
                    "Please search for an existing customer first before updating.");
            clearAllFields();
            return;
        }

        currentFetchedCustomer.setName(nameField.getText());
        currentFetchedCustomer.setPhone(phoneField.getText());
        currentFetchedCustomer.setEmail(emailField.getText());
        currentFetchedCustomer.getAddress().setStreet(streetField.getText());
        currentFetchedCustomer.getAddress().setCity(cityField.getText());
        currentFetchedCustomer.getAddress().setState(stateField.getText());
        int zipCode;
        try {
            zipCode = Integer.parseInt(zipCodeField.getText());
            currentFetchedCustomer.getAddress().setZipCode(zipCode);
        } catch (NumberFormatException e) {
            // do not update if zip code is non-null and not a number
            if (!zipCodeField.getText().trim().isEmpty()) {
                showAlert(AlertType.ERROR, "Please only input numbers into the zip code field");
                return;
            }
        }

        AddressController.updateAddress(currentFetchedCustomer.getAddress());
        CustomerController.updateCustomer(currentFetchedCustomer);

        showAlert(AlertType.INFORMATION, "Customer successfully updated");
        clearAllFields();
        currentFetchedCustomer = null;
    }

    // Delete Customer operation
    public void handleDeleteClick() {
        if (currentFetchedCustomer == null) {
            showAlert(AlertType.ERROR,
                    "Please search for an existing customer first before deleting.");
            clearAllFields();
            return;
        }

        try {
            CustomerController.deleteCustomer(currentFetchedCustomer.getId());
            showAlert(AlertType.INFORMATION, "Customer successfully deleted.");
        } catch (Exception e) {
            showAlert(AlertType.ERROR,
                    "The customer to delete could not be found. Please search for an existing customer first before deleting.");
        }

        currentFetchedCustomer = null;
        clearAllFields();
    }

    // go back to main window
    public void handleBackClick(ActionEvent e) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader f = new FXMLLoader(CustomerWindowController.class.getResource("main-view.fxml"));
        Scene newScene = new Scene(f.load());
        stage.setScene(newScene);
        stage.setTitle("Customer");
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean areAnyRequiredFieldsEmpty() {
        return nameField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty()
                || streetField.getText().trim().isEmpty() || cityField.getText().trim().isEmpty()
                || stateField.getText().trim().isEmpty();
    }

    private void clearAllFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        streetField.clear();
        stateField.clear();
        cityField.clear();
        zipCodeField.clear();
    }
}
