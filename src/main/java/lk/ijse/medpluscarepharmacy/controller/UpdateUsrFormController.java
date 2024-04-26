package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.Tm.UserTm;
import lk.ijse.medpluscarepharmacy.model.User;
import lk.ijse.medpluscarepharmacy.repository.EmployeeRepo;
import lk.ijse.medpluscarepharmacy.repository.UserRepo;

import java.sql.SQLException;
import java.util.Optional;

public class UpdateUsrFormController {

    public JFXTextField usernameTxt;
    public JFXPasswordField passwordTxt;
    public JFXPasswordField reEnterTxt;

    public String id;

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) usernameTxt.getScene().getWindow();
        stage.close();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
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
}
