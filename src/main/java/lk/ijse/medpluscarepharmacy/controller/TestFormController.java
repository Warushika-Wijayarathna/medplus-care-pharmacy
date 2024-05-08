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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Test;
import lk.ijse.medpluscarepharmacy.model.Tm.CustomerTm;
import lk.ijse.medpluscarepharmacy.model.Tm.TestTm;
import lk.ijse.medpluscarepharmacy.repository.TestRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TestFormController {
    public TableView<TestTm> testTable;
    public TableColumn<?,?> colTestId;
    public TableColumn<?,?> colDesc;
    public TableColumn<?,?> colLab;
    public TableColumn<?,?> colSampleType;
    public TableColumn<?,?> colTestType;
    public TableColumn<?,?> colPrice;
    public JFXTextField testTxt;
    public JFXTextField descTxt;
    public JFXTextField labTxt;
    public JFXTextField searchBar;
    public JFXTextField sampleTypeTxt;
    public JFXTextField testTypeTxt;
    public JFXTextField priceTxt;
    public JFXButton addBtn;

    ObservableList<TestTm> obList = FXCollections.observableArrayList();
    public TestTm selectedTest;
    public TableColumn<TestTm, List<JFXButton>> colAction;

    public void initialize(){
        setCellValueFactory();
        loadAllTests();
        searchTest();

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                testTable.requestFocus();
            }
        });

        Platform.runLater(()->{
            descTxt.requestFocus();
            descTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    labTxt.requestFocus();
                }
            });

            labTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    sampleTypeTxt.requestFocus();
                }
            });

            sampleTypeTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    testTypeTxt.requestFocus();
                }
            });

            testTypeTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    priceTxt.requestFocus();
                }
            });

            priceTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtn.requestFocus();
                }
            });

            addBtn.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtnOnAction(new ActionEvent());
                }
            });
        });
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

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateTest(test));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteTest(test));

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                TestTm testTm = new TestTm(
                        test.getTestId(),
                        test.getDescription(),
                        test.getLab(),
                        test.getSampleType(),
                        test.getTestType(),
                        test.getPrice(),
                        actionBtns
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
                Platform.runLater(()-> {
                    new Alert(Alert.AlertType.CONFIRMATION, "Test deleted successfully!").showAndWait();
                });
            } catch (SQLException e) {
                Platform.runLater(()-> {
                            new Alert(Alert.AlertType.ERROR, "Failed to delete test!").showAndWait();
                });
                e.printStackTrace();
            }
        } else {
            Platform.runLater(()->{
                new Alert(Alert.AlertType.WARNING, "Please select a test to delete!").showAndWait();
            });
        }
    }


    private void handleUpdateTest(Test test) {
        if (selectedTest != null) {

            String testId = selectedTest.getTestId();
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

                Platform.runLater(()->{
                    new Alert(Alert.AlertType.CONFIRMATION, "Test Updated");
                });
                loadAllTests();
                searchTest();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {
            Platform.runLater(()->{
                new Alert(Alert.AlertType.WARNING, "Please select a test to update!");
            });

        }
    }


    private void setCellValueFactory() {
        colTestId.setCellValueFactory(new PropertyValueFactory<>("testId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colLab.setCellValueFactory(new PropertyValueFactory<>("lab"));
        colSampleType.setCellValueFactory(new PropertyValueFactory<>("sampleType"));
        colTestType.setCellValueFactory(new PropertyValueFactory<>("testType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colAction.setCellFactory((TableColumn<TestTm, List<JFXButton>> column) -> {
            return new TableCell<TestTm, List<JFXButton>>() {
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

        String desc = descTxt.getText().trim();
        String lab = labTxt.getText().trim();
        String sampleType = sampleTypeTxt.getText().trim();
        String testType = testTypeTxt.getText().trim();
        String price = priceTxt.getText().trim();

        if (desc.isEmpty() || lab.isEmpty() || sampleType.isEmpty() || testType.isEmpty() || price.isEmpty()) {
            Platform.runLater(()->{
                new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
            });
            return;
        }

        try {
            double priceOf = Double.parseDouble(price);

            Test newTest= new Test( desc, lab, sampleType, testType, priceOf);

            TestRepo.add(newTest);

            String generatedTestId = TestRepo.generateTestId(newTest);
            newTest.setTestId(generatedTestId);

            JFXButton updateButton = new JFXButton();
            ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
            updateIcon.setFitWidth(20);
            updateIcon.setFitHeight(20);
            updateButton.setGraphic(updateIcon);
            updateButton.setOnAction(event -> handleUpdateTest(newTest));

            JFXButton deleteButton = new JFXButton();
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
            deleteIcon.setFitWidth(20);
            deleteIcon.setFitHeight(20);
            deleteButton.setGraphic(deleteIcon);
            deleteButton.setOnAction(event -> handleDeleteTest(newTest));

            List<JFXButton> actionBtns = new ArrayList<>();
            actionBtns.add(updateButton);
            actionBtns.add(deleteButton);

            Platform.runLater(()->{
                new Alert(Alert.AlertType.CONFIRMATION, "Test added successfully!").showAndWait();
            });

            obList.add(new TestTm(newTest.getTestId(), desc, lab, sampleType, testType, priceOf, actionBtns));

            clear();
        } catch (NumberFormatException e) {
            Platform.runLater(()-> {
                new Alert(Alert.AlertType.ERROR, "Invalid contact number!").showAndWait();
            });
        } catch (SQLException e) {
            Platform.runLater(()->{
                new Alert(Alert.AlertType.ERROR, "Failed to add customer!").showAndWait();
            });
            e.printStackTrace();
        }
    }


    public void onMouseClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = testTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedTest = testTable.getItems().get(selectedIndex);

                String testId = selectedTest.getTestId();
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

    public void onClickAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) testTable.getScene().getWindow();
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
                    String desc = data[0].trim();
                    String lab = data[1].trim();
                    String sampleType = data[2].trim();
                    String testType = data[3].trim();
                    double price = Double.parseDouble(data[4].trim());

                    Test newTest = new Test(desc, lab, sampleType, testType, price);
                    TestRepo.add(newTest);

                    String generatedTestId = TestRepo.generateTestId(newTest);
                    newTest.setTestId(generatedTestId);

                    JFXButton updateButton = new JFXButton();
                    ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                    updateIcon.setFitWidth(20);
                    updateIcon.setFitHeight(20);
                    updateButton.setGraphic(updateIcon);
                    updateButton.setOnAction(event -> handleUpdateTest(newTest));

                    JFXButton deleteButton = new JFXButton();
                    ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                    deleteIcon.setFitWidth(20);
                    deleteIcon.setFitHeight(20);
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.setOnAction(event -> handleDeleteTest(newTest));


                    List<JFXButton> actionBtns = new ArrayList<>();
                    actionBtns.add(updateButton);
                    actionBtns.add(deleteButton);

                    obList.add(new TestTm(newTest.getTestId(), desc, lab, sampleType, testType, price, actionBtns));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price value in CSV: " + line);
                }
            }
            Platform.runLater(()->{
                new Alert(Alert.AlertType.CONFIRMATION, "Test data imported successfully!").showAndWait();
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(()->{
                new Alert(Alert.AlertType.ERROR, "Failed to import test data!").showAndWait();
            });
        }
    }

    public void onFKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.F11){
            searchBar.requestFocus();
        }
    }

    public void onDesc(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.DESCRIPTION, descTxt);
    }

    public void onLab(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, labTxt);
    }

    public void onSample(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, sampleTypeTxt);
    }

    public void onType(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, testTypeTxt);
    }

    public void onPrice(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PRICE, priceTxt);
    }
}
