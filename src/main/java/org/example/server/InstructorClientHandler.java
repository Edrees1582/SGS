package org.example.server;

import org.example.Dao.MySQLCourseDao;
import org.example.Dao.MySQLEnrollmentDao;
import org.example.Dao.MySQLGradeDao;
import org.example.Dao.MySQLUserDao;
import org.example.models.Course;
import org.example.models.Grade;
import org.example.models.Student;
import org.example.users.User;
import org.example.users.UserType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class InstructorClientHandler {
    private final Socket clientSocket;
    private final MySQLUserDao mySQLUserDao;
    private final MySQLCourseDao mySQLCourseDao;
    private final MySQLGradeDao mySQLGradeDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    public InstructorClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        mySQLUserDao = new MySQLUserDao();
        mySQLCourseDao = new MySQLCourseDao();
        mySQLGradeDao = new MySQLGradeDao();
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
                    mySQLEnrollmentDao.save(dataInputStream);
                    dataOutputStream.writeUTF("A student is enrolled successfully");
                    break;
                case 2:
                    String unEnrollCourseId = dataInputStream.readUTF();
                    String unEnrollStudentId = dataInputStream.readUTF();
                    mySQLEnrollmentDao.delete(unEnrollCourseId, unEnrollStudentId);
                    dataOutputStream.writeUTF("Student with id: " + unEnrollStudentId + " un-enrolled from course with id: " + unEnrollCourseId);
                    break;
                case 3:
                    String getCourseId = dataInputStream.readUTF();
                    List<Student> fetchedStudents = mySQLEnrollmentDao.getCourseStudents(getCourseId);
                    dataOutputStream.writeUTF(String.valueOf(fetchedStudents));
                    break;
                case 4:
                    mySQLGradeDao.save(dataInputStream);
                    dataOutputStream.writeUTF("A grade is added successfully");
                    break;
                case 5:
                    String viewCourseId = dataInputStream.readUTF();
                    String viewStudentId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLGradeDao.get(viewCourseId, viewStudentId)));
                    break;
                case 6:
                    String updateCourseId = dataInputStream.readUTF();
                    String updateStudentId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLGradeDao.get(updateCourseId, updateStudentId)));
                    dataOutputStream.flush();
                    mySQLGradeDao.update(dataInputStream, updateCourseId, updateStudentId);
                    dataOutputStream.writeUTF("Grade updated successfully");
                    break;
                case 7:
                    String deleteGradeCourseId = dataInputStream.readUTF();
                    String deleteGradeStudentId = dataInputStream.readUTF();
                    mySQLGradeDao.delete(deleteGradeCourseId, deleteGradeStudentId);
                    dataOutputStream.writeUTF("Grade with course id: " + deleteGradeCourseId + ", and student id: " + deleteGradeStudentId + ", is deleted");
                    break;
                default:
                    dataOutputStream.writeUTF("Invalid option");
            }

            dataOutputStream.flush();
        } while (option != 8);
    }
}
