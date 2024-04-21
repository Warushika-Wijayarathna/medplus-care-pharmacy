package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Supplier;

import java.sql.PreparedStatement;
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
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int contact = resultSet.getInt(3);
            String email = resultSet.getString(4);

            Supplier supplier = new Supplier(id, name, contact, email);
            supplierList.add(supplier);
        }
        return supplierList;
    }

    public static void update(Supplier updatedSupplier) throws SQLException {
        String sql = "UPDATE Supplier SET name = ?, contact = ?, email = ? WHERE sp_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, updatedSupplier.getName());
            preparedStatement.setInt(2, updatedSupplier.getContact());
            preparedStatement.setString(3, updatedSupplier.getEmail());
            preparedStatement.setInt(4, updatedSupplier.getSupplierId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Supplier updated successfully.");
            } else {
                System.out.println("Failed to update supplier.");
            }
        }
    }

    public static void add(Supplier newSupplier) throws SQLException {
        String sql = "INSERT INTO Supplier (name, contact, email) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, newSupplier.getName());
            preparedStatement.setInt(2, newSupplier.getContact());
            preparedStatement.setString(3, newSupplier.getEmail());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Supplier added successfully.");
            } else {
                System.out.println("Failed to add supplier.");
            }
        }
    }

    public static void delete(Supplier supplier) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE sp_id = ?";

        try (PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, supplier.getSupplierId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Supplier deleted successfully.");
            } else {
                System.out.println("Failed to delete supplier.");
            }
        }
    }

}
