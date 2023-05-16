package com.assignment4;

import com.assignment4.controllers.CustomerController;
import com.assignment4.controllers.OrderController;
import com.assignment4.dao.EntityNotFoundException;
import com.assignment4.model.Customer;
import com.assignment4.model.Order;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class OrderWindowController implements Initializable {

    @FXML
    private TextField numberField;
    @FXML
    private TextField dateField;
    @FXML
    private ChoiceBox<Customer> customerChoiceBox;
    @FXML
    private ChoiceBox<String> itemChoiceBox;
    @FXML
    private TextField priceField;
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

    private Order currentFetchedOrder;

    // Search Order operation
    public void handleSearchClick() {
        if (numberField.getText().trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Please put in an order number to search for an order.");
            return;
        }

        try {
            int number = Integer.parseInt(numberField.getText());
            currentFetchedOrder = OrderController.fetchOrder(number);
            dateField.setText(currentFetchedOrder.getDate().toString());
            customerChoiceBox.setValue(currentFetchedOrder.getCustomer());
            itemChoiceBox.setValue(currentFetchedOrder.getItem());
            priceField.setText(String.valueOf(currentFetchedOrder.getPrice()));
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "You did not input a number in the order number field.");
        } catch (EntityNotFoundException e) {
            showAlert(AlertType.INFORMATION, "No records found.");
        }
    }

    // Add Order operation
    public void handleAddClick() {
        if (areAnyRequiredFieldsEmpty()) {
            showAlert(AlertType.ERROR,
                    "All fields except Number are required when adding. Please try again.");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException e) {
            showAlert(AlertType.ERROR, "Invalid date input. Please try again.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid price input. Please try again.");
            return;
        }

        Customer customer = customerChoiceBox.getValue();
        String item = itemChoiceBox.getValue();

        Order newOrder = new Order(date, item, price, customer);
        OrderController.insertOrder(newOrder);

        showAlert(AlertType.INFORMATION, "Order successfully included.");
        currentFetchedOrder = null;
        clearAllFields();
    }

    public void handleUpdateClick() {
        if (currentFetchedOrder == null) {
            showAlert(AlertType.ERROR, "Please search for an order before updating.");
            return;
        }

        if (areAnyRequiredFieldsEmpty()) {
            showAlert(AlertType.ERROR,
                    "All fields except Number are required when updating. Please try again.");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException e) {
            showAlert(AlertType.ERROR, "Invalid date input. Please try again.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid price input. Please try again.");
            return;
        }

        currentFetchedOrder.setDate(date);
        currentFetchedOrder.setCustomer(customerChoiceBox.getValue());
        currentFetchedOrder.setItem(itemChoiceBox.getValue());
        currentFetchedOrder.setPrice(price);
        OrderController.updateOrder(currentFetchedOrder);
        showAlert(AlertType.INFORMATION, "Order data successfully updated.");
        currentFetchedOrder = null;
        clearAllFields();
    }

    public void handleDeleteClick() {
        if (currentFetchedOrder == null) {
            showAlert(AlertType.ERROR, "Please search for an order before deleting.");
            return;
        }

        try {
            OrderController.deleteOrder(currentFetchedOrder.getNumber());
            showAlert(AlertType.INFORMATION, "Order successfully deleted.");
        } catch (EntityNotFoundException e) {
            showAlert(AlertType.ERROR, "The order to delete could not be found. Please try again.");
        }

        currentFetchedOrder = null;
        clearAllFields();
    }

    public void handleBackClick(ActionEvent e) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader f = new FXMLLoader(OrderWindowController.class.getResource("main-view.fxml"));
        Scene newScene = new Scene(f.load());
        stage.setScene(newScene);
        stage.setTitle("Customer");
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        // fetch all Customers to set choice box list
        customerChoiceBox
                .setItems(FXCollections.observableList(CustomerController.fetchAllCustomers()));

        itemChoiceBox.setItems(
                FXCollections.observableArrayList("Caesar Salad", "Greek Salad", "Cobb Salad"));
    }

    private boolean areAnyRequiredFieldsEmpty() {
        return dateField.getText().trim().isEmpty() || customerChoiceBox.getValue() == null
                || itemChoiceBox.getValue() == null || priceField.getText().trim().isEmpty();
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearAllFields() {
        numberField.clear();
        dateField.clear();
        priceField.clear();
    }
}
