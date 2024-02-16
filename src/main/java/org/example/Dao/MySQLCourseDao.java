package org.example.Dao;

import org.example.models.Course;
import org.example.util.DBUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCourseDao implements CourseDao {
    private final DBUtil dbUtil;
    public MySQLCourseDao() {
        try {
            dbUtil = new DBUtil();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course get(String id) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from courses where id = '" + id + "';");

            if (resultSet.next()) return new Course(id, resultSet.getString("title"), resultSet.getString("instructorId"));

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getAll() {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from courses;");
            List<Course> courses = new ArrayList<>();

            while (resultSet.next())
                courses.add(new Course(resultSet.getString("id"), resultSet.getString("title"), resultSet.getString("instructorId")));

            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String saveSql = "insert into courses values (?, ?, ?);";

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
    public void update(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String id, int updateOption) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String setQuery = switch (updateOption) {
                case 1 -> "id = ?";
                case 2 -> "title = ?";
                case 3 -> "instructorId = ?";
                default -> null;
            };

            String updateSql = "update courses set " + setQuery + " where id = ?;";

            PreparedStatement updatePreparedStatement = connection.prepareCall(updateSql);
            updatePreparedStatement.setString(1, dataInputStream.readUTF());
            updatePreparedStatement.setString(2, id);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String deleteSql = "delete from courses where id = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, id);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
