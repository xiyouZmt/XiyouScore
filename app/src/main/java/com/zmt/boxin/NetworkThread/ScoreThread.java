package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Module.ExamCourse;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Dangelo on 2016/7/30.
 */
public class ScoreThread implements Runnable{

    private App app;
    private String url;
    private String year;
    private String term;
    private Handler handler;
    public  static final int DEFAULT_YEAR_POSITION = 0;
    public  static final int DEFAULT_TERM_POSITION = 2;

    public ScoreThread(App app, String url, Handler handler) {
        this.app = app;
        this.url = url;
        this.handler = handler;
        term = DEFAULT_TERM_POSITION + "";
        if(app.getUser().getTermList().size() != 0){
            year = app.getUser().getTermList().get(DEFAULT_YEAR_POSITION);
        }
    }

    public ScoreThread(App app, String url, String year, String term, Handler handler) {
        this.app = app;
        this.url = url;
        this.year = year;
        this.term = term;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        Message msg = new Message();
        if(year != null){
            String content = okHttpUtils.getScoreByPost(year, term, app.getUser().getScoreValue());
            switch (content){
                case "error" :
                case "fail" :
                case "no evaluate" :
                    msg.obj = content;
                    handler.sendMessage(msg);
                    break;
                default :
                    /**
                     * 默认获取当前学年第二学期成绩，如果为空获取第一学期，再为空则成绩为空(目测大一同学)
                     */
                    boolean result = setScore(content, app);
                    if(result){
                        msg.obj = year;
                        msg.what = Integer.parseInt(term);
                        handler.sendMessage(msg);
                    } else {
                        if(term.equals("2")){
                            term = "1";
                            run();
                        } else {
                            msg.obj = "null";
                            handler.sendMessage(msg);
                        }
                    }
                    break;
            }
        } else {
            msg.obj = "null";
            handler.sendMessage(msg);
        }
    }

    public boolean setScore(String scoreContent ,App app){
        app.getUser().getScoreList().clear();
        Document document = Jsoup.parse(scoreContent);
        Element element = document.getElementById("Datagrid1");
        if(element != null){
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
                app.getUser().getScoreList().add(examCourse);
            }
            return true;
        } else {
            return false;
        }
    }
}
