package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.repository.UserRepo;

import java.io.IOException;
import java.sql.SQLException;

public class MemberIdentifierFormController {
    public JFXTextField userNameTxt;
    public Label userNameLabel;
    public Label passwordLabel;
    public JFXButton logInBtn;
    public JFXTextField showPassword;
    public JFXPasswordField hidePassword;
    public JFXToggleButton showToggleBtn;
    String password;
    public AnchorPane rootNode;

    public void initialize(){

        showToggleBtn.setOnAction(this::showToggleBtnClickOnAction);

        showPassword.setVisible(false);


    }

    @FXML
    public void LogInBtnClickOnAction(ActionEvent actionEvent) {
        String username=userNameTxt.getText();
        String password=hidePassword.getText();

        boolean isUserVerified = false;
        try {
            isUserVerified = UserRepo.check(username, password);
            if (isUserVerified) {
                navigateToDashboard();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Username or password is incorrect").show();
            throw new RuntimeException(e);
        }
    }

    private void navigateToDashboard()  {
        try {
            Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
            Scene scene = new Scene(rootNode);

            Stage stage = (Stage)this.rootNode.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Dashboard Form");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showToggleBtnClickOnAction(ActionEvent actionEvent) {
        if (showToggleBtn.isSelected()) {
            showPassword.setText(hidePassword.getText());
            showPassword.setVisible(true);
            hidePassword.setVisible(false);
            return;
        }
        hidePassword.setText(showPassword.getText());
        hidePassword.setVisible(true);
        showPassword.setVisible(false);
    }

    public void hidePasswordOnAction(KeyEvent keyEvent) {
        showPassword.setVisible(false);
    }
}
