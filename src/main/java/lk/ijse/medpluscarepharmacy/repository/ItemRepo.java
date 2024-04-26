package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemRepo {
    public static List<Item> getAll() throws SQLException {
        String sql = "SELECT * FROM Item";

        PreparedStatement preparedStatement= DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Item> itemList = new ArrayList<>();

        while (resultSet.next()){
            String id = resultSet.getString(1);
            String desc = resultSet.getString(2);
            double price = resultSet.getDouble(5);
            double discount = resultSet.getDouble(6);

            Item item = new Item(id, desc, price, discount);
            itemList.add(item);
        }
        return itemList;
    }

    public static void delete(Item item) throws SQLException {
        String sql = "DELETE FROM Item WHERE item_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, item.getItemId());

        preparedStatement.executeUpdate();
    }

    public static void update(Item updatedItem) throws SQLException {
        String sql = "UPDATE Item SET description = ?, whole_sale_price = ?, retail_price = ?, discount = ? WHERE item_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, updatedItem.getDescription());
        preparedStatement.setDouble(2, updatedItem.getWholeSalePrice());
        preparedStatement.setDouble(3, updatedItem.getRetailPrice());
        preparedStatement.setDouble(4, updatedItem.getDiscount());
        preparedStatement.setString(5, updatedItem.getItemId());

        preparedStatement.executeUpdate();
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
}
