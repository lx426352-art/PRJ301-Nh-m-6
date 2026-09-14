package com.aita.gitanalytics.dto;

import java.io.Serializable;

public class CourseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int courseId;
    private String courseCode;
    private String courseName;
    private String semester;

    public CourseDTO() {
    }

    public CourseDTO(int courseId, String courseCode, String courseName, String semester) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
