package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.Tm.EmployeeTm;
import lk.ijse.medpluscarepharmacy.model.Tm.UserTm;
import lk.ijse.medpluscarepharmacy.model.User;
import lk.ijse.medpluscarepharmacy.repository.EmployeeRepo;
import lk.ijse.medpluscarepharmacy.repository.UserRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmployeeFormController {
    public JFXTextField employeeNameTxt;
    public JFXTextField positionTxt;
    public JFXTextField addressTxt;
    public JFXTextField contactNo;
    public JFXTextField salaryTxt;
    public CheckBox usrCheckBox;
    public JFXTextField usernameTxt;
    public JFXPasswordField passwordTxt;
    public JFXPasswordField reEnterTxt;
    public JFXButton addBtn;
    public Label usrLbl;
    public Label passwordLbl;
    public Label reEnterLbl;
    @FXML
    public TableColumn <?,?>colId;
    public TableColumn <?,?>colName;
    public TableColumn <?,?>colPosition;
    public TableColumn <?,?>colAddress;
    public TableColumn <?,?>colContact;
    public TableColumn <?,?>colSalary;
    public TableColumn <?,?>colUser;
    public TableView <EmployeeTm>employeeTable;
    public TableColumn <?,?>colEmAction;
    public TableView <UserTm>usrTable;
    public TableColumn <?,?>colUserId;
    public TableColumn <?,?>colUserName;
    public TableColumn <?,?>colPassword;
    public TableColumn <?,?>colUsrAction;
    public TableColumn <?,?>colEmDelete;
    public TableColumn <?,?>colEmUpdate;
    public TableColumn <?,?>colUsrUpdate;
    public TableColumn <?,?>colUsrDelete;
    ObservableList<EmployeeTm> observableList = FXCollections.observableArrayList();
    ObservableList<UserTm> obList = FXCollections.observableArrayList();

    private EmployeeTm selectedEmployee;
    private UserTm selectedUser;
    public void initialize() {

        setCellValueFactory();
        loadAllEmployees();
        loadAllUsers();
        setFieldOpacity(false);
        setTextFieldEditable(false);

        usrCheckBox.setOnAction(event -> {
            boolean isSelected = usrCheckBox.isSelected();
            setFieldOpacity(isSelected);
            setTextFieldEditable(isSelected);
        });

        Platform.runLater(() -> {
            employeeNameTxt.requestFocus();
            employeeNameTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    positionTxt.requestFocus();
                }
            });
            positionTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    addressTxt.requestFocus();
                }
            });
            addressTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    contactNo.requestFocus();
                }
            });
            contactNo.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    salaryTxt.requestFocus();
                }
            });
            salaryTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER){
                    usrCheckBox.requestFocus();
                }
            });
            if (usrCheckBox.isSelected()) {
                usernameTxt.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER){
                        passwordTxt.requestFocus();
                    }
                });
                passwordTxt.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER){
                        reEnterTxt.requestFocus();
                    }
                });
                reEnterTxt.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER){
                        addBtn.requestFocus();
                    }
                });
            }

        });
    }

    public void loadAllUsers() {
        obList.clear();
        try {
            List<User> userList = UserRepo.getAll();
            for (User user : userList) {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateUser(user));

                UserTm userTm = new UserTm(
                        user.getUserId(),
                        user.getUserName(),
                        user.getPassword(),
                        updateButton
                );
                obList.add(userTm);
            }
            usrTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateUser(User user) {
        openUpdateUserForm();
    }

    private void openUpdateUserForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update_usr_form.fxml"));
            Parent root = loader.load();

            UpdateUsrFormController controller = loader.getController();

            if (selectedUser != null) {
                controller.setUsrData(selectedUser);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update User");
            stage.show();

            stage.getScene().setUserData(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllEmployees() {
        observableList.clear();
        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList) {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateEmployee(employee));

                EmployeeTm employeeTm = new EmployeeTm(
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getAddress(),
                        employee.getContactNo(),
                        employee.getSalary(),
                        employee.getUserId(),
                        updateButton
                );
                observableList.add(employeeTm);
            }
            employeeTable.setItems(observableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateEmployee(Employee employee) {
        openUpdateEmployeeForm();
    }


    private void openUpdateEmployeeForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update_em_form.fxml"));
            Parent root = loader.load();

            UpdateEmFormController controller = loader.getController();

            if (selectedEmployee != null) {
                controller.setEmployeeData(selectedEmployee);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Employee");
            stage.show();

            stage.getScene().setUserData(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colEmUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));


        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colUsrUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));

    }

    private void setTextFieldEditable(boolean isEditable) {
        usernameTxt.setEditable(isEditable);
        passwordTxt.setEditable(isEditable);
        reEnterTxt.setEditable(isEditable);
    }
    private void setFieldOpacity(boolean isVisible) {
        double opacity = isVisible ? 1.0 : 0.5;
        usernameTxt.setOpacity(opacity);
        passwordTxt.setOpacity(opacity);
        reEnterTxt.setOpacity(opacity);
        usrLbl.setOpacity(opacity);
        passwordLbl.setOpacity(opacity);
        reEnterLbl.setOpacity(opacity);
    }


    public void addBtnClickOnAction(ActionEvent actionEvent) {

        boolean isUserRequired = usrCheckBox.isSelected();
        boolean isUserFilled = !usernameTxt.getText().isEmpty() && !passwordTxt.getText().isEmpty() && !reEnterTxt.getText().isEmpty();




        if (isUserRequired && isUserFilled) {
            try {
                String username = usernameTxt.getText();
                String password = passwordTxt.getText();
                UserRepo.save(new User(username, password));
                String userId = UserRepo.getIdByUsername(username);

                saveEmployee(userId);
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {
            saveEmployee(null);
        }
    }

    private void saveEmployee(String  userId) {
        try {
            if (userId == null) {
                EmployeeRepo.save(new Employee(
                        employeeNameTxt.getText(),
                        positionTxt.getText(),
                        addressTxt.getText(),
                        contactNo.getText(),
                        Double.parseDouble(salaryTxt.getText()),
                        null
                ));
            } else {
                EmployeeRepo.save(new Employee(
                        employeeNameTxt.getText(),
                        positionTxt.getText(),
                        addressTxt.getText(),
                        contactNo.getText(),
                        Double.parseDouble(salaryTxt.getText()),
                        userId
                ));
            }

            observableList.clear();
            loadAllEmployees();
            loadAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onUsrMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = usrTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                selectedUser = usrTable.getItems().get(selectedIndex);
            }
        }
    }


    public void onEmMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            int selectedIndex = employeeTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                selectedEmployee = employeeTable.getItems().get(selectedIndex);
            }
        }
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

