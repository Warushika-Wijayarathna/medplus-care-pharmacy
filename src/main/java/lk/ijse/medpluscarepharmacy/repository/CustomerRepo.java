package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;

public class CustomerRepo {
    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM Customer";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Customer> customerList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            int contactNo = Integer.parseInt(resultSet.getString(3));
            String email = resultSet.getString(4);

            Customer customer = new Customer(id, name, contactNo, email);
            customerList.add(customer);
        }

        resultSet.close();
        preparedStatement.close();

        return customerList;
    }

    public static void add(Customer newCustomer) throws SQLException {
        String sql = "INSERT INTO Customer (name, contact_no, email) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, newCustomer.getName());
        preparedStatement.setString(2, String.valueOf(newCustomer.getContactNo()));
        preparedStatement.setString(3, newCustomer.getEmail());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Customer added successfully.");
        } else {
            System.out.println("Failed to add customer.");
        }

        preparedStatement.close();
    }

    public static void delete(Customer customer) throws SQLException {
        String sql = "DELETE FROM Customer WHERE cust_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, customer.getCustomerId());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Failed to delete customer.");
        }

        preparedStatement.close();
    }

    public static void update(Customer updatedCustomer) throws SQLException {
        String sql = "UPDATE Customer SET name = ?, contact_no = ?, email = ? WHERE cust_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
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

        preparedStatement.close();
    }

    public static String generateCustomerId(Customer newCustomer) throws SQLException {
        String generatedCustomerId = null;
        String sql = "SELECT CONCAT('C', LPAD(next_id, 4, '0')) FROM AutoIncrement_Customer";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            generatedCustomerId = resultSet.getString(1);
        }

        resultSet.close();
        statement.close();

        return generatedCustomerId;
    }

    public static Customer searchCustomerByContact(String contactNumber) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customer WHERE contact_no = ?");
        statement.setString(1, contactNumber);
        ResultSet resultSet = statement.executeQuery();

        Customer customer = null;
        if (resultSet.next()) {
            customer = new Customer();
            customer.setCustomerId(resultSet.getString("cust_id"));
            customer.setName(resultSet.getString("name"));
            customer.setContactNo(resultSet.getInt("contact_no"));
        }

        resultSet.close();
        statement.close();

        return customer;
    }

    public static String getCustomerId(int contact) throws SQLException {
        String getCustomerId = null;
        String sql = "SELECT cust_id FROM Customer WHERE contact_no = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, contact);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            getCustomerId = resultSet.getString(1);
        }

        resultSet.close();
        preparedStatement.close();

        return getCustomerId;
    }

    public static List<String> getAllCustNames() {
        try {
            List<String> custNames = new ArrayList<>();
            String query = "SELECT name FROM Customer";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                custNames.add(name);
            }
            resultSet.close();
            statement.close();

            return custNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateCustomerIdByName(String customerId) {
        try {
            String generatedCustomerId = null;
            String sql = "SELECT cust_id FROM Customer WHERE name = ?";

            PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                generatedCustomerId = resultSet.getString(1);
            }

            resultSet.close();
            statement.close();

            return generatedCustomerId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}