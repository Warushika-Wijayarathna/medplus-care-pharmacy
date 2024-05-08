package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lk.ijse.medpluscarepharmacy.model.Customer;
import lk.ijse.medpluscarepharmacy.model.Tm.CustomerTm;
import lk.ijse.medpluscarepharmacy.repository.CustomerRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {


    public TableColumn<?,?> colCustId;
    public TableColumn <?,?>colCustName;
    public TableColumn<?,?> colContact;
    public TableColumn<?,?> colEmail;
    public JFXTextField custTxt;
    public JFXTextField contactTxt;
    public JFXTextField emailTxt;
    public TableView<CustomerTm> customerTable;
    public JFXTextField searchBar;
    public TableColumn<CustomerTm, List<JFXButton>> colAction;
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

        Platform.runLater(()->{
            custTxt.requestFocus();
            custTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    contactTxt.requestFocus();
                }
            });
            contactTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    emailTxt.requestFocus();
                }
            });
            emailTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtnOnAction(null);
                }
            });
        });
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

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                CustomerTm customerTm = new CustomerTm(
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getContactNo(),
                        customer.getEmail(),
                        actionBtns
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
                clear();
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
            colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

            colAction.setCellFactory((TableColumn<CustomerTm, List<JFXButton>> column) -> {
                return new TableCell<CustomerTm, List<JFXButton>>() {
                    @Override
                    protected void updateItem(List<JFXButton> buttons, boolean empty) {
                        super.updateItem(buttons, empty);
                        if (empty || buttons == null || buttons.isEmpty()) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox();
                            for (JFXButton button : buttons) {
                                hbox.getChildren().add(button);
                            }
                            setGraphic(hbox);
                        }
                    }

                };
            });


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

            if (isContactNoDuplicate(contact)) {
                new Alert(Alert.AlertType.WARNING, "Contact number already exists!").showAndWait();
                return;
            }

            if (isEmailDuplicate(email)) {
                new Alert(Alert.AlertType.WARNING, "Email already exists!").showAndWait();
                return;
            }

            Customer newCustomer = new Customer(name, contact, email);

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

            List<JFXButton> actionBtns = new ArrayList<>();
            actionBtns.add(updateButton);
            actionBtns.add(deleteButton);

            obList.add(new CustomerTm(newCustomer.getCustomerId(), name, contact, email, actionBtns));

            clear();

            custTxt.requestFocus();

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

    public void onFKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.F11){
            searchBar.requestFocus();
        }
    }

    public void onNameReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, custTxt);
    }

    public void onContactReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.CONTACT, contactTxt);
    }

    public void onEmailReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.EMAIL, emailTxt);
    }

    private boolean isContactNoDuplicate(int contact) throws SQLException {
        List<Customer> existingCustomers = CustomerRepo.getAll();
        for (Customer existingCustomer : existingCustomers) {
            if (existingCustomer.getContactNo() == contact) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailDuplicate(String email) throws SQLException {
        List<Customer> existingCustomers = CustomerRepo.getAll();
        for (Customer existingCustomer : existingCustomers) {
            if (existingCustomer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

}

