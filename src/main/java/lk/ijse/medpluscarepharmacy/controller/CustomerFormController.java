package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import lk.ijse.medpluscarepharmacy.model.Customer;
import lk.ijse.medpluscarepharmacy.model.Tm.CustomerTm;
import lk.ijse.medpluscarepharmacy.repository.CustomerRepo;
import lk.ijse.medpluscarepharmacy.repository.SupplierRepo;

import java.sql.SQLException;
import java.util.List;

public class CustomerFormController {


    public TableColumn<?,?> colCustId;
    public TableColumn <?,?>colCustName;
    public TableColumn<?,?> colContact;
    public TableColumn<?,?> colEmail;
    public TableColumn<?,?> colUpdate;
    public TableColumn<?,?> colDelete;
    public JFXTextField custTxt;
    public JFXTextField contactTxt;
    public JFXTextField emailTxt;
    public TableView<CustomerTm> customerTable;
    public JFXTextField searchBar;
    ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
    public CustomerTm selectedCustomer;

    public void initialize(){
        setCellValueFactory();
        loadAllCustomers();
        searchCustomer();

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                customerTable.requestFocus();
            }
        });
        searchBar.requestFocus();
    }

    private void searchCustomer() {
        FilteredList<CustomerTm> filteredData = new FilteredList<>(obList, b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customerTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if (String.valueOf(customerTm.getCustomerId()).contains(searchKey)) {
                    return true;
                } else if (customerTm.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(customerTm.getContactNo()).contains(searchKey)) {
                    return true;
                } else if (customerTm.getEmail().toLowerCase().contains(searchKey)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<CustomerTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());
        customerTable.setItems(sortedData);
    }

    private void loadAllCustomers() {
        obList.clear();
        try {
            List<Customer> custList = CustomerRepo.getAll();
            for (Customer customer : custList) {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateCustomer(customer));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteCustomer(customer));

                CustomerTm customerTm = new CustomerTm(
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getContactNo(),
                        customer.getEmail(),
                        updateButton,
                        deleteButton
                );
                obList.add(customerTm);
            }
            customerTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteCustomer(Customer customer) {
        if (customer != null) {
            try {
                CustomerRepo.delete(customer);
                obList.remove(selectedCustomer);
                new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted successfully!").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").showAndWait();
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a customer to delete!").showAndWait();
        }
    }


    private void handleUpdateCustomer(Customer customer) {
        if (selectedCustomer != null) {

            String custId = selectedCustomer.getCustomerId();
            String name = selectedCustomer.getName();
            String contact = String.valueOf(selectedCustomer.getContactNo());
            String email = selectedCustomer.getEmail();


            Customer updatedCustomer = new Customer(
                    custId,
                    custTxt.getText(),
                    Integer.parseInt(contactTxt.getText()),
                    emailTxt.getText()
            );

            try {
                CustomerRepo.update(updatedCustomer);

                selectedCustomer.setName(name);
                selectedCustomer.setContactNo(Integer.parseInt(contact));
                selectedCustomer.setEmail(email);

                clear();

                new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated");
                loadAllCustomers();
                searchCustomer();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a customer to update!");

        }
    }


    private void setCellValueFactory() {
        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void addBtnOnAction(ActionEvent actionEvent) {
        String name = custTxt.getText().trim();
        String contactText = contactTxt.getText().trim();
        String email = emailTxt.getText().trim();

        if (name.isEmpty() || contactText.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
            return;
        }

        try {
            int contact = Integer.parseInt(contactText);
            Customer newCustomer = new Customer( name, contact, email);

            CustomerRepo.add(newCustomer);

            String generatedCustomerId = CustomerRepo.generateCustomerId(newCustomer);
            newCustomer.setCustomerId(generatedCustomerId);

            new Alert(Alert.AlertType.CONFIRMATION, "Customer added successfully!").showAndWait();

            ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
            updateIcon.setFitWidth(20);
            updateIcon.setFitHeight(20);

            JFXButton updateButton = new JFXButton();
            updateButton.setGraphic(updateIcon);
            updateButton.setOnAction(event -> handleUpdateCustomer(newCustomer));

            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
            deleteIcon.setFitWidth(20);
            deleteIcon.setFitHeight(20);

            JFXButton deleteButton = new JFXButton();
            deleteButton.setGraphic(deleteIcon);
            deleteButton.setOnAction(event -> handleDeleteCustomer(newCustomer));

            obList.add(new CustomerTm(newCustomer.getCustomerId(), name, contact, email, updateButton, deleteButton));

            clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid contact number!").showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add customer!").showAndWait();
            e.printStackTrace();
        }
    }


    public void onMouseClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedCustomer = customerTable.getItems().get(selectedIndex);

                String custId = selectedCustomer.getCustomerId();
                String name = selectedCustomer.getName();
                String contact = String.valueOf(selectedCustomer.getContactNo());
                String email = selectedCustomer.getEmail();

                custTxt.setText(name);
                contactTxt.setText(contact);
                emailTxt.setText(email);
            }
        }
    }

    public void clear(){
        custTxt.clear();
        contactTxt.clear();
        emailTxt.clear();
    }
}

