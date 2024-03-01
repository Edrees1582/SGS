package org.example.util;

import org.example.models.Course;
import org.example.models.Enrollment;
import org.example.models.Grade;
import org.example.models.Student;
import org.example.users.User;
import org.example.users.UserType;

import java.util.List;

public class FormatData {
    public String formatUsers(List<User> fetchedUsers) {
        StringBuilder result = new StringBuilder();
        result.append("+------------+----------------------+\n");
        result.append(String.format("| %-10s | %-20s |\n", "User ID", "Name"));
        result.append("+------------+----------------------+\n");

        for (User user : fetchedUsers) {
            result.append(String.format("| %-10s | %-20s |\n", user.getId(), user.getName()));
        }

        result.append("+------------+----------------------+\n");

        return String.valueOf(result);
    }

    public String formatStudents(List<Student> fetchedStudents) {
        StringBuilder result = new StringBuilder();
        result.append("+------------+----------------------+\n");
        result.append(String.format("| %-10s | %-20s |\n", "Student ID", "Name"));
        result.append("+------------+----------------------+\n");

        for (Student student : fetchedStudents) {
            result.append(String.format("| %-10s | %-20s |\n", student.getId(), student.getName()));
        }

        result.append("+------------+----------------------+\n");

        return String.valueOf(result);
    }

    public String formatCourses(List<Course> courses) {
        StringBuilder result = new StringBuilder();
        result.append("+------------+----------------------+---------------+\n");
        result.append(String.format("| %-10s | %-20s | %-13s |\n", "Course ID", "Title", "Instructor ID"));
        result.append("+------------+----------------------+---------------+\n");

        for (Course course : courses) {
            result.append(String.format("| %-10s | %-20s | %-13s |\n", course.getId(), course.getTitle(), course.getInstructorId()));
        }

        result.append("+------------+----------------------+---------------+\n");

        return String.valueOf(result);
    }

    public String formatGrades(List<Grade> grades) {
        StringBuilder result = new StringBuilder();
        result.append("+------------+------------+-----------+\n");
        result.append(String.format("| %-10s | %-10s | %-9s |\n", "Course ID", "Student ID", "Grade"));
        result.append("+------------+------------+-----------+\n");

        for (Grade grade : grades) {
            result.append(String.format("| %-10s | %-10s | %-5f |\n", grade.getCourseId(), grade.getStudentId(), grade.getGrade()));
        }

        result.append("+------------+------------+-----------+\n");

        return String.valueOf(result);
    }
}
