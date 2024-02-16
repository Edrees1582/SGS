package org.example.server;

import org.example.Dao.MySQLCourseDao;
import org.example.Dao.MySQLEnrollmentDao;
import org.example.Dao.MySQLUserDao;
import org.example.models.Course;
import org.example.models.Student;
import org.example.users.User;
import org.example.users.UserType;

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
    public AdminClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        mySQLUserDao = new MySQLUserDao();
        mySQLCourseDao = new MySQLCourseDao();
        mySQLEnrollmentDao = new MySQLEnrollmentDao();
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
                    UserType userType = choseUserType(dataInputStream.readInt());
                    String getUserId = dataInputStream.readUTF();
                    User fetchedUser = mySQLUserDao.get(getUserId, userType);
                    dataOutputStream.writeUTF(String.valueOf(fetchedUser));
                    break;
                case 3:
                    UserType usersType = choseUserType(dataInputStream.readInt());
                    List<User> fetchedUsers = mySQLUserDao.getAll(usersType);
                    dataOutputStream.writeUTF(String.valueOf(fetchedUsers));
                    break;
                case 4:
                    UserType updateUserType = choseUserType(dataInputStream.readInt());
                    String updateUserId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLUserDao.get(updateUserId, updateUserType)));
                    dataOutputStream.flush();
                    int updateUserOption = dataInputStream.readInt();
                    mySQLUserDao.update(dataInputStream, dataOutputStream, updateUserId, updateUserType, updateUserOption);
                    dataOutputStream.writeUTF("User updated successfully");
                    break;
                case 5:
                    UserType deleteUserType = choseUserType(dataInputStream.readInt());
                    String deleteUserId = dataInputStream.readUTF();
                    mySQLUserDao.delete(deleteUserId, deleteUserType);
                    dataOutputStream.writeUTF(deleteUserType + " with id: " + deleteUserId + ", is deleted");
                    break;
                case 6:
                    mySQLCourseDao.save(dataInputStream, dataOutputStream);
                    dataOutputStream.writeUTF("A new course is added successfully");
                    break;
                case 7:
                    dataOutputStream.writeUTF(String.valueOf(mySQLCourseDao.get(dataInputStream.readUTF())));
                    break;
                case 8:
                    dataOutputStream.writeUTF(String.valueOf(mySQLCourseDao.getAll()));
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
                    dataOutputStream.writeUTF(String.valueOf(fetchedCourses));
                    break;
                case 12:
                    List<Student> fetchedStudents = mySQLEnrollmentDao.getCourseStudents(dataInputStream.readUTF());
                    dataOutputStream.writeUTF(String.valueOf(fetchedStudents));
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
