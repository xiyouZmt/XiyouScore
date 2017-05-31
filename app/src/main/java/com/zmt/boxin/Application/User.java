package com.zmt.boxin.Application;

import com.zmt.boxin.Module.ExamCourse;
import com.zmt.boxin.Module.PhysicalTest;
import com.zmt.boxin.Module.PhysicalTestItem;
import com.zmt.boxin.Module.TrainCourses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class User implements Serializable {

    private String name = "";
    private String number = "";
    private String sex = "";
    private String colleague = "";
    private String major = "";
    private String classes = "";
    private String imageUrl = "";
    private String Cookie = "";
    private String scoreValue = "";
    private String trainValue = "";
    private String currentTerm = "";
    private String scoreYear = "";
    private String _VIEWSTATE = "";
    private int scoreTerm;

    public String get_VIEWSTATE() {
        return _VIEWSTATE;
    }

    public void set_VIEWSTATE(String _VIEWSTATE) {
        this._VIEWSTATE = _VIEWSTATE;
    }

    public String getScoreYear() {
        return scoreYear;
    }

    public void setScoreYear(String scoreYear) {
        this.scoreYear = scoreYear;
    }

    public int getScoreTerm() {
        return scoreTerm;
    }

    public void setScoreTerm(int scoreTerm) {
        this.scoreTerm = scoreTerm;
    }

    public String getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(String currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getTrainValue() {
        return trainValue;
    }

    public void setTrainValue(String trainValue) {
        this.trainValue = trainValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getScoreValue() {
        return scoreValue;
    }

    private List<String> termList = new ArrayList<>();

    public List<String> getTermList() {
        return termList;
    }

    private List< Map<String, String> > failedPass = new ArrayList<>();

    public List<Map<String, String>> getFailedPass() {
        return failedPass;
    }

    private List<ExamCourse> scoreList = new ArrayList<>();

    public List<ExamCourse> getScoreList() {
        return scoreList;
    }

    private List<PhysicalTest> physicalTest = new ArrayList<>();

    public List<PhysicalTest> getPhysicalTest(){
        return physicalTest;
    }

    private List<PhysicalTestItem> physicalTestItem = new ArrayList<>();

    public List<PhysicalTestItem> getPhysicalTestItem(){
        return physicalTestItem;
    }

    private List<TrainCourses> trainCoursesList = new ArrayList<>();

    public List<TrainCourses> getTrainCoursesList() {
        return trainCoursesList;
    }

    public String getCookie() {
        return Cookie;
    }

    public void setCookie(String cookie) {
        Cookie = cookie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getColleague() {
        return colleague;
    }

    public void setColleague(String colleague) {
        this.colleague = colleague;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
