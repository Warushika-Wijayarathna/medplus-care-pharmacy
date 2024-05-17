package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.Tm.EmployeeTm;
import lk.ijse.medpluscarepharmacy.repository.EmployeeRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.sql.SQLException;
import java.util.Optional;

public class UpdateEmFormController {

    public JFXTextField employeeNameTxt;
    public JFXTextField positionTxt;
    public JFXTextField addressTxt;
    public JFXTextField contactNo;
    public JFXTextField salaryTxt;

    public String id;
    public JFXButton cancelBtn;
    public JFXButton updateBtn;

    public void initialize() {
        employeeNameTxt.requestFocus();
        employeeNameTxt.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.DOWN) {
                positionTxt.requestFocus();
            }
        });

        positionTxt.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.DOWN) {
                addressTxt.requestFocus();
            } else if (keyEvent.getCode()== KeyCode.UP) {
                employeeNameTxt.requestFocus();
            }
        });

        addressTxt.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.DOWN) {
                contactNo.requestFocus();
            } else if (keyEvent.getCode()== KeyCode.UP) {
                positionTxt.requestFocus();
            }
        });

        contactNo.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.DOWN) {
                salaryTxt.requestFocus();
            } else if (keyEvent.getCode()== KeyCode.UP) {
                addressTxt.requestFocus();
            }
        });

        salaryTxt.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.UP) {
                contactNo.requestFocus();
            } else if (keyEvent.getCode()==KeyCode.DOWN) {
                cancelBtn.requestFocus();
            }
        });

        cancelBtn.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.UP) {
                salaryTxt.requestFocus();
            } else if (keyEvent.getCode()== KeyCode.ENTER) {
                cancelBtnOnAction(new ActionEvent());
            } else if (keyEvent.getCode()== KeyCode.RIGHT) {
                updateBtn.requestFocus();
            }
        });

        updateBtn.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) {
                salaryTxt.requestFocus();
            } else if (keyEvent.getCode() == KeyCode.ENTER) {
                updateBtnOnAction(new ActionEvent());
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                cancelBtn.requestFocus();
            }
        });

    }
    public void setEmployeeData(EmployeeTm employee) {
        id = employee.getEmployeeId();
        String name = employee.getName();
        String position = employee.getPosition();
        String address = employee.getAddress();
        String contact = employee.getContactNo();
        double salary = employee.getSalary();

        employeeNameTxt.setText(name);
        positionTxt.setText(position);
        addressTxt.setText(address);
        contactNo.setText(contact);
        salaryTxt.setText(String.valueOf(salary));
    }

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        closeForm();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        if (!Regex.isTextFieldValid(TextField.NAME,employeeNameTxt.getText())) {
            employeeNameTxt.requestFocus();
            return;
        }
        if (!Regex.isTextFieldValid(TextField.POSITION,positionTxt.getText())) {
            positionTxt.requestFocus();
            return;
        }
        if (!Regex.isTextFieldValid(TextField.ADDRESS,addressTxt.getText())) {
            addressTxt.requestFocus();
            return;
        }
        if (!Regex.isTextFieldValid(TextField.CONTACT,contactNo.getText())) {
            contactNo.requestFocus();
            return;
        }
        if (!Regex.isTextFieldValid(TextField.SALARY,salaryTxt.getText())) {
            salaryTxt.requestFocus();
            return;
        }

        Employee employee = new Employee(
                id,
                employeeNameTxt.getText(),
                positionTxt.getText(),
                addressTxt.getText(),
                contactNo.getText(),
                Double.parseDouble(salaryTxt.getText())
        );

        try {
            EmployeeRepo.update(employee);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Employee Updated!");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    closeForm();
                }
            });

            ((EmployeeFormController) employeeNameTxt.getScene().getUserData()).loadAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void closeForm() {
        Stage stage = (Stage) employeeNameTxt.getScene().getWindow();
        stage.close();
    }

    public void onEmNameKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME, employeeNameTxt);
    }

    public void onPositionKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.POSITION, positionTxt);
    }

    public void onAddressKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.ADDRESS, addressTxt);
    }

    public void onContactKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.CONTACT, contactNo);
    }

    public void onSalaryKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.SALARY, salaryTxt);
    }
}
