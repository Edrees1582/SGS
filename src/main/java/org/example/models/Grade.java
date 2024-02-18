package org.example.models;

public class Grade {
    private String courseId;
    private String studentId;
    private double grade;

    public Grade(String courseId, String studentId, double grade) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.grade = grade;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "courseId='" + courseId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
