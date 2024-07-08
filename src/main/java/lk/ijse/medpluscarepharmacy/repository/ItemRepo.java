package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Customer;
import lk.ijse.medpluscarepharmacy.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemRepo {
    public static List<Item> getAll() throws SQLException {
        String sql = "SELECT test_id AS id, description, price, discount FROM Test " +
                "UNION ALL " +
                "SELECT item_id AS id, description, retail_price AS price, discount FROM Item";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Item> itemList = new ArrayList<>();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String desc = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                double discount = resultSet.getDouble("discount");

                Item item = new Item(id, desc, price, discount);
                itemList.add(item);
            }
            return itemList;
        }
    }


    public static void delete(Item item) throws SQLException {
        String sql = "DELETE FROM Item WHERE item_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, item.getItemId());

        preparedStatement.executeUpdate();
    }

    public static void update(Item updatedItem) throws SQLException {
        try {
            String sql = "UPDATE Item SET description = ?, qty = ?, whole_sale_price = ?, retail_price = ?, discount = ? WHERE item_id = ?";

            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, updatedItem.getDescription());
            preparedStatement.setInt(2, updatedItem.getQty());
            preparedStatement.setDouble(3, updatedItem.getWholeSalePrice());
            preparedStatement.setDouble(4, updatedItem.getRetailPrice());
            preparedStatement.setDouble(5, updatedItem.getDiscount());
            preparedStatement.setString(6, updatedItem.getItemId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Item> getAllItem() throws SQLException {
        String sql = "SELECT * FROM Item";

        PreparedStatement preparedStatement= DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Item> itemList = new ArrayList<>();

        while (resultSet.next()){
            String id = resultSet.getString(1);
            String desc = resultSet.getString(2);
            int qty = resultSet.getInt(3);
            double wholeSalePrice = resultSet.getDouble(4);
            double retailPrice = resultSet.getDouble(5);
            double discount = resultSet.getDouble(6);
            LocalDate expDate = LocalDate.parse(resultSet.getString(7));

            Item item = new Item(id, desc, qty, wholeSalePrice, retailPrice, discount,expDate);
            itemList.add(item);
        }
        return itemList;
    }

    public static String generateItemId(Item item) throws SQLException {
        String generatedItemId = null;
        String sql = "SELECT CONCAT('I', LPAD(next_id, 5, '0')) FROM AutoIncrement_Item";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            generatedItemId = resultSet.getString(1);
        }

        resultSet.close();
        statement.close();

        return generatedItemId;
    }

    public static boolean add(Connection connection, Item item) throws SQLException {
        String sql = "INSERT INTO Item VALUES (?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, item.getItemId());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setInt(3, item.getQty());
            preparedStatement.setDouble(4, item.getWholeSalePrice());
            preparedStatement.setDouble(5, item.getRetailPrice());
            preparedStatement.setDouble(6, item.getDiscount());
            preparedStatement.setString(7, item.getExpDate().toString());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;

    }

    public static boolean updateStock(String itemId, int qty) throws SQLException {
        String sql = "UPDATE Item SET qty = qty - ? WHERE item_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, qty);
        preparedStatement.setString(2, itemId);

        preparedStatement.executeUpdate();

        return true;
    }
}
