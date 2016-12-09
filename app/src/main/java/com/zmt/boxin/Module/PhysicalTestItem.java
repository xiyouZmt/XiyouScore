package com.zmt.boxin.Module;

/**
 * Created by MintaoZhu on 2016/12/8.
 */
public class PhysicalTestItem {

    private String examName;    //项目名称
    private String examUnit;    //单位
    private String actualScore; //成绩
    private String score;       //分数
    private String rank;        //等级
    private String plusRank;    //加分
    private String meaStatus;   //状态

    public String getExamName() {
        return examName;
    }

    public PhysicalTestItem setExamName(String examName) {
        this.examName = examName;
        return this;
    }

    public String getExamUnit() {
        return examUnit;
    }

    public PhysicalTestItem setExamUnit(String examUnit) {
        this.examUnit = examUnit;
        return this;
    }

    public String getActualScore() {
        return actualScore;
    }

    public PhysicalTestItem setActualScore(String actualScore) {
        this.actualScore = actualScore;
        return this;
    }

    public String getScore() {
        return score;
    }

    public PhysicalTestItem setScore(String score) {
        this.score = score;
        return this;
    }

    public String getRank() {
        return rank;
    }

    public PhysicalTestItem setRank(String rank) {
        this.rank = rank;
        return this;
    }

    public String getPlusRank() {
        return plusRank;
    }

    public PhysicalTestItem setPlusRank(String plusRank) {
        this.plusRank = plusRank;
        return this;
    }

    public String getMeaStatus() {
        return meaStatus;
    }

    public PhysicalTestItem setMeaStatus(String meaStatus) {
        this.meaStatus = meaStatus;
        return this;
    }

}
