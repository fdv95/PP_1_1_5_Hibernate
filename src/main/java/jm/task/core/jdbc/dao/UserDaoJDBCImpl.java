package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    final String INSERT_NEW = "INSERT INTO user (name, lastName, age) VALUES(?,?,?)"; // Внесение значений в таблицу
    private static final String GET_ALL = "SELECT * FROM user"; // Получение всех значений из таблицы
    private static final String DELETE = "DELETE FROM user WHERE id=?"; // Удаление значений по id
    private static final String DELETE_ALL = "DELETE FROM user"; // Удаление всех значений из таблицы


    Connection connection = Util.getConnection();
    PreparedStatement preparedStatement = null;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table if not exists user (id bigint auto_increment, name varchar(45) not null," +
                    "lastName varchar(45) not null, age tinyint not null, constraint user_pk primary key (id))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table if exists user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW);
//            preparedStatement.setLong(1,);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        User user = new User();
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                User user = new User();
                user.setId(res.getLong(1));
                user.setName(res.getString(2));
                user.setLastName(res.getString(3));
                user.setAge(res.getByte(4));
                usersList.add(user);
            }
            return usersList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try {
            preparedStatement = connection.prepareStatement(DELETE_ALL);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
