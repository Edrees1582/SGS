package org.example.Dao;

import org.example.models.Grade;

import java.io.DataInputStream;

public interface GradeDao {
    Grade get(String courseId, String studentId);
    void save(DataInputStream dataInputStream);
    void update(DataInputStream dataInputStream, String courseId, String studentId);
    void delete(String courseId, String studentId);
}
