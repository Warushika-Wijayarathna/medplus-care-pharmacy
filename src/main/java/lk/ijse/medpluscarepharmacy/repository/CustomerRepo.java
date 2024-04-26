package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM Customer";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Customer> customerList = new ArrayList<>();

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                int contactNo = Integer.parseInt(resultSet.getString(3));
                String email = resultSet.getString(4);

                Customer customer = new Customer(id, name, contactNo, email);
                customerList.add(customer);
            }

            return customerList;
        }
    }

    public static void add(Customer newCustomer) throws SQLException {
        String sql = "INSERT INTO Customer (name, contact_no, email) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, newCustomer.getName());
            preparedStatement.setString(2, String.valueOf(newCustomer.getContactNo()));
            preparedStatement.setString(3, newCustomer.getEmail());


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer added successfully.");
            } else {
                System.out.println("Failed to add customer.");
            }
        }
    }

    public static void delete(Customer customer) throws SQLException {
        String sql = "DELETE FROM Customer WHERE cust_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getCustomerId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Failed to delete customer.");
            }
        }
    }

    public static void update(Customer updatedCustomer) throws SQLException {
        String sql = "UPDATE Customer SET name = ?, contact_no = ?, email = ? WHERE cust_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, updatedCustomer.getName());
            preparedStatement.setString(2, String.valueOf(updatedCustomer.getContactNo()));
            preparedStatement.setString(3, updatedCustomer.getEmail());
            preparedStatement.setString(4, updatedCustomer.getCustomerId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Failed to update customer.");
            }
        }
    }

    public static String generateCustomerId(Customer newCustomer) throws SQLException {
        String generatedCustomerId = null;
        String sql = "SELECT CONCAT('C', LPAD(next_id, 4, '0')) FROM AutoIncrement_Customer";

        try (PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    generatedCustomerId = resultSet.getString(1);
                }
            }
        }
        return generatedCustomerId;
    }

}
