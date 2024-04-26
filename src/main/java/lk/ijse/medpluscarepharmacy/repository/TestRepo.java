package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestRepo {
    public static List<Test> getAll() throws SQLException {
        String sql = "SELECT * FROM Test";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
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
    }

    public static void update(Test updatedTest) throws SQLException {
        String sql = "UPDATE Test SET description = ?, lab = ?, sample_type = ?, test_type = ?, price = ? WHERE test_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
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
    }

    public static void add(Test newTest) throws SQLException {
        String sql = "INSERT INTO Test (description, lab, sample_type, test_type, price) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
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
    }

    public static void delete(Test test) throws SQLException {
        String sql = "DELETE FROM Test WHERE test_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, test.getTestId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Test deleted successfully.");
            } else {
                System.out.println("Failed to delete test.");
            }
        }
    }

    public static String generateTestId(Test newTest) throws SQLException {
        String generatedTestId = null;
        String sql = "SELECT CONCAT('T', LPAD(next_id, 4, '0')) FROM AutoIncrement_Test";

        try (PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    generatedTestId = resultSet.getString(1);
                }
            }
        }
        return generatedTestId;
    }

}
