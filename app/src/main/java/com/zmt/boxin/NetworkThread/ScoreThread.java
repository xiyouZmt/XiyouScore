package com.zmt.boxin.NetworkThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Module.ExamCourse;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dangelo on 2016/7/30.
 */
public class ScoreThread implements Runnable{

    private App app;
    private String url;
    private Bundle termBundle;
    private Handler handler;

    public ScoreThread(App app, String url, Bundle termBundle, Handler handler) {
        this.app = app;
        this.url = url;
        this.termBundle = termBundle;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        Bundle scoreBundle = new Bundle();
        String content = okHttpUtils.getScoreByPost(termBundle, scoreBundle, app.getUser().getScoreValue());
        Message msg = new Message();
        switch (content){
            case "error" :
            case "fail" :
            case "no evaluate" :
                msg.obj = content;
                handler.sendMessage(msg);
                break;
            case "score is ok" :
                msg.obj = "score is ok";
                msg.setData(termBundle);
                getScore(scoreBundle);
                handler.sendMessage(msg);
                break;
        }
    }

    public void getScore(Bundle scoreBundle){
        Document document;
        for (int i = 0; i < termBundle.size(); i++) {
            Object object = termBundle.get("year" + i);
            for (int j = 2; j > 0; j--){
                List<ExamCourse> termScore = new ArrayList<>();
                if(object != null){
                    /**
                     * 每学期成绩数据
                     */
                    document = Jsoup.parse(scoreBundle.get(object.toString() + " " + j).toString());
                    Element element = document.getElementById("Datagrid1");
                    Elements elements = element.getElementsByTag("tr");
                    for (int k = 1; k < elements.size(); k++) {
                        Elements scoreElements = elements.get(k).getAllElements();
                        String year = scoreElements.get(1).text();               //学年
                        String term = scoreElements.get(2).text();               //学期
                        String name = scoreElements.get(4).text();               //课程名称
                        String property = scoreElements.get(5).text();           //课程性质
                        String credit = scoreElements.get(7).text();             //课程学分
                        String score = scoreElements.get(9).text();              //课程成绩
                        String resit = scoreElements.get(11).text();             //补考成绩
                        String retake = scoreElements.get(12).text();            //重修成绩
                        ExamCourse examCourse = new ExamCourse();
                        examCourse.setCurrentYear(year).setCurrentTerm(term).setCourseName(name)
                                .setCourseProperty(property).setCourseCredit(credit)
                                .setCourseScore(score).setResitScore(resit).setRetakeScore(retake);
                        termScore.add(examCourse);
                    }
                }
                app.getUser().getScoreList().add(termScore);
            }
        }
    }

}
