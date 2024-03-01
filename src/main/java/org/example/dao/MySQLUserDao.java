package org.example.dao;

import org.example.models.Course;
import org.example.system_interfaces.AdminInterface;
import org.example.system_interfaces.InstructorInterface;
import org.example.system_interfaces.StudentInterface;
import org.example.system_interfaces.UserInterface;
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
    public User get(String id) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where ID = '" + id + "';");

            if (resultSet.next()) {
                UserType userType = switch (resultSet.getString("userType")) {
                    case "admin" -> UserType.ADMIN;
                    case "instructor" -> UserType.INSTRUCTOR;
                    case "student" -> UserType.STUDENT;
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };

                UserInterface userInterface = switch (resultSet.getString("userType")) {
                    case "admin" -> new AdminInterface();
                    case "instructor" -> new InstructorInterface();
                    case "student" -> new StudentInterface();
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };

                return new User(id, resultSet.getString("password"), resultSet.getString("name"), userType, userInterface);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users;");
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                UserType userType = switch (resultSet.getString("userType")) {
                    case "admin" -> UserType.ADMIN;
                    case "instructor" -> UserType.INSTRUCTOR;
                    case "student" -> UserType.STUDENT;
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };

                UserInterface userInterface = switch (resultSet.getString("userType")) {
                    case "admin" -> new AdminInterface();
                    case "instructor" -> new InstructorInterface();
                    case "student" -> new StudentInterface();
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };

                users.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), userType, userInterface));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getStudents() {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userType = 'student';");
            List<User> students = new ArrayList<>();

            while (resultSet.next())
                students.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), UserType.STUDENT, new StudentInterface()));

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getInstructors() {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where userType = 'instructor';");
            List<User> instructors = new ArrayList<>();

            while (resultSet.next())
                instructors.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), UserType.INSTRUCTOR, new InstructorInterface()));

            return instructors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(DataInputStream dataInputStream, DataOutputStream dataOutputStream, UserType userType) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String saveSql = switch (userType) {
                case ADMIN -> "insert into users values (?, ?, ?, 'admin');";
                case INSTRUCTOR -> "insert into users values (?, ?, ?, 'instructor');";
                case STUDENT -> "insert into users values (?, ?, ?, 'student');";
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
    public void update(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String id, int updateOption) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            String setQuery = switch (updateOption) {
                case 1 -> "id = ?";
                case 2 -> "password = ?";
                case 3 -> "name = ?";
                default -> null;
            };

            String updateSql = "update users set " + setQuery + " where id = ?;";

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
            String deleteSql = "delete from users where id = ?;";
            User user = get(id);

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, id);

            if (user.getUserType() == UserType.STUDENT) {
                MySQLEnrollmentDao mySQLEnrollmentDao = new MySQLEnrollmentDao();
                List<Course> courses = mySQLEnrollmentDao.getStudentCourses(id);
                for (Course course : courses) mySQLEnrollmentDao.delete(course.getId(), id);
            }
            else if (user.getUserType() == UserType.INSTRUCTOR) {
                MySQLCourseDao mySQLCourseDao = new MySQLCourseDao();
                List<Course> courses = mySQLCourseDao.getAllByInstructorId(id);
                for (Course course : courses) mySQLCourseDao.delete(course.getId());
            }

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
