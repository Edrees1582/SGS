package org.example.dao;

import org.example.models.Course;
import org.example.models.Enrollment;
import org.example.models.Student;
import org.example.util.DBUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLEnrollmentDao implements EnrollmentDao {
    private final DBUtil dbUtil;
    public MySQLEnrollmentDao() {
        try {
            dbUtil = new DBUtil();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getStudentCourses(String studentId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select\n" +
                    "\tc.*" +
                    "from\n" +
                    "\tcourses c\n" +
                    "inner join enrollment e on e.courseId = c.id\n" +
                    "inner join users s on s.id = e.studentId\n" +
                    "where s.id = '" + studentId + "';");
            List<Course> courses = new ArrayList<>();

            while (resultSet.next())
                courses.add(new Course(resultSet.getString("id"), resultSet.getString("title"), resultSet.getString("instructorId")));

            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> getCourseStudents(String courseId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select\n" +
                    "\ts.*\n" +
                    "from\n" +
                    "\tusers s\n" +
                    "inner join enrollment e on e.studentId = s.id\n" +
                    "inner join courses c on c.id = e.courseId\n" +
                    "where c.id = '" + courseId + "';");
            List<Student> students = new ArrayList<>();

            while (resultSet.next())
                students.add(new Student(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name")));

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrollment> getAll() {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from enrollment;");
            List<Enrollment> enrollmentList = new ArrayList<>();

            while (resultSet.next())
                enrollmentList.add(new Enrollment(resultSet.getString("courseId"), resultSet.getString("studentId")));

            return enrollmentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(DataInputStream dataInputStream) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String saveSql = "insert into enrollment values (?, ?);";

            PreparedStatement preparedStatement = connection.prepareCall(saveSql);

            String courseId = dataInputStream.readUTF();
            String studentId = dataInputStream.readUTF();

            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, studentId);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String courseId, String studentId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String deleteSql = "delete from enrollment where courseId = ? and studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, courseId);
            deletePreparedStatement.setString(2, studentId);

            deletePreparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.delete(courseId, studentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByStudent(String studentId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String deleteSql = "delete from enrollment where studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, studentId);

            deletePreparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.deleteByStudent(studentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCourse(String courseId) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String deleteSql = "delete from enrollment where courseId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, courseId);

            deletePreparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.deleteByCourse(courseId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
