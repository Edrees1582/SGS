package org.example.util;

import org.example.dao.MySQLGradeDao;
import org.example.models.Grade;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GradesStatistics {
    private final MySQLGradeDao mySQLGradeDao;

    public GradesStatistics() {
        mySQLGradeDao = new MySQLGradeDao();
    }

    public String getCourseStatistics(String courseId) {
        List<Grade> grades = mySQLGradeDao.getCourseGrades(courseId);
        String stats = "";
        double average = grades.stream().mapToDouble(Grade::getGrade).reduce(0.0, Double::sum) / grades.size();

        double median = 0.0;
        List<Double> medianGrades = grades.stream().mapToDouble(Grade::getGrade).sorted().boxed().toList();
        if (!medianGrades.isEmpty() && medianGrades.size() % 2 != 0)
            median = medianGrades.get(medianGrades.size() / 2);
        else if (!medianGrades.isEmpty())
            median = (medianGrades.get((medianGrades.size() - 1) / 2) + medianGrades.get(medianGrades.size() / 2)) / 2.0;

        double highest = Collections.max(grades, Comparator.comparing(Grade::getGrade)).getGrade();
        double lowest = Collections.min(grades, Comparator.comparing(Grade::getGrade)).getGrade();

        stats += ("Course (" + courseId + ") grade statistics:\n");
        stats += ("Average: " + average + "\n");
        stats += ("Median: " + median + "\n");
        stats += ("Highest: " + highest + "\n");
        stats += ("Lowest: " + lowest + "\n");

        return stats;
    }

    public String getStudentStatistics(String studentId) {
        List<Grade> grades = mySQLGradeDao.getStudentGrades(studentId);
        String stats = "";
        double average = grades.stream().mapToDouble(Grade::getGrade).reduce(0.0, Double::sum) / grades.size();

        double median = 0.0;
        List<Double> medianGrades = grades.stream().mapToDouble(Grade::getGrade).sorted().boxed().toList();
        if (!medianGrades.isEmpty() && medianGrades.size() % 2 != 0)
            median = medianGrades.get(medianGrades.size() / 2);
        else if (!medianGrades.isEmpty())
            median = (medianGrades.get((medianGrades.size() - 1) / 2) + medianGrades.get(medianGrades.size() / 2)) / 2.0;

        double highest = Collections.max(grades, Comparator.comparing(Grade::getGrade)).getGrade();
        double lowest = Collections.min(grades, Comparator.comparing(Grade::getGrade)).getGrade();

        stats += ("Student (" + studentId + ") grade statistics:\n");
        stats += ("Average: " + average + "\n");
        stats += ("Median: " + median + "\n");
        stats += ("Highest: " + highest + "\n");
        stats += ("Lowest: " + lowest + "\n");

        return stats;
    }
}
