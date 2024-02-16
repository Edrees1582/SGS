package org.example.Dao;

import org.example.system_interfaces.AdminInterface;
import org.example.system_interfaces.InstructorInterface;
import org.example.system_interfaces.StudentInterface;
import org.example.users.*;
import org.example.util.DBUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDao implements UserDao<User> {
    private final DBUtil dbUtil;
    public MySQLUserDao() {
        try {
            dbUtil = new DBUtil();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(String id, UserType userType) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            if (userType.equals(UserType.ADMIN)) {
                resultSet = statement.executeQuery("select * from admins where ID = '" + id + "';");
                if (resultSet.next()) return new User(id, resultSet.getString("password"), resultSet.getString("name"), userType, new AdminInterface());
            }
            else if (userType.equals(UserType.INSTRUCTOR)) {
                resultSet = statement.executeQuery("select * from instructors where ID = '" + id + "';");
                if (resultSet.next()) return new User(id, resultSet.getString("password"), resultSet.getString("name"), userType, new InstructorInterface());
            }
            else if (userType.equals(UserType.STUDENT)) {
                resultSet = statement.executeQuery("select * from students where ID = '" + id + "';");
                if (resultSet.next()) return new User(id, resultSet.getString("password"), resultSet.getString("name"), userType, new StudentInterface());
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll(UserType userType) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            List<User> users = new ArrayList<>();

            if (userType.equals(UserType.ADMIN)) {
                resultSet = statement.executeQuery("select * from admins;");
                while (resultSet.next())
                    users.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), userType, new AdminInterface()));
            }
            else if (userType.equals(UserType.INSTRUCTOR)) {
                resultSet = statement.executeQuery("select * from instructors;");
                while (resultSet.next())
                    users.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), userType, new InstructorInterface()));
            }
            else if (userType.equals(UserType.STUDENT)) {
                resultSet = statement.executeQuery("select * from students;");
                while (resultSet.next())
                    users.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), userType, new StudentInterface()));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(DataInputStream dataInputStream, DataOutputStream dataOutputStream, UserType userType) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String saveSql = switch (userType) {
                case ADMIN -> "insert into admins values (?, ?, ?);";
                case INSTRUCTOR -> "insert into instructors values (?, ?, ?);";
                case STUDENT -> "insert into students values (?, ?, ?);";
            };

            PreparedStatement preparedStatement = connection.prepareCall(saveSql);
            for (int i = 1; i <= 3; i++) {
                preparedStatement.setString(i, dataInputStream.readUTF());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String id, UserType userType, int updateOption) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String setQuery = switch (updateOption) {
                case 1 -> "id = ?";
                case 2 -> "password = ?";
                case 3 -> "name = ?";
                default -> null;
            };

            String updateSql = switch (userType) {
                case ADMIN -> "update admins set " + setQuery + " where id = ?;";
                case INSTRUCTOR -> "update instructors set " + setQuery + " where id = ?;";
                case STUDENT -> "update students set " + setQuery + " where id = ?;";
            };

            PreparedStatement updatePreparedStatement = connection.prepareCall(updateSql);
            updatePreparedStatement.setString(1, dataInputStream.readUTF());
            updatePreparedStatement.setString(2, id);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id, UserType userType) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String deleteSql = switch (userType) {
                case ADMIN -> "delete from admins where id = ?;";
                case INSTRUCTOR -> "delete from instructors where id = ?;";
                case STUDENT -> "delete from students where id = ?;";
            };
            
            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, id);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
