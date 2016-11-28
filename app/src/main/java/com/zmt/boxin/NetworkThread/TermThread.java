package com.zmt.boxin.NetworkThread;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dangelo on 2016/7/30.
 */
public class TermThread implements Runnable{

    private String url;
    private Handler handler;
    private App app;

    public TermThread(String url, Handler handler, App app) {
        this.url = url;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, "Cookie", app.getUser().getCookie());
        String content = okHttpUtils.getMessageByGet();
        Message msg = new Message();
        switch (content){
            case "error" :
            case "fail" :
            case "no evaluate" :
                msg.obj = content;
                handler.sendMessage(msg);
                break;
            default :
                Bundle bundle = getTerm(content);
                ScoreThread scoreThread = new ScoreThread(app, url, bundle, handler);
                Thread t = new Thread(scoreThread, "ScoreThread");
                t.start();
//                handler.sendMessage(msg);
                break;
        }
    }

    public Bundle getTerm(String content){
        int i = 0;
        Bundle bundle = new Bundle();
        Document document = Jsoup.parse(content);
        /**
         * 获取成绩value
         */
        Element element = document.getElementById("Form1");
        Elements elements = element.getElementsByAttributeValue("name", "__VIEWSTATE");
        for (int j = 0; j < elements.size(); j++) {
            if(elements.get(i).tag().toString().equals("input")){
                String scoreValue = elements.get(i).attr("value");
                app.getUser().setScoreValue(scoreValue);
            }
        }
        /**
         * 获取学期数据
         */
        element = document.getElementById("ddlXN");
        if(element != null){
            for (Element element1 : element.getAllElements()) {
                if(!element1.text().equals("") && element1.tag().toString().equals("option")){
                    bundle.putString("year" + i, element1.text());
                    i++;
                }
            }
        }
        /**
         * 至今未通过成绩
         */
        element = document.getElementById("Datagrid3");
        elements = element.getElementsByTag("tr");
        for (i = 1; i < elements.size(); i++) {
            String courseName = elements.get(i).getAllElements().get(2).text();
            String property = elements.get(i).getAllElements().get(3).text();
            String credit = elements.get(i).getAllElements().get(4).text();
            String score = elements.get(i).getAllElements().get(5).text();
            Map<String, String> map = new HashMap<>();
            map.put("courseName", courseName);
            map.put("property", property);
            map.put("credit", credit);
            map.put("score", score);
            app.getUser().getFailedPass().add(map);
        }
        return bundle;
    }

}
