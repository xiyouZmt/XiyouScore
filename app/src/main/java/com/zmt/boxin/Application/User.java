package com.zmt.boxin.Application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class User implements Serializable {

    private String name;
    private String number;
    private String sex;
    private String colleague;
    private String major;
    private String classes;
    private String imageUrl;
    private String Cookie;
    private String scoreValue;

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getScoreValue() {

        return scoreValue;
    }

    private List<String> termList = new ArrayList<>();

    private List< Map<String, String> > failedPass = new ArrayList<>();

    private List< List<Map<String, String>> > scoreList = new ArrayList<>();

    public List<String> getTermList() {
        return termList;
    }

    public List<Map<String, String>> getFailedPass() {
        return failedPass;
    }

    public List< List<Map<String, String>> > getScoreList() {
        return scoreList;
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
