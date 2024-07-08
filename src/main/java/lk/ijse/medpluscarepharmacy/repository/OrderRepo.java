package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

public class OrderRepo {
    public static boolean saveOrder(int contact, String totalLblText, String admin, String dateLblText) throws SQLException {
        String customerId = CustomerRepo.getCustomerId(contact);
        String orderId = generateOrderId();
        String userId = UserRepo.getUserId(admin);

        String sql = "INSERT INTO `Order` VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, orderId);
        preparedStatement.setString(2, totalLblText);
        preparedStatement.setString(3, customerId);
        preparedStatement.setString(4, userId);
        preparedStatement.setString(5, dateLblText);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;

        }
    }

    public static String generateOrderId() throws SQLException {
        String generatedId = null;
        String sql = "SELECT CONCAT('O', LPAD(next_id, 4, '0')) FROM AutoIncrement_Order";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            generatedId = resultSet.getString(1);
        }

        resultSet.close();
        preparedStatement.close();

        return generatedId;
    }

    public static String getOrderId() {
        String getLastOrderId = null;
        try {
            String sql = "SELECT o_id FROM `Order` ORDER BY o_id DESC LIMIT 1";
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                getLastOrderId = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getLastOrderId;
    }

    public static String getDailySales(LocalDate today) {
        Double total = 0.0;
        String sql = "SELECT SUM(total) FROM `Order` WHERE date = ?";
        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, today.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.valueOf(total);

    }

    public static String getMonthlySales(Month month, int year) {
        Double total = 0.0;
        String sql = "SELECT SUM(total) FROM `Order` WHERE MONTH(date) = ? AND YEAR(date) = ?";
        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, month.getValue());
            preparedStatement.setInt(2, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.valueOf(total);
    }

    public static String getAnnualSales(int year) {
        Double total = 0.0;
        String sql = "SELECT SUM(total) FROM `Order` WHERE YEAR(date) = ?";
        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.valueOf(total);
    }

    public static String getProfit(LocalDate today) {
        Double totalProfit = 0.0;
        String sql = "SELECT daily_profit FROM DailyProfitView WHERE sale_date = ?";
        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, today.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalProfit = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.valueOf(totalProfit);
    }
}
