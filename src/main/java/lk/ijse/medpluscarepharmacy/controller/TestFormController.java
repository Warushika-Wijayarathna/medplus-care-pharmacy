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
import lk.ijse.medpluscarepharmacy.model.Test;
import lk.ijse.medpluscarepharmacy.model.Tm.TestTm;
import lk.ijse.medpluscarepharmacy.repository.TestRepo;

import java.sql.SQLException;
import java.util.List;


public class TestFormController {
    public TableView<TestTm> testTable;
    public TableColumn<?,?> colTestId;
    public TableColumn<?,?> colDesc;
    public TableColumn<?,?> colLab;
    public TableColumn<?,?> colSampleType;
    public TableColumn<?,?> colTestType;
    public TableColumn<?,?> colPrice;
    public TableColumn<?,?> colUpdate;
    public TableColumn<?,?> colDelete;
    public JFXTextField testTxt;
    public JFXTextField descTxt;
    public JFXTextField labTxt;
    public JFXTextField searchBar;
    public JFXTextField sampleTypeTxt;
    public JFXTextField testTypeTxt;
    public JFXTextField priceTxt;

    ObservableList<TestTm> obList = FXCollections.observableArrayList();
    public TestTm selectedTest;
    public JFXButton updateButton;
    public JFXButton deleteButton;

    public void initialize(){
        setCellValueFactory();
        loadAllTests();
        searchTest();

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                testTable.requestFocus();
            }
        });
        searchBar.requestFocus();
    }

    private void searchTest() {
        FilteredList<TestTm> filteredData = new FilteredList<>(obList, b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(testTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if (String.valueOf(testTm.getTestId()).contains(searchKey)) {
                    return true;
                } else if (testTm.getDescription().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (testTm.getLab().contains(searchKey)) {
                    return true;
                } else if (testTm.getSampleType().toLowerCase().contains(searchKey)) {
                    return true;
                }else if (testTm.getTestType().toLowerCase().contains(searchKey)) {
                    return true;
                }else if (String.valueOf(testTm.getPrice()).contains(searchKey)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<TestTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(testTable.comparatorProperty());
        testTable.setItems(sortedData);
    }

    private void loadAllTests() {
        obList.clear();
        try {
            List<Test> testList = TestRepo.getAll();
            for (Test test : testList) {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateTest(test));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteTest(test));

                TestTm testTm = new TestTm(
                        test.getTestId(),
                        test.getDescription(),
                        test.getLab(),
                        test.getSampleType(),
                        test.getTestType(),
                        test.getPrice(),
                        updateButton,
                        deleteButton
                );
                obList.add(testTm);
            }
            testTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteTest(Test test) {
        if (test != null) {
            try {
                TestRepo.delete(test);
                obList.remove(selectedTest);
                new Alert(Alert.AlertType.CONFIRMATION, "Test deleted successfully!").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to delete test!").showAndWait();
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a test to delete!").showAndWait();
        }
    }


    private void handleUpdateTest(Test test) {
        if (selectedTest != null) {

            int testId = selectedTest.getTestId();
            String desc = selectedTest.getDescription();
            String lab = selectedTest.getLab();
            String sampleType = selectedTest.getSampleType();
            String testType = selectedTest.getTestType();
            double price = selectedTest.getPrice();


            Test updatedTest = new Test(
                    testId,
                    descTxt.getText(),
                    labTxt.getText(),
                    sampleTypeTxt.getText(),
                    testTypeTxt.getText(),
                    Double.parseDouble(priceTxt.getText())
            );

            try {
                TestRepo.update(updatedTest);

                selectedTest.setDescription(desc);
                selectedTest.setLab(lab);
                selectedTest.setSampleType(sampleType);
                selectedTest.setTestType(testType);
                selectedTest.setPrice(price);

                clear();

                new Alert(Alert.AlertType.CONFIRMATION, "Test Updated");
                loadAllTests();
                searchTest();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a test to update!");

        }
    }


    private void setCellValueFactory() {
        colTestId.setCellValueFactory(new PropertyValueFactory<>("testId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colLab.setCellValueFactory(new PropertyValueFactory<>("lab"));
        colSampleType.setCellValueFactory(new PropertyValueFactory<>("sampleType"));
        colTestType.setCellValueFactory(new PropertyValueFactory<>("testType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void addBtnOnAction(ActionEvent actionEvent) {

        String desc = descTxt.getText().trim();
        String lab = labTxt.getText().trim();
        String sampleType = sampleTypeTxt.getText().trim();
        String testType = testTypeTxt.getText().trim();
        String price = priceTxt.getText().trim();

        if (desc.isEmpty() || lab.isEmpty() || sampleType.isEmpty() || testType.isEmpty() || price.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
            return;
        }

        try {
            double priceOf = Double.parseDouble(price);

            Test newTest= new Test( desc, lab, sampleType, testType, priceOf);

            TestRepo.add(newTest);

            new Alert(Alert.AlertType.CONFIRMATION, "Test added successfully!").showAndWait();

            obList.add(new TestTm(newTest.getTestId(), desc, lab, sampleType, testType, priceOf, updateButton, deleteButton));

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
            int selectedIndex = testTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedTest = testTable.getItems().get(selectedIndex);

                int testId = selectedTest.getTestId();
                String desc = selectedTest.getDescription();
                String lab = selectedTest.getLab();
                String sampleType = selectedTest.getSampleType();
                String testType = selectedTest.getTestType();
                double price = selectedTest.getPrice();

                descTxt.setText(desc);
                labTxt.setText(lab);
                sampleTypeTxt.setText(sampleType);
                testTypeTxt.setText(testType);
                priceTxt.setText(String.valueOf(price));
            }
        }
    }

    public void clear(){
        descTxt.clear();
        labTxt.clear();
        sampleTypeTxt.clear();
        testTypeTxt.clear();
        priceTxt.clear();
    }
}
