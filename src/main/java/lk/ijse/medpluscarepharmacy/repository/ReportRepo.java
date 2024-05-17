package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportRepo {
    public static List<Report> getAll() throws SQLException {
        String sql = "SELECT * FROM Report";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Report> reportList = new ArrayList<>();

        while (resultSet.next()) {
            String reportId = resultSet.getString(1);
            String custId = resultSet.getString(2);
            String testId = resultSet.getString(3);
            String result = resultSet.getString(4);
            LocalDate issueDate = LocalDate.parse(resultSet.getString(5));
            LocalDate pickupDate = LocalDate.parse(resultSet.getString(6));

            Report report = new Report(reportId, custId, testId, result, issueDate, pickupDate);
            reportList.add(report);
        }

        resultSet.close();
        preparedStatement.close();

        return reportList;
    }

    public static void delete(String reportId) throws SQLException {
        String sql = "DELETE FROM Report WHERE r_Id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, reportId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Report deleted Successfully.");
        } else {
            System.out.println("Failed to delete report.");
        }

        preparedStatement.close();
    }

    public static void update(Report updatedReport) throws SQLException {
        String sql = "UPDATE Report SET cust_id = ?, test_id = ?, result = ?, issue_date = ?, pickup_date = ? WHERE r_Id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,updatedReport.getCustId());
        preparedStatement.setString(2,updatedReport.getTestId());
        preparedStatement.setString(3,updatedReport.getResult());
        preparedStatement.setDate(4,java.sql.Date.valueOf(updatedReport.getIssueDate()));
        preparedStatement.setDate(5,java.sql.Date.valueOf(updatedReport.getPickupDate()));
        preparedStatement.setString(6,updatedReport.getReportId());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Report updated Successfully.");
        } else {
            System.out.println("Failed to update report.");
        }

        preparedStatement.close();

    }

    public static void add(Report newReport) throws SQLException {
        String sql = "INSERT INTO Report (cust_id, test_id, result, issue_date, pickup_date) VALUES (?, ?, ?, ?,?)";

        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, newReport.getCustId());
            preparedStatement.setString(2, newReport.getTestId());
            preparedStatement.setString(3, newReport.getResult());
            preparedStatement.setDate(4, java.sql.Date.valueOf(newReport.getIssueDate()));
            preparedStatement.setDate(5, java.sql.Date.valueOf(newReport.getPickupDate()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Report added successfully.");
            } else {
                System.out.println("Failed to add report.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public static String generateReportId (Report newReport) throws SQLException {
            String generatedReportId = null;

            String sql = "SELECT CONCAT('R', LPAD(next_id, 4, '0')) FROM AutoIncrement_Report";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                generatedReportId = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();

            return generatedReportId;
        }

    public static boolean checkInstant(String testId) throws SQLException {
        String sql = "SELECT test_type FROM Test WHERE test_id = ?";
        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, testId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String testType = resultSet.getString("test_type");
                    return "instant".equalsIgnoreCase(testType.trim());
                } else {
                    return false;
                }
            }
        }
    }


    public static String getNextId() {
        String sql = "SELECT CONCAT('R', LPAD(next_id, 4, '0')) FROM AutoIncrement_Report";
        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
