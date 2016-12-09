package com.zmt.boxin.Module;

/**
 * Created by MintaoZhu on 2016/12/8.
 */
public class PhysicalTest {

    private String meaScoreId;  //总分Id
    private String year;        //学年
    private String totalScore;  //总分

    public String getMeaScoreId() {
        return meaScoreId;
    }

    public PhysicalTest setMeaScoreId(String meaScoreId) {
        this.meaScoreId = meaScoreId;
        return this;
    }

    public String getYear() {
        return year;
    }

    public PhysicalTest setYear(String year) {
        this.year = year;
        return this;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public PhysicalTest setTotalScore(String totalScore) {
        this.totalScore = totalScore;
        return this;
    }

}
