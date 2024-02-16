package org.example.Dao;

import org.example.models.Course;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

public interface CourseDao {
    Course get(String id);
    List<Course> getAll();
    void save(DataInputStream dataInputStream, DataOutputStream dataOutputStream);
    void update(DataInputStream dataInputStream, DataOutputStream dataOutputStream, String id, int updateOption);
    void delete(String id);
}
