package org.example.dao;

import org.example.models.Grade;

import java.io.DataInputStream;
import java.util.List;

public interface GradeDao {
    Grade get(String courseId, String studentId);
    List<Grade> getStudentGrades(String studentId);
    List<Grade> getCourseGrades(String courseId);
    void save(DataInputStream dataInputStream);
    void update(DataInputStream dataInputStream, String courseId, String studentId);
    void delete(String courseId, String studentId);
}
