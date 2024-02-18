package org.example.server;

import org.example.dao.MySQLCourseDao;
import org.example.dao.MySQLEnrollmentDao;
import org.example.dao.MySQLGradeDao;
import org.example.dao.MySQLUserDao;
import org.example.models.Student;
import org.example.users.User;
import org.example.util.FormatData;
import org.example.util.GradesStatistics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class InstructorClientHandler {
    private final Socket clientSocket;
    private final MySQLGradeDao mySQLGradeDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    private final FormatData formatData;
    private final GradesStatistics gradesStatistics;
    public InstructorClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        mySQLGradeDao = new MySQLGradeDao();
        mySQLEnrollmentDao = new MySQLEnrollmentDao();
        formatData = new FormatData();
        gradesStatistics = new GradesStatistics();
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
                    dataOutputStream.writeUTF(formatData.formatStudents(fetchedStudents));
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
                case 8:
                    String statsCourseId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(gradesStatistics.getCourseStatistics(statsCourseId));
                default:
                    dataOutputStream.writeUTF("Invalid option");
            }

            dataOutputStream.flush();
        } while (option != 9);
    }
}
