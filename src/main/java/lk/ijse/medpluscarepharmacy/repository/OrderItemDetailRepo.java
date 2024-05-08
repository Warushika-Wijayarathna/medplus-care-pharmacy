package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDetailRepo {
    public static boolean  saveOrderDetail(String itemId, int qty, String orderId) throws SQLException {
        String sql = "INSERT INTO order_item_detail VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, orderId);
        preparedStatement.setString(2, itemId);
        preparedStatement.setInt(3, qty);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }
}
