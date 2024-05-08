package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Prescription;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PrescSaveRepo {
    public static boolean addPrescriptionWithTestIds(Prescription newPresc, List<String> testIds) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isPrescriptionAdded = PrescriptionRepo.add(connection, newPresc);
            if (!isPrescriptionAdded) {
                connection.rollback();
                return false;
            }

            for (String testId : testIds) {
                boolean isTestAdded = PrescTestDetailRepo.addTestToPrescription(connection, newPresc.getPrescriptionId(), testId);
                if (!isTestAdded) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }
}
