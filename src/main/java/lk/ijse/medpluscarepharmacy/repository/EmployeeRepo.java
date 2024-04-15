package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM Employee";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery(sql);
        List<Employee> employeeList = new ArrayList<>();

        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String position = resultSet.getString(3);
            String address = resultSet.getString(4);
            String contactNo = resultSet.getString(5);
            double salary = resultSet.getDouble(6);
            int user = resultSet.getInt(7);

            Employee employee = new Employee(id,name,position,address,contactNo,salary,user);
            employeeList.add(employee);
        }
        return employeeList;
    }

    public static void save(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (name, position, address, contact_no, salary, usr_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getPosition());
        preparedStatement.setString(3, employee.getAddress());
        preparedStatement.setString(4, employee.getContactNo());
        preparedStatement.setDouble(5, employee.getSalary());
        preparedStatement.setObject(6, employee.getUserId());
        preparedStatement.executeUpdate();
    }

    public static void delete(int employeeId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE emp_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, employeeId);
        preparedStatement.executeUpdate();
    }

    public static void update(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET name = ?, position = ?, address = ?, contact_no = ?, salary = ?, usr_id = ? WHERE emp_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getPosition());
        preparedStatement.setString(3, employee.getAddress());
        preparedStatement.setString(4, employee.getContactNo());
        preparedStatement.setDouble(5, employee.getSalary());
        preparedStatement.setObject(6, employee.getUserId());
        preparedStatement.setObject(7, employee.getEmployeeId());
        preparedStatement.executeUpdate();
    }

}
