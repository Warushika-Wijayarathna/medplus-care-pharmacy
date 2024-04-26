package lk.ijse.medpluscarepharmacy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardController {
    public AnchorPane rootPane;
    public AnchorPane root;
    public Label titleText;

    public void cashRegBtnClickOnAction(ActionEvent actionEvent) {
        try {
            titleText.setText("Cash Register");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cash_register.fxml"));
            AnchorPane cashRegister = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(cashRegister);
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
            rootPane.getChildren().add(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prescBtnClickOnAction(ActionEvent actionEvent) {
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
            rootPane.getChildren().add(item);
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
}
