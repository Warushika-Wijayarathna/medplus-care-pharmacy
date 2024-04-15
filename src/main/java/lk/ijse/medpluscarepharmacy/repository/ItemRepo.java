package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Item;
import lk.ijse.medpluscarepharmacy.model.Prescription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepo {
    public static List<Item> getAll() throws SQLException {
        String sql = "SELECT * FROM Item";

        PreparedStatement preparedStatement= DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Item> itemList = new ArrayList<>();

        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String desc = resultSet.getString(2);
            double price = resultSet.getDouble(5);
            double discount = resultSet.getDouble(6);

            Item item = new Item(id, desc, price, discount);
            itemList.add(item);
        }
        return itemList;
    }
}
