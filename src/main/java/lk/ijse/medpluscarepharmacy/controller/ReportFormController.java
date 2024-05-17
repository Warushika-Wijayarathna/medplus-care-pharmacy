package lk.ijse.medpluscarepharmacy.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.medpluscarepharmacy.model.Customer;
import lk.ijse.medpluscarepharmacy.model.Report;
import lk.ijse.medpluscarepharmacy.repository.CustomerRepo;
import lk.ijse.medpluscarepharmacy.repository.ReportRepo;
import lk.ijse.medpluscarepharmacy.model.Tm.ReportTm;
import lk.ijse.medpluscarepharmacy.repository.TestRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;
import javafx.stage.FileChooser;
import com.itextpdf.text.Paragraph;

import java.io.FileOutputStream;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReportFormController {

    public JFXComboBox testComBox;
    public JFXTextField resultText;
    public TableView<ReportTm> reportTable;
    public TableColumn<?,?> colReport;
    public TableColumn<?,?> colTest;
    public TableColumn<?,?> colIssue;
    public TableColumn<?,?> colPickUp;
    public TableColumn<?,?> colResult;

    public JFXTextField searchBar;
    public DatePicker issueDatePicker;
    public DatePicker pickUpDatePicker;
    public JFXTextField searchCustomer;
    public TableColumn<?,?> colCust;
    public ImageView upload;
    ObservableList<ReportTm> obList = FXCollections.observableArrayList();
    public ReportTm selectedReport;
    private ObservableList allTestNames;
    public TableColumn<ReportTm, List<JFXButton>> colAction;
    public Label custEmail;
    public Label custName;
    public File pdfResult;
    @FXML
    private Label name;

    public void initialize() {
        setCellValueFactory();
        loadAllReports();
        upload.setVisible(false);
        custEmail.setVisible(false);
        custName.setVisible(false);
        name.setVisible(false);

        Platform.runLater(() -> {
            searchCustomer.requestFocus();
            searchCustomer.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    searchCustomerByContactNumber();
                    testComBox.requestFocus();
                }
            }
            );

            testComBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    resultText.requestFocus();
                }
            });

            resultText.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    issueDatePicker.requestFocus();
                }
            });

            issueDatePicker.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    pickUpDatePicker.requestFocus();
                }
            });

            pickUpDatePicker.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtnOnAction(new ActionEvent());
                }
            });

        });

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
                        report.getCustId(),
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
            String custId = selectedReport.getCustId();
            String testId = selectedReport.getTestId();
            String result = selectedReport.getResult();
            String issueDate = selectedReport.getIssueDate().toString();
            String pickUpDate = selectedReport.getPickupDate().toString();

            if (!Regex.isTextFieldValid(TextField.DESCRIPTION,custId)){
                new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").showAndWait();
                custName.requestFocus();
                return;
            }

            Report updatedReport = new Report(
                    reportId,
                    searchCustomer.getText(),
                    testComBox.getValue().toString(),
                    resultText.getText(),
                    issueDatePicker.getValue(),
                    pickUpDatePicker.getValue()
            );

            try {
                ReportRepo.update(updatedReport);

                selectedReport.setCustId(custId);
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
        colCust.setCellValueFactory(new PropertyValueFactory<>("custId"));
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
            String name = custName.getText();
            String testId = testComBox.getValue().toString().trim();
            String testName = TestRepo.getTestName(testId);
            String result = resultText.getText().trim();
            LocalDate issueDate = issueDatePicker.getValue();
            LocalDate pickUpDate = pickUpDatePicker.getValue();
            System.out.println("Result     :"+result);

            if (!Regex.isTextFieldValid(TextField.DESCRIPTION,name)){
                new Alert(Alert.AlertType.ERROR, "Invalid Customer Name").showAndWait();
                custName.requestFocus();
                return;
            }

            if (name.isEmpty() || testId.isEmpty()|| issueDate == null || pickUpDate == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
                return;
            }

            if (result.isEmpty()) {
                System.out.println("Result     :"+result);

                sendDelayedEmail();
            } else {

                String nextId = ReportRepo.getNextId();
                //create pdf file adding data from the report form

                Document document = new Document();

                    // Create a new PdfWriter instance
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Report_" + nextId + ".pdf"));

                    // Open the PDF document
                    document.open();

                    // Add content to the document
                    document.add(new Paragraph("Report ID: " + nextId));
                    document.add(new Paragraph("Customer : " + name));
                    document.add(new Paragraph("Test ID: " + testId));
                    document.add(new Paragraph("Test : " + testName));
                    document.add(new Paragraph("Result: " + result));
                    document.add(new Paragraph("Issue Date: " + issueDate));
                    document.add(new Paragraph("Pickup Date: " +pickUpDate));

                    // Close the document
                    document.close();

                    // Close the writer
                    writer.close();

                    File pdfFile = new File("Report_" + nextId + ".pdf");

                    System.out.println("PDF Created!");
                    sendInstantEmail(pdfFile);
            }

            Report newReport = new Report(name,testId, result, issueDate, pickUpDate);

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
            obList.add(new ReportTm(newReport.getReportId(), name,testId, result, issueDate, pickUpDate, actionBtns));

            clear();
            loadAllReports();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add report!").showAndWait();
            throw new RuntimeException(e);
        }


    }

    private void sendInstantEmail(File document) {
        // Recipient's email ID needs to be mentioned.
        String to = custEmail.getText();
        System.out.println(to);

        // Sender's email ID needs to be mentioned
        String from = "www.thilankathushani@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("www.thilankathushani@gmail.com", "widp uvup qnge gvre");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Your Test Results Are Back!");

            // Now set the actual message

            //Customize the msg
            message.setText("Dear " + name + ",\n\n" +
                    "Your test results are ready. Please find the attached PDF file for your test results.\n\n" +
                    "Thank you for choosing MedPlus Care Pharmacy.\n\n" +
                    "Best Regards,\n" +
                    "MedPlus Care Pharmacy");

            // attach pdfResult
            Multipart multipart = new MimeMultipart();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(document);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDelayedEmail() {
        // Recipient's email ID needs to be mentioned.
        String to = custEmail.getText();
        System.out.println(to);

        // Sender's email ID needs to be mentioned
        String from = "www.thilankathushani@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("www.thilankathushani@gmail.com", "widp uvup qnge gvre");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Your Test Results Are Back!");

            // Now set the actual message

            //Customize the msg
            message.setText("Dear " + name + ",\n\n" +
                    "Your test results are ready. Please find the attached PDF file for your test results.\n\n" +
                    "Thank you for choosing MedPlus Care Pharmacy.\n\n" +
                    "Best Regards,\n" +
                    "MedPlus Care Pharmacy");

            // attach pdfResult
            Multipart multipart = new MimeMultipart();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(pdfResult);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onKeyPressedAction(KeyEvent keyEvent) {
        if (keyEvent.getCode()== KeyCode.ENTER){
            searchReport();
            reportTable.requestFocus();
        }
        searchBar.requestFocus();

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
                String custId = selectedReport.getCustId();
                String testId = selectedReport.getTestId();
                String result = selectedReport.getResult();
                LocalDate issueDate = selectedReport.getIssueDate();
                LocalDate pickUpDate = selectedReport.getPickupDate();

                Customer customer= CustomerRepo.searchCustomerByCustId(custId);

                searchCustomer.setText(customer.getName());
                testComBox.setValue(testId);
                resultText.setText(result);
                issueDatePicker.setValue(issueDate);
                pickUpDatePicker.setValue(pickUpDate);
            }

        }
    }

    public void clear(){
        searchCustomer.clear();
        testComBox.setValue(null);
        resultText.clear();
        issueDatePicker.setValue(null);
        pickUpDatePicker.setValue(null);
    }

    public void onSelectAction(ActionEvent actionEvent) {
        try {
            String testId = (String) testComBox.getSelectionModel().getSelectedItem();
            boolean isInstant = ReportRepo.checkInstant(testId);
            System.out.println(isInstant);

            if (isInstant) {
                upload.setVisible(false);
                resultText.setVisible(true);
                resultText.setOpacity(1.0);
            } else {
                upload.setVisible(true);
                resultText.setVisible(false);
                resultText.setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                issueDatePicker.requestFocus();
                            }
                        }
                );

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

    public void onSearchCustomer(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            searchCustomerByContactNumber();
            testComBox.requestFocus();
        }

    }

    private void searchCustomerByContactNumber() {
        String contactNumber = searchCustomer.getText().trim();
        if (!contactNumber.isEmpty()) {
            try {
                Customer customer = CustomerRepo.searchCustomerByContact(contactNumber);
                if (customer != null) {
                    searchCustomer.setText(customer.getName());
                    custEmail.setText(customer.getEmail());
                    custName.setText(customer.getCustomerId());
                    name.setText(customer.getName());
                    System.out.println(customer.getEmail());
                    System.out.println(custEmail.getText());
                    System.out.println(custName.getText());
                    System.out.println(name.getText());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer not found").showAndWait();
                    Platform.runLater(() -> {
                        searchCustomer.requestFocus();
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR,"Please enter a contact number!!");
        }
    }

    public void onCustomerReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.DESCRIPTION, searchCustomer);
    }

    public void onAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            pdfResult=selectedFile;
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection cancelled.");
        }
    }
}
