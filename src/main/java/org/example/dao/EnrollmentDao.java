package org.example.dao;

import org.example.models.Course;
import org.example.models.Enrollment;
import org.example.models.Student;

import java.io.DataInputStream;
import java.util.List;

public interface EnrollmentDao {
    List<Course> getStudentCourses(String studentId);
    List<Student> getCourseStudents(String courseId);
    List<Enrollment> getAll();
    void save(DataInputStream dataInputStream);
    void delete(String courseId, String studentId);
    void deleteByStudent(String studentId);
    void deleteByCourse(String courseId);
}
