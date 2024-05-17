package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.Tm.UserTm;
import lk.ijse.medpluscarepharmacy.model.User;
import lk.ijse.medpluscarepharmacy.repository.EmployeeRepo;
import lk.ijse.medpluscarepharmacy.repository.UserRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.sql.SQLException;
import java.util.Optional;

public class UpdateUsrFormController {

    public JFXTextField usernameTxt;
    public JFXPasswordField passwordTxt;
    public JFXPasswordField reEnterTxt;

    public String id;
    public JFXButton cancelBtn;
    public JFXButton updateBtn;

    public void initialize() {
        usernameTxt.requestFocus();
        usernameTxt.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DOWN){
                passwordTxt.requestFocus();
            }
        });

        passwordTxt.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DOWN){
                reEnterTxt.requestFocus();
            } else if(keyEvent.getCode() == KeyCode.UP){
                usernameTxt.requestFocus();
            }
        });

        reEnterTxt.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.UP){
                passwordTxt.requestFocus();
            } else if(keyEvent.getCode() == KeyCode.DOWN){
                cancelBtn.requestFocus();
            }
        });

        cancelBtn.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.UP) {
                reEnterTxt.requestFocus();
            } else if(keyEvent.getCode() == KeyCode.ENTER) {
                cancelBtnOnAction(new ActionEvent());
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                updateBtn.requestFocus();
            }
        });

        updateBtn.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.LEFT) {
                cancelBtn.requestFocus();
            } else if(keyEvent.getCode() == KeyCode.ENTER) {
                updateBtnOnAction(new ActionEvent());
            } else if (keyEvent.getCode() == KeyCode.UP) {
                reEnterTxt.requestFocus();
            }
        });
    }
    public void cancelBtnOnAction(ActionEvent actionEvent) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        if (!Regex.isTextFieldValid(TextField.NAME, usernameTxt.getText())) {
            usernameTxt.requestFocus();
            return;
        }
        if (!Regex.isTextFieldValid(TextField.PASSWORD, passwordTxt.getText())) {
            passwordTxt.requestFocus();
            return;
        }
        User user = new User(
                id,
                usernameTxt.getText(),
                passwordTxt.getText()
        );

        try {
            UserRepo.update(user);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User Updated!");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    closeForm();
                }
            });

            ((EmployeeFormController) usernameTxt.getScene().getUserData()).loadAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsrData(UserTm user) {
        id = user.getUserId();
        String name = user.getUserName();
        String password = user.getPassword();

        usernameTxt.setText(name);
        passwordTxt.setText(password);
        reEnterTxt.setText(password);

    }

    public void onUsrKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, usernameTxt);
    }

    public void onPasswordKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PASSWORD, passwordTxt);
    }

    public void onReEnterKeyReleased(KeyEvent keyEvent) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Should match with password");

        //input should match with passwordtxt
        if (passwordTxt.getText().equals(reEnterTxt.getText())) {
            reEnterTxt.setFocusColor(javafx.scene.paint.Paint.valueOf("Green"));
            reEnterTxt.setUnFocusColor(javafx.scene.paint.Paint.valueOf("Green"));
        } else {
            reEnterTxt.setFocusColor(javafx.scene.paint.Paint.valueOf("Red"));
            reEnterTxt.setUnFocusColor(javafx.scene.paint.Paint.valueOf("Red"));
            //validating msg
            reEnterTxt.getValidators().add(validator);
            reEnterTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    reEnterTxt.validate();
                }
            });
        }
    }
}
