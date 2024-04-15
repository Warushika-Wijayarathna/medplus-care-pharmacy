package lk.ijse.medpluscarepharmacy.repository;

import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Employee;
import lk.ijse.medpluscarepharmacy.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    public static boolean check(String username, String password) throws SQLException {
        String sql = "SELECT password FROM User WHERE usr_name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String dbPassword = resultSet.getString("password");
            return dbPassword.equals(password);
        } else {
            return false;
        }
    }

    public static List<User> getAll() throws SQLException {
        String sql = "SELECT * FROM User";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery(sql);
        List<User> employeeList = new ArrayList<>();

        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String password = resultSet.getString(3);

            User user = new User(id,name,password);
            employeeList.add(user);
        }
        return employeeList;
    }

    public static void save(User user) throws SQLException {
        String sql = "INSERT INTO User (usr_name, password) VALUES (?, ?)";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();
    }

    public static int getIdByUsername(String username) throws SQLException {
        String sql = "SELECT usr_id FROM User WHERE usr_name = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("usr_id");
        } else {
            return -1;
        }
    }

    public static void update(User user) throws SQLException {
        String sql = "UPDATE User SET usr_name = ?, password = ? WHERE usr_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setObject(3, user.getUserId());
        preparedStatement.executeUpdate();
    }

    public static void delete(int userId) throws SQLException {
        String sql = "DELETE FROM User WHERE usr_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, userId);
        preparedStatement.executeUpdate();
    }
}
