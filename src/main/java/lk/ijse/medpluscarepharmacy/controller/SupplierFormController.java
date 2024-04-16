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
import lk.ijse.medpluscarepharmacy.model.Supplier;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SupplierTm;
import lk.ijse.medpluscarepharmacy.repository.SupplierRepo;

import java.sql.SQLException;
import java.util.List;

public class SupplierFormController {


    public TableColumn<?,?> colSuppId;
    public TableColumn <?,?>colSuppName;
    public TableColumn<?,?> colContact;
    public TableColumn<?,?> colEmail;
    public TableColumn<?,?> colUpdate;
    public TableColumn<?,?> colDelete;
    public JFXTextField suppTxt;
    public JFXTextField contactTxt;
    public JFXTextField emailTxt;
    public TableView<SupplierTm> supplierTable;
    public JFXTextField searchBar;
    ObservableList<SupplierTm> obList = FXCollections.observableArrayList();
    public SupplierTm selectedSupplier;

    public void initialize(){
        setCellValueFactory();
        loadAllSuppliers();
        searchSupplier();

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                supplierTable.requestFocus();
            }
        });
        searchBar.requestFocus();
    }

    private void searchSupplier() {
        FilteredList<SupplierTm> filteredData = new FilteredList<>(obList, b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplierTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if (String.valueOf(supplierTm.getSupplierId()).contains(searchKey)) {
                    return true;
                } else if (supplierTm.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(supplierTm.getContact()).contains(searchKey)) {
                    return true;
                } else if (supplierTm.getEmail().toLowerCase().contains(searchKey)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<SupplierTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(supplierTable.comparatorProperty());
        supplierTable.setItems(sortedData);
    }

    private void loadAllSuppliers() {
        obList.clear();
        try {
            List<Supplier> supplierList = SupplierRepo.getAll();
            for (Supplier supplier : supplierList) {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateSupplier(supplier));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteSupplier(supplier));

                SupplierTm supplierTm = new SupplierTm(
                        supplier.getSupplierId(),
                        supplier.getName(),
                        supplier.getContact(),
                        supplier.getEmail(),
                        updateButton,
                        deleteButton
                );
                obList.add(supplierTm);
            }
            supplierTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteSupplier(Supplier supplier) {
    }

    private void handleUpdateSupplier(Supplier supplier) {
        if (selectedSupplier != null) {

            int supplierId = selectedSupplier.getSupplierId();
            String name = selectedSupplier.getName();
            String contact = String.valueOf(selectedSupplier.getContact());
            String email = selectedSupplier.getEmail();


            Supplier updatedSupplier = new Supplier(
                    supplierId,
                    suppTxt.getText(),
                    Integer.parseInt(contactTxt.getText()),
                    emailTxt.getText()
            );

            try {
                SupplierRepo.update(updatedSupplier);

                selectedSupplier.setName(name);
                selectedSupplier.setContact(Integer.parseInt(contact));
                selectedSupplier.setEmail(email);

                suppTxt.clear();
                contactTxt.clear();
                emailTxt.clear();

                new Alert(Alert.AlertType.CONFIRMATION, "SupplierUpdated");
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a supplier to update!");

        }
    }


    private void setCellValueFactory() {
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSuppName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void addBtnOnAction(ActionEvent actionEvent) {
        String name = suppTxt.getText().trim();
        String contactText = contactTxt.getText().trim();
        String email = emailTxt.getText().trim();

        if (name.isEmpty() || contactText.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
            return;
        }

        try {
            int contact = Integer.parseInt(contactText);
            Supplier newSupplier = new Supplier( name, contact, email);

            SupplierRepo.add(newSupplier);

            new Alert(Alert.AlertType.CONFIRMATION, "Supplier added successfully!").showAndWait();

            obList.add(new SupplierTm(newSupplier.getSupplierId(), name, contact, email, null, null));

            suppTxt.clear();
            contactTxt.clear();
            emailTxt.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid contact number!").showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add supplier!").showAndWait();
            e.printStackTrace();
        }
    }


    public void onMouseClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = supplierTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedSupplier = supplierTable.getItems().get(selectedIndex);

                int supplierId = selectedSupplier.getSupplierId();
                String name = selectedSupplier.getName();
                String contact = String.valueOf(selectedSupplier.getContact());
                String email = selectedSupplier.getEmail();

                suppTxt.setText(name);
                contactTxt.setText(contact);
                emailTxt.setText(email);
            }
        }
    }

}
