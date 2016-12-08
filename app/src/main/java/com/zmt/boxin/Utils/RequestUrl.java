package com.zmt.boxin.Utils;

/**
 * Created by Dangelo on 2016/7/26.
 */
public class RequestUrl {

    private String name;
    private String number;

    public RequestUrl(){}

    public RequestUrl(String number){
        this.number = number;
    }

    public RequestUrl(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public final String IP = "http://222.24.19.201/";

    /**
     * 获取验证码
     */
    public final String identifyCode = IP + "CheckCode.aspx";

    /**
     * 登陆，获取session
     */
    public final String cookieUrl = IP + "default2.aspx";

    /**
     * 正方首页
     */
    public String resultUrl = IP + "xs_main.aspx?xh=";

    /**
     * 课表
     */
    public String kbUrl = IP + "xskbcx.aspx?xh=";

    /**
     * 个人信息
     */
    public String messageUrl = IP + "xsgrxx.aspx?xh=";

    /**
     * 成绩
     */
    public String scoreUrl = IP + "xscjcx.aspx?xh=";

    /**
     * 培养计划
     */
    public String trainPlanUrl = IP + "pyjh.aspx?xh=";

    public String physicalTest = "http://yd.boxkj.com/app/measure/getStuTotalScore";

    public String getName() {
        return name;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public String getHomeUrl() {
        return resultUrl + number;
    }

    public String getCoursesUrl() {
        return kbUrl  + number + "&xm=" + name +  "&gnmkdm=N121603";
    }

    public String getMessageUrl() {
        return messageUrl + number + "&xm=" + name + "&gnmkdm=N121501";
    }

    public String getIP(){
        return IP;
    }

    public String getScoreUrl() {
        return scoreUrl + number + "&xm=" + name + "&gnmkdm=N121605";
    }

    public String getTrainPlan(){
        return trainPlanUrl + number + "&xm=" + name + "&gnmkdm=N121607";
    }

    public String getPhysicalTest(){
        return physicalTest;
    }

}
