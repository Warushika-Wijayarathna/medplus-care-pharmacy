package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lk.ijse.medpluscarepharmacy.model.Prescription;
import lk.ijse.medpluscarepharmacy.model.Test;
import lk.ijse.medpluscarepharmacy.model.Tm.*;
import lk.ijse.medpluscarepharmacy.repository.*;
import lk.ijse.medpluscarepharmacy.repository.PrescSaveRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescFormController {

    public JFXComboBox testComBox;
    public TableColumn<?,?> colPrescId;
    public TableColumn<?,?> colCustomer;
    public TableColumn<?,?> colPatient;
    public TableColumn<?,?> colAge;
    public TableColumn<?,?> colMedicalOfficer;
    public TableColumn<?,?> colContext;
    public TableColumn<?,?> colDuration;
    public TableColumn<?,?> colDate;
    public TableColumn<?,?> colUpdate;
    public TableColumn<?,?> colDelete;
    public TableView<PrescTm> prescTable;
    public JFXTextField patientTxt;
    public JFXTextField ageTxt;
    public JFXTextField medicalOfficerTxt;
    public JFXButton addBtn;
    public JFXTextField searchBar;
    public TableView<SmallTestTm> testCart;
    public TableColumn<?,?> colRemove;
    public JFXComboBox custCombox;
    public JFXTextField durationTxt;
    public DatePicker datePicker;
    public JFXTextArea contextText;
    public TableColumn<?,?> colTestId;
    public TableColumn<?,?> colDescName;
    ObservableList<PrescTm> observableList = FXCollections.observableArrayList();
    public PrescTm selectedPresc;
    private ObservableList<String> allTestNames;
    private ObservableList<String> allCustNames;
    ObservableList<SmallTestTm> obList = FXCollections.observableArrayList();
    public TableColumn<PrescTm, List<JFXButton>> colAction;


    public void initialize() {

        setCellValueFactory();
        loadAllPrescriptions();
        loadTestComBox();
        loadCustComBox();

        Platform.runLater(() -> {
            custCombox.requestFocus();

            custCombox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    patientTxt.requestFocus();
                }
            });

            patientTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    ageTxt.requestFocus();
                }
            });

            ageTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    medicalOfficerTxt.requestFocus();
                }
            });

            medicalOfficerTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    contextText.requestFocus();
                }
            });

            durationTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    datePicker.requestFocus();
                }
            });

            datePicker.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    testComBox.requestFocus();
                }
            });

            testComBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtn.requestFocus();
                }
            });

        });

    }

    private void loadCustComBox() {
        try {
            allCustNames = FXCollections.observableArrayList(CustomerRepo.getAllCustNames());
            custCombox.setItems(allCustNames);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTestComBox() {
        try {
            allTestNames = FXCollections.observableArrayList(TestRepo.getAllTestsNames());
            testComBox.setItems(allTestNames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadAllPrescriptions() {
        try {
            List<Prescription> prescriptions = PrescriptionRepo.getAll();
            observableList.clear();

            for(Prescription prescription : prescriptions){
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdatePresc(prescription));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeletePresc(prescription));

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                PrescTm prescTm = new PrescTm(
                        prescription.getPrescriptionId(),
                        prescription.getCustomerId(),
                        prescription.getPatientName(),
                        prescription.getAge(),
                        prescription.getMedicalOfficerName(),
                        prescription.getContext(),
                        prescription.getDuration(),
                        prescription.getDate(),
                        actionBtns
                );

                observableList.add(prescTm);
            }
            prescTable.setItems(observableList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void handleDeletePresc(Prescription prescription) {
        if (prescription != null) {
            try {
                boolean isDeleted = false;

                isDeleted = PrescTestDetailRepo.deletePresc(prescription);
                if (isDeleted) {
                    obList.clear();
                    loadAllPrescriptions();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete prescription.").showAndWait();
                    return;
                }
                new Alert(Alert.AlertType.INFORMATION, "Prescription deleted successfully.").showAndWait();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to delete prescription.").showAndWait();
                throw new RuntimeException(e);
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please select a prescription to delete.").showAndWait();
        }
    }

    private void handleUpdatePresc(Prescription prescription) {
        if (selectedPresc != null) {
            String prescId = selectedPresc.getPrescriptionId();
            String custId = selectedPresc.getCustomerId();
            String patient = selectedPresc.getPatientName();
            String age = String.valueOf(selectedPresc.getAge());
            String medicalOfficer = selectedPresc.getMedicalOfficerName();
            String context = selectedPresc.getContext();
            String duration = selectedPresc.getDuration();
            String date = selectedPresc.getDate().toString();

            if (!Regex.isTextFieldValid(TextField.NAME,patient)) {
                new Alert(Alert.AlertType.ERROR, "Invalid patient name.").showAndWait();
                patientTxt.requestFocus();
                return;
            }

            if (!Regex.isTextFieldValid(TextField.AGE,ageTxt.getText())) {
                new Alert(Alert.AlertType.ERROR, "Invalid age.").showAndWait();
                ageTxt.requestFocus();
                return;
            }

            if (!Regex.isTextFieldValid(TextField.NAME,medicalOfficer)) {
                new Alert(Alert.AlertType.ERROR, "Invalid medical officer name.").showAndWait();
                medicalOfficerTxt.requestFocus();
                return;
            }

            if (!Regex.isTextFieldValid(TextField.DESCRIPTION,context)) {
                new Alert(Alert.AlertType.ERROR, "Invalid context.").showAndWait();
                contextText.requestFocus();
                return;
            }

            if (!Regex.isTextFieldValid(TextField.DESCRIPTION,duration)) {
                new Alert(Alert.AlertType.ERROR, "Invalid duration.").showAndWait();
                durationTxt.requestFocus();
                return;
            }

            Prescription updatedPresc = new Prescription(
                    prescId,
                    custCombox.getValue().toString(),
                    patientTxt.getText(),
                    Integer.parseInt(ageTxt.getText()),
                    medicalOfficerTxt.getText(),
                    contextText.getText(),
                    durationTxt.getText(),
                    datePicker.getValue()
            );

            List<String> allTestIds = new ArrayList<>();
            for (SmallTestTm smallTestTm : testCart.getItems()) {
                allTestIds.add(smallTestTm.getTestId());
            }

            boolean isUpdated = PrescTestDetailRepo.updatePrescription(updatedPresc, allTestIds);

            if(isUpdated){
                obList.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update prescription.").showAndWait();
                return;
            }

            try {
                PrescriptionRepo.update(updatedPresc);

                selectedPresc.setCustomerId(custId);
                selectedPresc.setPatientName(patient);
                selectedPresc.setAge(Integer.parseInt(age));
                selectedPresc.setMedicalOfficerName(medicalOfficer);
                selectedPresc.setContext(context);
                selectedPresc.setDuration(duration);
                selectedPresc.setDate(LocalDate.parse(date));

                clear();

                new Alert(Alert.AlertType.INFORMATION, "Prescription updated successfully.").showAndWait();

                loadAllPrescriptions();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update prescription.").showAndWait();
                throw new RuntimeException(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a prescription to update.").showAndWait();
        }
    }

    private void loadTestsToCart(String prescId) {
        testCart.getItems().clear();

        try {
            List<Test> tests = PrescTestDetailRepo.getTestsByPrescriptionId(prescId);

            for (Test test : tests) {

                JFXButton removeButton = new JFXButton("Remove");
                removeButton.setOnAction(event -> {
                    removeFromCart(test);
                });

                SmallTestTm smallTestTm = new SmallTestTm(test.getTestId(),test.getDescription(),removeButton);


                testCart.getItems().add(smallTestTm);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load tests to cart.").showAndWait();
            throw new RuntimeException(e);
        }
    }

    private void removeFromCart(Test test) {
        testCart.getItems().remove(test);
    }


    private void setCellValueFactory() {
        colPrescId.setCellValueFactory(new PropertyValueFactory<>("prescriptionId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colMedicalOfficer.setCellValueFactory(new PropertyValueFactory<>("medicalOfficerName"));
        colContext.setCellValueFactory(new PropertyValueFactory<>("context"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colAction.setCellFactory((TableColumn<PrescTm, List<JFXButton>> column) -> {
            return new TableCell<PrescTm, List<JFXButton>>() {
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

        colTestId.setCellValueFactory(new PropertyValueFactory<>("testId"));
        colDescName.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("remove"));
    }

    public void onKeyPressedAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            prescTable.requestFocus();
        }
        searchBar.requestFocus();
        searchPresc();
    }

    private void searchPresc() {
            FilteredList<PrescTm> filteredList = new FilteredList<>(observableList, e -> true);
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(prescTm -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (prescTm.getPrescriptionId().contains(newValue)) {
                        return true;
                    } else if (prescTm.getCustomerId().contains(newValue)) {
                        return true;
                    } else if (prescTm.getPatientName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(prescTm.getAge()).contains(newValue)) {
                        return true;
                    } else if (prescTm.getMedicalOfficerName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (prescTm.getContext().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (prescTm.getDuration().contains(newValue)) {
                        return true;
                    } else if (prescTm.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });


        SortedList<PrescTm> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(prescTable.comparatorProperty());
        prescTable.setItems(sortedList);

    }

    public void onTestSelectOption(ActionEvent actionEvent) {
        String selectedTest = (String) testComBox.getSelectionModel().getSelectedItem();

        if (selectedTest != null) {
            try {
                Test test = TestRepo.getTestByDescription(selectedTest);
                if (test != null) {
                    ImageView removeIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (47).png")));
                    removeIcon.setFitWidth(20);
                    removeIcon.setFitHeight(20);

                    JFXButton remove = new JFXButton();
                    remove.setGraphic(removeIcon);
                    remove.setOnAction(e -> {
                        testCart.getItems().remove(new SmallSupplierTm(test.getTestId(), test.getTestId(), remove));
                    });

                    SmallTestTm smallTestTm = new SmallTestTm(test.getTestId(), test.getDescription(), remove);
                    testCart.getItems().add(smallTestTm);
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to add test to cart.").showAndWait();
                throw new RuntimeException(e);
            }
        }

    }

    public void onMouseClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = prescTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedPresc = prescTable.getItems().get(selectedIndex);

                String prescId = selectedPresc.getPrescriptionId();
                String custId = selectedPresc.getCustomerId();
                String patient = selectedPresc.getPatientName();
                String age = String.valueOf(selectedPresc.getAge());
                String medicalOfficer = selectedPresc.getMedicalOfficerName();
                String context = selectedPresc.getContext();
                String duration = selectedPresc.getDuration();
                String date = selectedPresc.getDate().toString();

                patientTxt.setText(patient);
                ageTxt.setText(age);
                medicalOfficerTxt.setText(medicalOfficer);
                contextText.setText(context);
                durationTxt.setText(duration);
                datePicker.setValue(LocalDate.parse(date));
                custCombox.setValue(custId);
                durationTxt.setText(duration);
                datePicker.setValue(LocalDate.parse(date));

                loadTestsToCart(prescId);
            }
        }
    }

    public void addBtnOnAction(ActionEvent actionEvent) {
        String prescId = null;
        try {
            prescId = PrescriptionRepo.generatePrescriptionId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate prescription ID.").showAndWait();
            throw new RuntimeException(e);
        }

        String custId = (String) custCombox.getSelectionModel().getSelectedItem();
        String patient = patientTxt.getText();
        int age = Integer.parseInt(ageTxt.getText());
        String medicalOfficer = medicalOfficerTxt.getText();
        String context = contextText.getText();
        String duration = durationTxt.getText();
        LocalDate date = datePicker.getValue();

        if (!Regex.isTextFieldValid(TextField.NAME,patient)) {
            new Alert(Alert.AlertType.ERROR, "Invalid patient name.").showAndWait();
            patientTxt.requestFocus();
            return;
        }

        if (!Regex.isTextFieldValid(TextField.AGE,ageTxt.getText())) {
            new Alert(Alert.AlertType.ERROR, "Invalid age.").showAndWait();
            ageTxt.requestFocus();
            return;
        }

        if (!Regex.isTextFieldValid(TextField.NAME,medicalOfficer)) {
            new Alert(Alert.AlertType.ERROR, "Invalid medical officer name.").showAndWait();
            medicalOfficerTxt.requestFocus();
            return;
        }

        if (!Regex.isTextFieldValid(TextField.DESCRIPTION,context)) {
            new Alert(Alert.AlertType.ERROR, "Invalid context.").showAndWait();
            contextText.requestFocus();
            return;
        }

        if (!Regex.isTextFieldValid(TextField.DESCRIPTION,duration)) {
            new Alert(Alert.AlertType.ERROR, "Invalid duration.").showAndWait();
            durationTxt.requestFocus();
            return;
        }


        Prescription newPresc = new Prescription(prescId, custId, patient, age, medicalOfficer, context, duration, date);

        List<String> testIds = testCart.getItems().stream()
                .map(SmallTestTm::getTestId)
                .collect(Collectors.toList());

        try {
            boolean isPrescSaved = PrescSaveRepo.addPrescriptionWithTestIds(newPresc, testIds);

            new Alert(Alert.AlertType.INFORMATION, "Prescription added successfully.").showAndWait();

            loadAllPrescriptions();
            clear();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add prescription.").showAndWait();
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void custComboxKeyPressed(KeyEvent keyEvent) {
        String userInput = custCombox.getEditor().getText().toLowerCase();

        if (userInput.isEmpty()) {
            custCombox.setItems(allCustNames);
        } else {
            ObservableList<String> filteredList = FXCollections.observableArrayList();
            for (String name : allCustNames) {
                if (name.toLowerCase().contains(userInput)) {
                    filteredList.add(name);
                }
            }
            custCombox.setItems(filteredList);
        }
        custCombox.show();
    }

    @FXML
    public void testComboxKeyPressed(KeyEvent keyEvent) {
        String userInput = testComBox.getEditor().getText().toLowerCase();

        if (userInput.isEmpty()) {
            testComBox.setItems(allTestNames);
        } else {
            ObservableList<String> filteredList = FXCollections.observableArrayList();
            for (String name : allTestNames) {
                if (name.toLowerCase().contains(userInput)) {
                    filteredList.add(name);
                }
            }
            testComBox.setItems(filteredList);
        }
        testComBox.show();
    }

    public void clear(){
        custCombox.setValue(null);
        patientTxt.clear();
        ageTxt.clear();
        medicalOfficerTxt.clear();
        contextText.clear();
        durationTxt.clear();
        datePicker.setValue(null);
    }

    public void onPatientName(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, patientTxt);
    }

    public void onAge(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.AGE, ageTxt);
    }

    public void onMedicalOfficer(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, medicalOfficerTxt);
    }

    public void onDuration(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.DESCRIPTION, durationTxt);
    }

    public void onContext(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.DESCRIPTION, contextText);
    }
}
