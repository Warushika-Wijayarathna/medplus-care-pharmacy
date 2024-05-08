package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Prescription;
import lk.ijse.medpluscarepharmacy.model.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class PrescTestDetailRepo {
    public static List<Test> getTestsByPrescriptionId(String prescId) throws SQLException {
        List<String> testIds = getTestIdsByPrescriptionId(prescId);
        List<Test> tests = new ArrayList<>();

        for (String testId : testIds) {
            Test test = TestRepo.getTestById(testId);
            if (test != null) {
                tests.add(test);
            }
        }

        return tests;
    }

    private static List<String> getTestIdsByPrescriptionId(String prescId) throws SQLException {
        List<String> testIds = new ArrayList<>();

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT test_id FROM presc_test_detail WHERE presc_id = ?");


        preparedStatement.setString(1, prescId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            testIds.add(resultSet.getString("test_id"));
        }

        resultSet.close();
        preparedStatement.close();

        return testIds;
    }

    public static boolean addTestToPrescription(Connection connection, String prescriptionId, String testId) throws SQLException {
        String sql = "INSERT INTO presc_test_detail (presc_id, test_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, prescriptionId);
        preparedStatement.setString(2, testId);


        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    public static boolean updatePrescription(Prescription updatedPresc, List<String> allTestIds) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isPrescriptionUpdated = PrescriptionRepo.update(updatedPresc);
            if (!isPrescriptionUpdated) {
                connection.rollback();
                return false;
            }

            boolean isOldDetailsDeleted = deletePrescTestDetails(connection, updatedPresc.getPrescriptionId());
            if (!isOldDetailsDeleted) {
                connection.rollback();
                return false;
            }

            for (String testId : allTestIds) {
                boolean isTestAdded = addTestToPrescription(connection, updatedPresc.getPrescriptionId(), testId);
                if (!isTestAdded) {
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

    private static boolean deletePrescTestDetails(Connection connection, String prescriptionId) throws SQLException {
        try {
            String sql = "DELETE FROM presc_test_detail WHERE presc_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, prescriptionId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deletePresc(Prescription prescription) {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isDetailsDeleted = deletePrescTestDetails(connection, prescription.getPrescriptionId());
            if (!isDetailsDeleted) {
                connection.rollback();
                return false;
            }

            boolean isPrescriptionDeleted = PrescriptionRepo.delete(prescription);
            if (!isPrescriptionDeleted) {
                connection.rollback();
                return false;
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
