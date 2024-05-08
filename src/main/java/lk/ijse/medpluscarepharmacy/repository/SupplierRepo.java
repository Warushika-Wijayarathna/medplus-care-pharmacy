package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Supplier;
import lk.ijse.medpluscarepharmacy.model.Tm.SmallSupplierTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SupplierTm;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {
    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM Supplier";

        PreparedStatement preparedStatement= DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Supplier> supplierList = new ArrayList<>();

        while (resultSet.next()){
            String  id = resultSet.getString(1);
            String name = resultSet.getString(2);
            int contact = resultSet.getInt(3);
            String email = resultSet.getString(4);

            Supplier supplier = new Supplier(id, name, contact, email);
            supplierList.add(supplier);
        }
        resultSet.close();
        preparedStatement.close();
        return supplierList;
    }

    public static void update(Supplier updatedSupplier) throws SQLException {
        String sql = "UPDATE Supplier SET name = ?, contact = ?, email = ? WHERE sp_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, updatedSupplier.getName());
        preparedStatement.setInt(2, updatedSupplier.getContact());
        preparedStatement.setString(3, updatedSupplier.getEmail());
        preparedStatement.setString(4, updatedSupplier.getSupplierId());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Supplier updated successfully.");
        } else {
            System.out.println("Failed to update supplier.");
        }
        preparedStatement.close();
    }

    public static void add(Supplier newSupplier) throws SQLException {
        String sql = "INSERT INTO Supplier (name, contact, email) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, newSupplier.getName());
        preparedStatement.setInt(2, newSupplier.getContact());
        preparedStatement.setString(3, newSupplier.getEmail());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Supplier added successfully.");
        } else {
            System.out.println("Failed to add supplier.");
        }
        preparedStatement.close();
    }

    public static void delete(Supplier supplier) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE sp_id = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, supplier.getSupplierId());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Supplier deleted successfully.");
        } else {
            System.out.println("Failed to delete supplier.");
        }
        preparedStatement.close();
    }

    public static String generateSupplierId(Supplier newSupplier) throws SQLException {
        String generatedSupplierId = null;
        String sql = "SELECT CONCAT('S', LPAD(next_id, 4, '0')) FROM AutoIncrement_Supplier";

        PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            generatedSupplierId = resultSet.getString(1);
        }
        resultSet.close();
        statement.close();
        return generatedSupplierId;
    }

    public static List<String> getAllSupplierNames() throws SQLException {
        List<String> supplierNames = new ArrayList<>();
        String query = "SELECT name FROM Supplier";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            supplierNames.add(name);
        }
        resultSet.close();
        statement.close();
        return supplierNames;
    }


    public static SupplierTm getSupplierTmByName(String selectedSupplierName) throws SQLException {
        String sql = "SELECT * FROM Supplier WHERE name = ?";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, selectedSupplierName);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            int contact = resultSet.getInt(3);
            String email = resultSet.getString(4);

            SupplierTm supplierTm = new SupplierTm(id, name, contact, email, null);
            return supplierTm;
        }

        resultSet.close();
        preparedStatement.close();

        return null;
    }


    public static List<Supplier> getSupplierDetailsBySupplierId(List<String> suppliers) throws SQLException {
        try {
            String sql = "SELECT sp_id, name FROM Supplier WHERE sp_id = ?";
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            List<Supplier> suppliersList = new ArrayList<>();
            for (String supplierId : suppliers) {
                preparedStatement.setString(1, supplierId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String id = resultSet.getString("sp_id");
                    String name = resultSet.getString("name");
                    Supplier supplier = new Supplier(id, name);
                    suppliersList.add(supplier);
                }
            }
            return suppliersList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}