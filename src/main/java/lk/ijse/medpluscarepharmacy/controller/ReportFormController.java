package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import lk.ijse.medpluscarepharmacy.model.Report;
import lk.ijse.medpluscarepharmacy.model.Tm.CustomerTm;
import lk.ijse.medpluscarepharmacy.repository.ReportRepo;
import lk.ijse.medpluscarepharmacy.model.Tm.ReportTm;
import lk.ijse.medpluscarepharmacy.repository.TestRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportFormController {

    public JFXComboBox testComBox;
    public JFXTextField resultText;
    public TableView<ReportTm> reportTable;
    public TableColumn<?,?> colReport;
    public TableColumn<?,?> colTest;
    public TableColumn<?,?> colIssue;
    public TableColumn<?,?> colPickUp;
    public TableColumn<?,?> colResult;
    public TableColumn<?,?> colUpdate;
    public TableColumn<?,?> colDelete;
    public JFXTextField searchBar;
    public DatePicker issueDatePicker;
    public DatePicker pickUpDatePicker;
    ObservableList<ReportTm> obList = FXCollections.observableArrayList();
    public ReportTm selectedReport;
    private ObservableList allTestNames;
    public TableColumn<ReportTm, List<JFXButton>> colAction;

    public void initialize() {
        setCellValueFactory();
        loadAllReports();

        try {
            allTestNames = FXCollections.observableArrayList(TestRepo.getAllTestNames());
            testComBox.setItems(allTestNames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadAllReports() {
        obList.clear();
        try {
            List<Report> allReports = ReportRepo.getAll();
            for(Report report : allReports){
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateReport(report));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteReport(report));

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                ReportTm reportTm = new ReportTm(
                        report.getReportId(),
                        report.getTestId(),
                        report.getResult(),
                        report.getIssueDate(),
                        report.getPickupDate(),
                        actionBtns
                );
                obList.add(reportTm);
            }
            reportTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteReport(Report report) {
        if (report != null) {
            try {
                ReportRepo.delete(report.getReportId());
                obList.remove(selectedReport);
                new Alert(Alert.AlertType.CONFIRMATION, "Report deleted successfully!").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Report to delete customer!").showAndWait();
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a report to delete!").showAndWait();
        }
    }

    private void handleUpdateReport(Report report) {
        if (selectedReport != null) {
            String reportId = selectedReport.getReportId();
            String testId = selectedReport.getTestId();
            String result = selectedReport.getResult();
            String issueDate = selectedReport.getIssueDate().toString();
            String pickUpDate = selectedReport.getPickupDate().toString();

            Report updatedReport = new Report(
                    reportId,
                    testComBox.getValue().toString(),
                    resultText.getText(),
                    issueDatePicker.getValue(),
                    pickUpDatePicker.getValue()
            );

            try {
                ReportRepo.update(updatedReport);

                selectedReport.setTestId(testId);
                selectedReport.setResult(result);
                selectedReport.setIssueDate(LocalDate.parse(issueDate));
                selectedReport.setPickupDate(LocalDate.parse(pickUpDate));

                loadAllReports();
                new Alert(Alert.AlertType.CONFIRMATION, "Report updated successfully!").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the report!").showAndWait();
                e.printStackTrace();
            }

        }

    }

    private void setCellValueFactory() {
        colReport.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        colTest.setCellValueFactory(new PropertyValueFactory<>("testId"));
        colResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        colIssue.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colPickUp.setCellValueFactory(new PropertyValueFactory<>("pickupDate"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colAction.setCellFactory((TableColumn<ReportTm, List<JFXButton>> column) -> {
            return new TableCell<ReportTm, List<JFXButton>>() {
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
        try {
            String testId = testComBox.getValue().toString().trim();
            String result = resultText.getText().trim();
            LocalDate issueDate = issueDatePicker.getValue();
            LocalDate pickUpDate = pickUpDatePicker.getValue();

            if ( testId.isEmpty()|| issueDate == null || pickUpDate == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
                return;
            }

            Report newReport = new Report(testId, result, issueDate, pickUpDate);

            ReportRepo.add(newReport);

            String generatedReportId = ReportRepo.generateReportId(newReport);
            newReport.setReportId(generatedReportId);



            ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
            updateIcon.setFitWidth(20);
            updateIcon.setFitHeight(20);

            JFXButton updateButton = new JFXButton();
            updateButton.setGraphic(updateIcon);
            updateButton.setOnAction(event -> handleUpdateReport(newReport));

            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
            deleteIcon.setFitWidth(20);
            deleteIcon.setFitHeight(20);

            JFXButton deleteButton = new JFXButton();
            deleteButton.setGraphic(deleteIcon);
            deleteButton.setOnAction(event -> handleDeleteReport(newReport));

            List<JFXButton> actionBtns = new ArrayList<>();
            actionBtns.add(updateButton);
            actionBtns.add(deleteButton);

            new Alert(Alert.AlertType.CONFIRMATION, "Report added successfully!").showAndWait();
            obList.add(new ReportTm(newReport.getReportId(), testId, result, issueDate, pickUpDate, actionBtns));

            clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add report!").showAndWait();
            throw new RuntimeException(e);
        }


    }

    public void onKeyPressedAction(KeyEvent keyEvent) {
        if (keyEvent.getCode()== KeyCode.ENTER){
            reportTable.requestFocus();
        }
        searchBar.requestFocus();
        searchReport();
    }

    private void searchReport() {
        FilteredList<ReportTm> filteredList = new FilteredList<>(obList, b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(reportTm -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (reportTm.getReportId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reportTm.getTestId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reportTm.getResult().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reportTm.getIssueDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reportTm.getPickupDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<ReportTm> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(reportTable.comparatorProperty());
        reportTable.setItems(sortedList);
    }

    public void onMouseClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = reportTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedReport = reportTable.getItems().get(selectedIndex);

                String reportId = selectedReport.getReportId();
                String testId = selectedReport.getTestId();
                String result = selectedReport.getResult();
                LocalDate issueDate = selectedReport.getIssueDate();
                LocalDate pickUpDate = selectedReport.getPickupDate();

                testComBox.setValue(testId);
                resultText.setText(result);
                issueDatePicker.setValue(issueDate);
                pickUpDatePicker.setValue(pickUpDate);
            }

        }
    }

    public void clear(){
        testComBox.setValue(null);
        resultText.clear();
        issueDatePicker.setValue(null);
        pickUpDatePicker.setValue(null);
    }

    public void onSelectAction(ActionEvent actionEvent) {
        try {
            String testId = (String) testComBox.getSelectionModel().getSelectedItem();
            boolean isInstant = ReportRepo.checkInstant(testId);

            if (isInstant) {
                resultText.setEditable(true);
                resultText.setOpacity(1.0);
            } else {
                resultText.setEditable(false);
                resultText.setOpacity(0.5);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onFKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.F11){
            searchBar.requestFocus();
        }
    }
    public void onResult(KeyEvent keyEvent){
        Regex.setTextColor(TextField.DESCRIPTION, resultText);
    }
}
