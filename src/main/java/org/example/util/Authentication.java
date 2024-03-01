package org.example.util;

import org.example.system_interfaces.AdminInterface;
import org.example.system_interfaces.InstructorInterface;
import org.example.system_interfaces.StudentInterface;
import org.example.users.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication {
    private final DBUtil dbUtil;

    public Authentication() {
        dbUtil = new DBUtil();
    }

    public User authenticateUser(String id, String password) {
        try (Connection connection = dbUtil.getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            User user = null;

            resultSet = statement.executeQuery("select * from users where ID = '" + id + "' AND password = '" + password + "';");

            if (resultSet.next()) {
                if (resultSet.getString("userType").equals("admin"))
                    user = new User(id, password, resultSet.getString("name"), UserType.ADMIN, new AdminInterface());
                else if (resultSet.getString("userType").equals("instructor"))
                    user = new User(id, password, resultSet.getString("name"), UserType.INSTRUCTOR, new InstructorInterface());
                else if (resultSet.getString("userType").equals("student"))
                    user = new User(id, password, resultSet.getString("name"), UserType.STUDENT, new StudentInterface());
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
