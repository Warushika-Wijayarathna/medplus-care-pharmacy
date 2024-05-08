package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemCartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderRepo {
    public static boolean checkOut(List<ItemCartTm> cartItems, int contact, String totalLblText, String admin, String dateLblText) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved = OrderRepo.saveOrder(contact, totalLblText, admin, dateLblText);
            if (!isOrderSaved) {
                connection.rollback();
                return false;
            }

            String orderId = OrderRepo.generateOrderId();
            if (orderId == null) {
                connection.rollback();
                return false;
            }

            List<ItemCartTm> testItems = new ArrayList<>();
            List<ItemCartTm> nonTestItems = new ArrayList<>();

            for (ItemCartTm item : cartItems) {
                if (item.getItemId().startsWith("T")) {
                    testItems.add(item);
                } else {
                    nonTestItems.add(item);
                }
            }

            for (ItemCartTm item : nonTestItems) {
                boolean isOrderDetailSaved = OrderItemDetailRepo.saveOrderDetail(item.getItemId(), item.getQty(), orderId);
                if (!isOrderDetailSaved) {
                    connection.rollback();
                    return false;
                }
            }

            for (ItemCartTm item : testItems) {
                boolean isTestOrderDetailSaved = OrderTestDetailRepo.saveOrderDetail(item.getItemId(), item.getQty(), orderId);
                if (!isTestOrderDetailSaved) {
                    connection.rollback();
                    return false;
                }
            }

            for (ItemCartTm item : nonTestItems) {
                boolean isStockUpdated = ItemRepo.updateStock(item.getItemId(), item.getQty());
                if (!isStockUpdated) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}
