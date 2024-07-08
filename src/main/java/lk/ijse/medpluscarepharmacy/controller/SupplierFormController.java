package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Supplier;
import lk.ijse.medpluscarepharmacy.model.Test;
import lk.ijse.medpluscarepharmacy.model.Tm.CustomerTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SupplierTm;
import lk.ijse.medpluscarepharmacy.model.Tm.TestTm;
import lk.ijse.medpluscarepharmacy.repository.SupplierRepo;
import lk.ijse.medpluscarepharmacy.repository.TestRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public JFXButton addBtn;
    ObservableList<SupplierTm> obList = FXCollections.observableArrayList();
    public SupplierTm selectedSupplier;
    public TableColumn<SupplierTm, List<JFXButton>> colAction;

    public void initialize(){
        setCellValueFactory();
        loadAllSuppliers();
        searchSupplier();

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                supplierTable.requestFocus();
            }
        });
        Platform.runLater(()->{
            suppTxt.requestFocus();
            suppTxt.setOnKeyPressed(event ->{
                if (event.getCode()==KeyCode.ENTER) {
                    contactTxt.requestFocus();
                }

            });

            contactTxt.setOnKeyPressed(event ->{
                if (event.getCode() == KeyCode.ENTER) {
                    emailTxt.requestFocus();
                }
            });

            emailTxt.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    addBtn.requestFocus();
                }
            });
        });
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

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                SupplierTm supplierTm = new SupplierTm(
                        supplier.getSupplierId(),
                        supplier.getName(),
                        supplier.getContact(),
                        supplier.getEmail(),
                        actionBtns
                );
                obList.add(supplierTm);
            }
            supplierTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteSupplier(Supplier supplier) {
        if (supplier != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this supplier?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    SupplierRepo.delete(supplier);
                    obList.remove(supplier);
                    Platform.runLater(()-> {
                        new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").showAndWait();
                    });
                } catch (SQLException e) {
                    Platform.runLater(()-> {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete supplier!").showAndWait();
                    });
                    e.printStackTrace();
                }
            }
        } else {
            Platform.runLater(()-> {
                new Alert(Alert.AlertType.WARNING, "Please select a supplier to delete!").showAndWait();
            });
        }
    }


    private void handleUpdateSupplier(Supplier supplier) {
        if (selectedSupplier != null) {

            String supplierId = selectedSupplier.getSupplierId();
            String name = selectedSupplier.getName();
            String contact = String.valueOf(selectedSupplier.getContact());
            String email = selectedSupplier.getEmail();


            if (!Regex.isTextFieldValid(TextField.NAME, name)) {
                new Alert(Alert.AlertType.WARNING, "Invalid Supplier Name").showAndWait();
                suppTxt.requestFocus();
                return;
            }

            if (!Regex.isTextFieldValid(TextField.CONTACT, contact)) {
                new Alert(Alert.AlertType.WARNING, "Invalid Contact Number").showAndWait();
                contactTxt.requestFocus();
                return;
            }

            if (!Regex.isTextFieldValid(TextField.EMAIL, email)) {
                new Alert(Alert.AlertType.WARNING, "Invalid Email").showAndWait();
                emailTxt.requestFocus();
                return;
            }

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

                Platform.runLater(()-> {
                    new Alert(Alert.AlertType.CONFIRMATION, "SupplierUpdated");
                });
                loadAllSuppliers();
                searchSupplier();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(()-> {
                new Alert(Alert.AlertType.WARNING, "Please select a supplier to update!");
            });

        }
    }


    private void setCellValueFactory() {
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSuppName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colAction.setCellFactory((TableColumn<SupplierTm, List<JFXButton>> column) -> {
            return new TableCell<SupplierTm, List<JFXButton>>() {
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
        String name = suppTxt.getText().trim();
        String contactText = contactTxt.getText().trim();
        String email = emailTxt.getText().trim();

        if (!Regex.isTextFieldValid(TextField.NAME, name)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Supplier Name").showAndWait();
            suppTxt.requestFocus();
            return;
        }

        if (!Regex.isTextFieldValid(TextField.CONTACT, contactText)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Contact Number").showAndWait();
            contactTxt.requestFocus();
            return;
        }

        if (!Regex.isTextFieldValid(TextField.EMAIL, email)) {
            new Alert(Alert.AlertType.WARNING, "Invalid Email").showAndWait();
            emailTxt.requestFocus();
            return;
        }

        if (name.isEmpty() || contactText.isEmpty() || email.isEmpty()) {
            Platform.runLater(()-> {
                new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
            });
            return;
        }

        try {

            int contact = Integer.parseInt(contactText);

            if (isContactNoDuplicate(contact)) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.WARNING, "Contact number already exists!").showAndWait();
                });
                return;
            }

            if (isEmailDuplicate(email)) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.WARNING, "Email already exists!").showAndWait();
                });
                return;
            }

            Supplier newSupplier = new Supplier(name, contact, email);

            SupplierRepo.add(newSupplier);

            String generatedSupplierId = SupplierRepo.generateSupplierId(newSupplier);
            newSupplier.setSupplierId(generatedSupplierId);

            Platform.runLater(()-> {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier added successfully!").showAndWait();
            });

            ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
            updateIcon.setFitWidth(20);
            updateIcon.setFitHeight(20);

            JFXButton updateButton = new JFXButton();
            updateButton.setGraphic(updateIcon);
            updateButton.setOnAction(event -> handleUpdateSupplier(newSupplier));

            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
            deleteIcon.setFitWidth(20);
            deleteIcon.setFitHeight(20);

            JFXButton deleteButton = new JFXButton();
            deleteButton.setGraphic(deleteIcon);
            deleteButton.setOnAction(event -> handleDeleteSupplier(newSupplier));

            List<JFXButton> actionBtns = new ArrayList<>();
            actionBtns.add(updateButton);
            actionBtns.add(deleteButton);

            obList.add(new SupplierTm(newSupplier.getSupplierId(), name, contact, email, actionBtns));

            suppTxt.clear();
            contactTxt.clear();
            emailTxt.clear();

        } catch (NumberFormatException e) {
            Platform.runLater(()-> {
                new Alert(Alert.AlertType.ERROR, "Invalid contact number!").showAndWait();
            });
        } catch (SQLException e) {
            Platform.runLater(()-> {
                new Alert(Alert.AlertType.ERROR, "Failed to add supplier!").showAndWait();
            });
            e.printStackTrace();
        }
    }


    public void onMouseClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = supplierTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedSupplier = supplierTable.getItems().get(selectedIndex);

                String supplierId = selectedSupplier.getSupplierId();
                String name = selectedSupplier.getName();
                String contact = String.valueOf(selectedSupplier.getContact());
                String email = selectedSupplier.getEmail();

                suppTxt.setText(name);
                contactTxt.setText(contact);
                emailTxt.setText(email);
            }
        }
    }

    public void addKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode()==KeyCode.ENTER) {
            addBtnOnAction(new ActionEvent());
        }
    }

    public void onSuppName(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME,suppTxt);
    }

    public void onSuppContact(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.CONTACT,contactTxt);
    }

    public void onSuppEmail(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.EMAIL,emailTxt);
    }

    public void onFKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.F11){
            searchBar.requestFocus();
        }
    }

    private boolean isContactNoDuplicate(int contact) throws SQLException {
        List<Supplier> existingSuppliers = SupplierRepo.getAll(); // Retrieve all existing suppliers
        for (Supplier existingSupplier : existingSuppliers) {
            if (existingSupplier.getContact() == contact) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailDuplicate(String email) throws SQLException {
        List<Supplier> existingSuppliers = SupplierRepo.getAll(); // Retrieve all existing suppliers
        for (Supplier existingSupplier : existingSuppliers) {
            if (existingSupplier.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public void onClickAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) supplierTable.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            insertTestData(filePath);
        }
    }

    private void insertTestData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    String name = data[0].trim();
                    int contact = Integer.parseInt(data[1].trim());
                    String email = data[2].trim();

                    Supplier newSupplier= new Supplier(name, contact, email);
                    SupplierRepo.add(newSupplier);

                    String generatedSupplierId = SupplierRepo.generateSupplierId(newSupplier);
                    newSupplier.setSupplierId(generatedSupplierId);

                    JFXButton updateButton = new JFXButton();
                    ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                    updateIcon.setFitWidth(20);
                    updateIcon.setFitHeight(20);
                    updateButton.setGraphic(updateIcon);
                    updateButton.setOnAction(event -> handleUpdateSupplier(newSupplier));

                    JFXButton deleteButton = new JFXButton();
                    ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                    deleteIcon.setFitWidth(20);
                    deleteIcon.setFitHeight(20);
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.setOnAction(event -> handleDeleteSupplier(newSupplier));


                    List<JFXButton> actionBtns = new ArrayList<>();
                    actionBtns.add(updateButton);
                    actionBtns.add(deleteButton);

                    obList.add(new SupplierTm(newSupplier.getSupplierId(), name, contact, email, actionBtns));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price value in CSV: " + line);
                }
            }
            Platform.runLater(()->{
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier data imported successfully!").showAndWait();
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(()->{
                new Alert(Alert.AlertType.ERROR, "Failed to import Supplier data!").showAndWait();
            });
        }
    }
}
