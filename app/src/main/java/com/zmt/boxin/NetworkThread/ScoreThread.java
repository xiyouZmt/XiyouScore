package com.zmt.boxin.NetworkThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
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
                List<Map<String, String>> termScore = new ArrayList<>();
                if(object != null){
                    /**
                     * 获取每学期成绩页面
                     */
                    document = Jsoup.parse(scoreBundle.get(object.toString() + " " + j).toString());

//                    Element element1 = document.getElementById("Form1");
//                    Elements elements1 = element1.getElementsByAttributeValue("name", "__VIEWSTATE");
//                    for (j = 0; j < elements1.size(); j++) {
//                        if(elements1.get(i).tag().toString().equals("input")){
//                            String scoreValue = elements1.get(i).attr("value");
//                            byte [] value = Base64.decode(scoreValue, Base64.DEFAULT);
//                            try {
//                                String str = new String(value, "utf-8");
//                                System.out.println(str);
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        }
//                    }

                    Element element = document.getElementById("Datagrid1");
                    Elements elements = element.getElementsByTag("tr");
                    for (int k = 1; k < elements.size(); k++) {
                        Elements scoreElements = elements.get(k).getAllElements();
                        String year = scoreElements.get(1).text();               //学年
                        String term = scoreElements.get(2).text();               //学期
                        String courseName = scoreElements.get(4).text();         //课程名称
                        String property = scoreElements.get(5).text();           //课程性质
                        String credit = scoreElements.get(7).text();             //课程学分
                        String score = scoreElements.get(9).text();              //课程成绩
                        String resit = scoreElements.get(11).text();             //补考成绩
                        String retake = scoreElements.get(12).text();            //重修成绩
                        Map<String, String> map = new HashMap<>();
                        map.put("year", year);
                        map.put("term", term);
                        map.put("courseName", courseName);
                        map.put("property", property);
                        map.put("credit", credit);
                        map.put("score", score);
                        map.put("resit", resit);
                        map.put("retake", retake);
                        termScore.add(map);
                    }
                }
                app.getUser().getScoreList().add(termScore);
            }
        }
    }

}
