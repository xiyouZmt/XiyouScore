package com.zmt.boxin.Module;

/**
 * Created by MintaoZhu on 2016/11/29.
 */
public class TrainCourses {

    private String courseName;
    private String courseCredit;
    private String weekHours;
    private String examMethod;
    private String courseProperty;
    private String courseTime;

    public String getCourseName() {
        return courseName;
    }

    public TrainCourses setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public TrainCourses setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
        return this;
    }

    public String getWeekHours() {
        return weekHours;
    }

    public TrainCourses setWeekHours(String weekHours) {
        this.weekHours = weekHours;
        return this;
    }

    public String getExamMethod() {
        return examMethod;
    }

    public TrainCourses setExamMethod(String examMethod) {
        this.examMethod = examMethod;
        return this;
    }

    public String getCourseProperty() {
        return courseProperty;
    }

    public TrainCourses setCourseProperty(String courseProperty) {
        this.courseProperty = courseProperty;
        return this;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public TrainCourses setCourseTime(String courseTime) {
        this.courseTime = courseTime;
        return this;
    }
}
