package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Prescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepo {
    public static List<Prescription> getAll() throws SQLException {
        String sql = "SELECT * FROM Prescription";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Prescription> prescriptionList = new ArrayList<>();

        while (resultSet.next()){
            String prescriptionId = resultSet.getString(1);
            String customerId = resultSet.getString(2);
            String patientName = resultSet.getString(3);
            int age = resultSet.getInt(4);
            String medicalOfficerName = resultSet.getString(5);
            String context = resultSet.getString(6);
            String duration = resultSet.getString(7);
            LocalDate date = LocalDate.parse(resultSet.getString(8));

            Prescription prescription = new Prescription(prescriptionId, customerId, patientName, age, medicalOfficerName, context, duration, date);
            prescriptionList.add(prescription);
        }
        return prescriptionList;
    }

    public static boolean delete(Prescription prescription) throws SQLException {
        String sql = "DELETE FROM Prescription WHERE presc_id = ?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, prescription.getPrescriptionId());
        preparedStatement.executeUpdate();
        return true;
    }

    public static boolean update(Prescription updatedPresc) {
        try {
            String sql = "UPDATE Prescription SET cust_id = ?, patient_name = ?, age = ?, medical_officer_name = ?, context = ?, duration = ?, date = ? WHERE presc_id = ?";
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);

            String custId= CustomerRepo.generateCustomerIdByName(updatedPresc.getCustomerId());

            preparedStatement.setString(1, custId);
            preparedStatement.setString(2, updatedPresc.getPatientName());
            preparedStatement.setInt(3, updatedPresc.getAge());
            preparedStatement.setString(4, updatedPresc.getMedicalOfficerName());
            preparedStatement.setString(5, updatedPresc.getContext());
            preparedStatement.setString(6, updatedPresc.getDuration());
            preparedStatement.setString(7, updatedPresc.getDate().toString());
            preparedStatement.setString(8, updatedPresc.getPrescriptionId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generatePrescriptionId() throws SQLException {
        String getLastPrescriptionId = null;

        String sql = "SELECT CONCAT('PR' , LPAD(next_id, 3, '0')) FROM AutoIncrement_Prescription";

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            getLastPrescriptionId = resultSet.getString(1);
        }
        return getLastPrescriptionId;
    }

    public static boolean add(Connection connection, Prescription newPresc) throws SQLException {
        try {
            String sql = "INSERT INTO Prescription VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            String custId= CustomerRepo.generateCustomerIdByName(newPresc.getCustomerId());

            preparedStatement.setString(1, newPresc.getPrescriptionId());
            preparedStatement.setString(2, custId);
            preparedStatement.setString(3, newPresc.getPatientName());
            preparedStatement.setInt(4, newPresc.getAge());
            preparedStatement.setString(5, newPresc.getMedicalOfficerName());
            preparedStatement.setString(6, newPresc.getContext());
            preparedStatement.setString(7, newPresc.getDuration());
            preparedStatement.setString(8, newPresc.getDate().toString());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
