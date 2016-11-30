package com.zmt.boxin.Module;

/**
 * Created by MintaoZhu on 2016/11/30.
 */
public class ExamCourse {

    private String currentYear;
    private String currentTerm;
    private String courseName;
    private String courseProperty;
    private String courseCredit;
    private String courseScore;
    private String resitScore;
    private String retakeScore;

    public String getCurrentYear() {
        return currentYear;
    }

    public ExamCourse setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
        return this;
    }

    public String getCurrentTerm() {
        return currentTerm;
    }

    public ExamCourse setCurrentTerm(String currentTerm) {
        this.currentTerm = currentTerm;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public ExamCourse setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String getCourseProperty() {
        return courseProperty;
    }

    public ExamCourse setCourseProperty(String courseProperty) {
        this.courseProperty = courseProperty;
        return this;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public ExamCourse setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
        return this;
    }

    public String getCourseScore() {
        return courseScore;
    }

    public ExamCourse setCourseScore(String courseScore) {
        this.courseScore = courseScore;
        return this;
    }

    public String getResitScore() {
        return resitScore;
    }

    public ExamCourse setResitScore(String resitScore) {
        this.resitScore = resitScore;
        return this;
    }

    public String getRetakeScore() {
        return retakeScore;
    }

    public ExamCourse setRetakeScore(String retakeScore) {
        this.retakeScore = retakeScore;
        return this;
    }
}
