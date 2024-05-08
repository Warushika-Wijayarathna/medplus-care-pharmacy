package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.TemperatureReading;

import java.sql.*;

public class TemperatureRepo {

    public static void addTemperature(TemperatureReading temperatureReading) throws SQLException {
        System.out.println(temperatureReading.getDate() + " " + temperatureReading.getTimestamp() + " " + temperatureReading.getTemperature());
        String sql = "INSERT INTO Temperature (date, time, temperature) VALUES (?, ?, ?)";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setDate(1, Date.valueOf(temperatureReading.getDate()));
            pstm.setTime(2, Time.valueOf(temperatureReading.getTimestamp()));
            pstm.setDouble(3, temperatureReading.getTemperature());
            pstm.executeUpdate();
    }
}
