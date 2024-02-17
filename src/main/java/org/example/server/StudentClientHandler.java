package org.example.server;

import org.example.dao.MySQLEnrollmentDao;
import org.example.dao.MySQLGradeDao;
import org.example.models.Course;
import org.example.models.Student;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class StudentClientHandler {
    private final Socket clientSocket;
    private final MySQLGradeDao mySQLGradeDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    public StudentClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
                    String getStudentId = dataInputStream.readUTF();
                    List<Course> fetchedCourses = mySQLEnrollmentDao.getStudentCourses(getStudentId);
                    dataOutputStream.writeUTF(String.valueOf(fetchedCourses));
//                    dataOutputStream.writeUTF(String.valueOf(fetchedCourses));
                    break;
                case 2:
                    String viewCourseId = dataInputStream.readUTF();
                    String viewStudentId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLGradeDao.get(viewCourseId, viewStudentId)));
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
                default:
                    dataOutputStream.writeUTF("Invalid option");
            }

            dataOutputStream.flush();
        } while (option != 4);
    }
}
