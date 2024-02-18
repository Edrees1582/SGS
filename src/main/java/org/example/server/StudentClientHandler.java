package org.example.server;

import org.example.dao.MySQLEnrollmentDao;
import org.example.dao.MySQLGradeDao;
import org.example.models.Course;
import org.example.models.Grade;
import org.example.util.FormatData;
import org.example.util.GradesStatistics;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class StudentClientHandler {
    private final Socket clientSocket;
    private final MySQLGradeDao mySQLGradeDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    private final FormatData formatData;
    private final GradesStatistics gradesStatistics;
    public StudentClientHandler(Socket clientSocket) {
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
                    String getStudentId = dataInputStream.readUTF();
                    List<Course> fetchedCourses = mySQLEnrollmentDao.getStudentCourses(getStudentId);
                    dataOutputStream.writeUTF(formatData.formatCourses(fetchedCourses));
                    break;
                case 2:
                    String viewCourseId = dataInputStream.readUTF();
                    String viewStudentId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(String.valueOf(mySQLGradeDao.get(viewCourseId, viewStudentId)));
                    break;
                case 3:
                    String viewGradesStudentId = dataInputStream.readUTF();
                    List<Grade> fetchedGrades = mySQLGradeDao.getStudentGrades(viewGradesStudentId);
                    dataOutputStream.writeUTF(formatData.formatGrades(fetchedGrades));
                    break;
                case 4:
                    String statisticsStudentId = dataInputStream.readUTF();
                    dataOutputStream.writeUTF(gradesStatistics.getStudentStatistics(statisticsStudentId));
                    break;
                default:
                    dataOutputStream.writeUTF("Invalid option");
            }

            dataOutputStream.flush();
        } while (option != 5);
    }
}
