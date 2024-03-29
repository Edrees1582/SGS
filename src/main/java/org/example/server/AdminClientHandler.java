package org.example.server;

import org.example.dao.MySQLCourseDao;
import org.example.dao.MySQLEnrollmentDao;
import org.example.dao.MySQLUserDao;
import org.example.models.Course;
import org.example.models.Student;
import org.example.users.User;
import org.example.users.UserType;
import org.example.util.FormatData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class AdminClientHandler {
    private final Socket clientSocket;
    private final MySQLUserDao mySQLUserDao;
    private final MySQLCourseDao mySQLCourseDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    private final FormatData formatData;
    public AdminClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        mySQLUserDao = new MySQLUserDao();
        mySQLCourseDao = new MySQLCourseDao();
        mySQLEnrollmentDao = new MySQLEnrollmentDao();
        formatData = new FormatData();
    }

    public void runHandler() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        int option;

        do {
            option = dataInputStream.readInt();
            switch (option) {
                case 1:
                    UserType newUserType = choseUserType(dataInputStream.readInt());
                    mySQLUserDao.save(dataInputStream, dataOutputStream, newUserType);
                    dataOutputStream.writeUTF("A new user is added successfully");
                    break;
                case 2:
                    String getUserId = dataInputStream.readUTF();
                    User fetchedUser = mySQLUserDao.get(getUserId);
                    dataOutputStream.writeUTF(String.valueOf(fetchedUser));
                    break;
                case 3:
                    List<User> fetchedUsers = mySQLUserDao.getAll();
                    dataOutputStream.writeUTF(formatData.formatUsers(fetchedUsers));
                    break;
                case 4:
                    String updateUserId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLUserDao.get(updateUserId)));
                    dataOutputStream.flush();
                    int updateUserOption = dataInputStream.readInt();
                    mySQLUserDao.update(dataInputStream, dataOutputStream, updateUserId, updateUserOption);
                    dataOutputStream.writeUTF("User updated successfully");
                    break;
                case 5:
                    String deleteUserId = dataInputStream.readUTF();
                    mySQLUserDao.delete(deleteUserId);
                    dataOutputStream.writeUTF( "User with id: " + deleteUserId + ", is deleted");
                    break;
                case 6:
                    mySQLCourseDao.save(dataInputStream, dataOutputStream);
                    dataOutputStream.writeUTF("A new course is added successfully");
                    break;
                case 7:
                    dataOutputStream.writeUTF(String.valueOf(mySQLCourseDao.get(dataInputStream.readUTF())));
                    break;
                case 8:
                    dataOutputStream.writeUTF(formatData.formatCourses(mySQLCourseDao.getAll()));
                    break;
                case 9:
                    String updateCourseId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLCourseDao.get(updateCourseId)));
                    dataOutputStream.flush();
                    int updateCourseOption = dataInputStream.readInt();
                    mySQLCourseDao.update(dataInputStream, dataOutputStream, updateCourseId, updateCourseOption);
                    dataOutputStream.writeUTF("Course updated successfully");
                    break;
                case 10:
                    String deleteCourseId = dataInputStream.readUTF();
                    mySQLCourseDao.delete(deleteCourseId);
                    dataOutputStream.writeUTF("Course with id: " + deleteCourseId + ", is deleted");
                    break;
                case 11:
                    List<Course> fetchedCourses = mySQLEnrollmentDao.getStudentCourses(dataInputStream.readUTF());
                    dataOutputStream.writeUTF(formatData.formatCourses(fetchedCourses));
                    break;
                case 12:
                    List<Student> fetchedStudents = mySQLEnrollmentDao.getCourseStudents(dataInputStream.readUTF());
                    dataOutputStream.writeUTF(formatData.formatStudents(fetchedStudents));
                    break;
                default:
                    dataOutputStream.writeUTF("Invalid option");
            }

            dataOutputStream.flush();
        } while (option != 13);
    }

    private UserType choseUserType(int chosenUserType) {
        return switch (chosenUserType) {
            case 1 -> UserType.ADMIN;
            case 2 -> UserType.INSTRUCTOR;
            case 3 -> UserType.STUDENT;
            default -> null;
        };
    }
}
