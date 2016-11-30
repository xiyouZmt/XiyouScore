package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Module.TrainCourses;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by MintaoZhu on 2016/11/30.
 */
public class GetTrainPlan extends Thread{

    private String url;
    private String trainValue;
    private Handler handler;
    private App app;
    private String term;

    public GetTrainPlan(String url, String trainValue, String term, Handler handler, App app) {
        this.url = url;
        this.trainValue = trainValue;
        this.term = term;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        String content;
        content = okHttpUtils.getTrainPlan(trainValue, term);
        Message msg = handler.obtainMessage();
        switch (content){
            case "error" :
            case "fail" :
            case "no evaluate" :
                msg.obj = content;
                handler.sendMessage(msg);
                break;
            default :
                getTrainCourses(content, app);
                msg.obj = "success";
                handler.sendMessage(msg);
                break;
        }
    }

    public void getTrainCourses(String trainContent, App app){
        app.getUser().getTrainCoursesList().clear();
        Document document = Jsoup.parse(trainContent);
        Element element = document.getElementById("DBGrid");
        Elements elements = element.getElementsByTag("tr");
        for (int i = 1; i < elements.size(); i++) {
            Elements courseElements = elements.get(i).getAllElements();
            String courseName = courseElements.get(2).text();
            String courseCredit = courseElements.get(3).text();
            String weekHours = courseElements.get(4).text();
            String examMethod = courseElements.get(5).text();
            String courseProperty = courseElements.get(6).text();
            String courseTime = courseElements.get(16).text();
            TrainCourses courses = new TrainCourses();
            courses.setCourseName(courseName).setCourseCredit(courseCredit).setWeekHours(weekHours)
                    .setExamMethod(examMethod).setCourseProperty(courseProperty).setCourseTime(courseTime);
            app.getUser().getTrainCoursesList().add(courses);
        }
        DefaultTrain.setTrainValue(trainContent, app);
    }
}
