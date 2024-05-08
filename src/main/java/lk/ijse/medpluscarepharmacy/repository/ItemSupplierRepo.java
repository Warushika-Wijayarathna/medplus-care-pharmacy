package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemSupplierRepo {
    public static boolean saveItem(Item newItem, List<String> allSupplierIds) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isItemSaved = ItemRepo.add(connection, newItem);
            if (!isItemSaved) {
                connection.rollback();
                return false;
            }

            for (String supplierId : allSupplierIds) {
                boolean isItemSupplierDetailSaved = saveItemSupplierDetail(connection, newItem.getItemId(), supplierId);
                if (!isItemSupplierDetailSaved) {
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
            throw new RuntimeException(e);
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


    private static boolean saveItemSupplierDetail(Connection connection, String itemId, String supplierId) throws SQLException {
        try {
            String sql = "INSERT INTO item_supplier_detail (item_id, sp_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, itemId);
            preparedStatement.setString(2, supplierId);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> getSupplierIdsByItemId(String itemId) throws SQLException {
        try {
            String sql = "SELECT sp_id FROM item_supplier_detail WHERE item_id = ?";
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, itemId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> suppliers = new ArrayList<>();
            while (resultSet.next()) {
                String supplierId = resultSet.getString("sp_id");
                suppliers.add(supplierId);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updateItem(Item updatedItem, List<String> allSupplierIds) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            ItemRepo.update(updatedItem);

            deleteItemSupplierDetails(connection, updatedItem.getItemId());

            for (String supplierId : allSupplierIds) {
                saveItemSupplierDetail(connection, updatedItem.getItemId(), supplierId);
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

    private static void deleteItemSupplierDetails(Connection connection,String itemId) throws SQLException {
        String sql = "DELETE FROM item_supplier_detail WHERE item_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, itemId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }


    public static boolean deleteItem(Item item) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            deleteItemSupplierDetails(connection,item.getItemId());
            ItemRepo.delete(item);


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
