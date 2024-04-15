package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.Tm.EmployeeTm;
import lk.ijse.medpluscarepharmacy.repository.EmployeeRepo;

import java.sql.SQLException;
import java.util.Optional;

public class UpdateEmFormController {

    public JFXTextField employeeNameTxt;
    public JFXTextField positionTxt;
    public JFXTextField addressTxt;
    public JFXTextField contactNo;
    public JFXTextField salaryTxt;

    public Integer id;

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
}
