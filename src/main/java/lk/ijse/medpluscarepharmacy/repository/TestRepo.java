package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestRepo {
    public static List<Test> getAll() throws SQLException {
        String sql = "SELECT * FROM Test";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Test> testList = new ArrayList<>();

            while (resultSet.next()) {
                String id = resultSet.getString("test_id");
                String description = resultSet.getString("description");
                String lab = resultSet.getString("lab");
                String sampleType = resultSet.getString("sample_type");
                String testType = resultSet.getString("test_type");
                double price = resultSet.getDouble("price");

                Test test = new Test(id, description, lab, sampleType, testType, price);
                testList.add(test);
            }
            return testList;

    }

    public static void update(Test updatedTest) throws SQLException {
        String sql = "UPDATE Test SET description = ?, lab = ?, sample_type = ?, test_type = ?, price = ? WHERE test_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, updatedTest.getDescription());
            preparedStatement.setString(2, updatedTest.getLab());
            preparedStatement.setString(3, updatedTest.getSampleType());
            preparedStatement.setString(4, updatedTest.getTestType());
            preparedStatement.setDouble(5, updatedTest.getPrice());
            preparedStatement.setString(6, updatedTest.getTestId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test updated successfully.");
            } else {
                System.out.println("Failed to update test.");
            }
    }

    public static void add(Test newTest) throws SQLException {
        String sql = "INSERT INTO Test (description, lab, sample_type, test_type, price) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, newTest.getDescription());
            preparedStatement.setString(2, newTest.getLab());
            preparedStatement.setString(3, newTest.getSampleType());
            preparedStatement.setString(4, newTest.getTestType());
            preparedStatement.setDouble(5, newTest.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test added successfully.");
            } else {
                System.out.println("Failed to add test.");
            }

    }

    public static void delete(Test test) throws SQLException {
        try {
            String sql = "DELETE FROM Test WHERE test_id = ?";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, test.getTestId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test deleted successfully.");
            } else {
                System.out.println("Failed to delete test.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static String generateTestId(Test newTest) throws SQLException {
        String generatedTestId = null;
        String sql = "SELECT CONCAT('T', LPAD(next_id, 4, '0')) FROM AutoIncrement_Test";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    generatedTestId = resultSet.getString(1);
                }
        return generatedTestId;
    }

    public static List<String> getAllTestNames() throws SQLException {
        List<String> testNames = new ArrayList<>();
        String query = "SELECT test_id FROM Test";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("test_id");
            testNames.add(name);
        }
        resultSet.close();
        statement.close();

        return testNames;
    }

    public static List<String> getAllTestsNames() throws SQLException {
        try {
            List<String> testNames = new ArrayList<>();
            String query = "SELECT description FROM Test";

            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("description");
                testNames.add(name);
            }
            resultSet.close();
            statement.close();

            return testNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Test getTestById(String testId) throws SQLException {
        String sql = "SELECT * FROM Test WHERE test_id = ?";
        Test test = null;

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, testId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String testName = resultSet.getString("description");
            test = new Test(testId, testName);
        }

        resultSet.close();
        preparedStatement.close();

        return test;
    }

    public static Test getTestByDescription(String selectedTest) throws SQLException {

            String sql = "SELECT * FROM Test WHERE description = ?";
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedTest);
            ResultSet resultSet = statement.executeQuery();

            Test test = null;
            if (resultSet.next()) {
                String testId = resultSet.getString("test_id");
                String description = resultSet.getString("description");
                String lab = resultSet.getString("lab");
                String sampleType = resultSet.getString("sample_type");
                String testType = resultSet.getString("test_type");
                double price = resultSet.getDouble("price");

                test = new Test(testId, description, lab, sampleType, testType, price);
            }

            resultSet.close();
            statement.close();

            return test;

    }
}
