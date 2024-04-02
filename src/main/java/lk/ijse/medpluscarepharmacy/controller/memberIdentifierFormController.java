package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class memberIdentifierFormController {
    public JFXTextField userNameTxt;
    public Label userNameLabel;
    public Label passwordLabel;
    public JFXButton logInBtn;
    public AnchorPane rootPane;
    public JFXTextField showPassword;
    public JFXPasswordField hidePassword;
    public JFXToggleButton showToggleBtn;

    @FXML
    private RadioButton employerBtn, employeeBtn;
    private ToggleGroup positionGroup;
    String password;

    public void initialize(){

        showToggleBtn.setOnAction(this::showToggleBtnClickOnAction);

        // Initially hide the plain text password field
        showPassword.setVisible(false);


    }

    @FXML
    public void LogInBtnClickOnAction(ActionEvent actionEvent) {

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
