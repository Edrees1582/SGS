package org.example.dao;

import org.example.models.Grade;
import org.example.util.DBUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.*;

public class MySQLGradeDao implements GradeDao {
    private final DBUtil dbUtil;
    public MySQLGradeDao() {
        try {
            dbUtil = new DBUtil();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Grade get(String courseId, String studentId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from grades where courseId = '" + courseId + "' and studentId = '" + studentId + "';");

            if (resultSet.next()) return new Grade(courseId, studentId, resultSet.getString("grade"));

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(DataInputStream dataInputStream) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String saveSql = "insert into grades values (?, ?, ?);";

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
    public void update(DataInputStream dataInputStream, String courseId, String studentId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String updateSql = "update grades set grade = ? where courseId = ? and studentId = ?;";

            PreparedStatement updatePreparedStatement = connection.prepareCall(updateSql);
            updatePreparedStatement.setString(1, dataInputStream.readUTF());
            updatePreparedStatement.setString(2, courseId);
            updatePreparedStatement.setString(3, studentId);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String courseId, String studentId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String deleteSql = "delete from grades where courseId = ? and studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, courseId);
            deletePreparedStatement.setString(2, studentId);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
