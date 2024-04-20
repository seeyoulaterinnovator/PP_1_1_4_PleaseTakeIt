package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }
    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS USERS (" +
                        "id BIGINT NOT NULL AUTO_INCREMENT," +
                        "name VARCHAR(64)," +
                        "lastName VARCHAR(64)," +
                        "age SMALLINT," +
                        "PRIMARY KEY ( id ) )"
                );
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate("DROP TABLE IF EXISTS users");
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement prepStat = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES(?, ?, ?)")) {
            try {
                prepStat.setString(1, name);
                prepStat.setString(2, lastName);
                prepStat.setByte(3, age);
                prepStat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement prepStat = connection.prepareStatement("DELETE FROM users where id = ?")) {
            try {
                prepStat.setLong(1, id);
                prepStat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getAllUsers() {
        List<User> userList = new LinkedList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate("TRUNCATE users");
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
