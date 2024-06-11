package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;



public class DashBoardController extends AnchorPane{
    public AnchorPane rootPane;
    public AnchorPane root;
    public Label titleText;
    public Label adminLabel;
    public String admin;
    public JFXButton cashRegBtn;
    public JFXButton custBtn;
    public JFXButton prescBtn;
    public JFXButton employeeBtn;
    public JFXButton supBtn;
    public JFXButton itemBtn;
    public JFXButton reportBtn;
    public JFXButton testBtn;
    public JFXButton logOutBtn;
    public AnchorPane temp;
    public JFXButton homeBtn;


    public void initialize() {
        Platform.runLater(()-> root.requestFocus());
        try {
            titleText.setText("Temperature & Inventory Monitoring");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/temp_form.fxml"));
                temp = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setUsername(String username) {
        admin = username;
        adminLabel.setText(username);
    }
    public void cashRegBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Cash Register");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cash_register.fxml"));
            AnchorPane cashRegister = loader.load();

            CashRegisterFormController controller = loader.getController();
            controller.usrLbl.setText(admin);
            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(cashRegister);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void custBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Customer");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer_form.fxml"));
            AnchorPane customer = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prescBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Prescription");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/presc_form.fxml"));
            AnchorPane cashRegister = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(cashRegister);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employeeBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Employee");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employee_form.fxml"));
            AnchorPane employee = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Supplier");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/supplier_form.fxml"));
            AnchorPane supplier = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(supplier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Item");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/item_form.fxml"));
            AnchorPane item = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().addAll(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Report");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report_form.fxml"));
            AnchorPane report = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Test");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/test_form.fxml"));
            AnchorPane cashRegister = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(cashRegister);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOutBtnClickOnAction(ActionEvent actionEvent) {
        try {
            TempFormController tempFormController = new TempFormController();
            tempFormController.stopReading();

            Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/member_identifier_form.fxml"));
            Scene scene = new Scene(rootNode);

            Stage stage = (Stage)this.root.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Log in");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void onBtnNavigation(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("F1")) {
            cashRegBtn.requestFocus();
            cashRegBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F4")) {
            custBtn.requestFocus();
            custBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F5")) {
            prescBtn.requestFocus();
            prescBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F2")) {
            employeeBtn.requestFocus();
            employeeBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F6")) {
            supBtn.requestFocus();
            supBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F3")) {
            itemBtn.requestFocus();
            itemBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F7")) {
            reportBtn.requestFocus();
            reportBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F8")) {
            testBtn.requestFocus();
            testBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F9")) {
            logOutBtn.requestFocus();
            logOutBtnClickOnAction(new ActionEvent());
        } else if (keyEvent.getCode().toString().equals("F10")) {
            homeBtn.requestFocus();
            homeBtnClickOnAction(new ActionEvent());
        }
    }

    public void onHelp(ActionEvent actionEvent) {
        Stage stage = new Stage();
        VBox vBox = new VBox();

        try {
            File pdfFile = new File("/home/user/IdeaProjects/medplus-care-pharmacy/src/main/resources/help/Help.pdf");
            if (!pdfFile.exists()) {
                System.out.println("File does not exist: " + pdfFile.getAbsolutePath());
                return;
            }

            PDDocument document = PDDocument.load(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            List<ImageView> imageViews = new ArrayList<>();
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 150);
                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageView imageView = new ImageView(fxImage);
                imageViews.add(imageView);
            }

            vBox.getChildren().addAll(imageViews);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Help Document");
        stage.show();
    }

    public void homeBtnClickOnAction(ActionEvent actionEvent) {
        titleText.setText("Temperature & Inventory Monitoring");
        rootPane.getChildren().clear();
        rootPane.getChildren().add(temp);
    }
}