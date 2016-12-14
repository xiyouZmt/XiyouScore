package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by MintaoZhu on 2016/11/29.
 */
public class DefaultTrain extends Thread {

    private String url;
    private Handler handler;
    private App app;

    public DefaultTrain(String url, Handler handler, App app) {
        this.url = url;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        String content = okHttpUtils.getMessageByGet();
        Message msg = handler.obtainMessage();
        switch (content){
            case "error" :
            case "fail" :
            case "no evaluate" :
                msg.obj = content;
                handler.sendMessage(msg);
                break;
            default :
                setTrainValue(content, app);
                msg.obj = getCurrentTerm(content);
                handler.sendMessage(msg);
                break;
        }
    }

    public static void setTrainValue(String trainContent, App app){
        Document document = Jsoup.parse(trainContent);
        Element element = document.getElementById("Form1");
        String trainValue = element.getAllElements().get(3).attr("value");
        app.getUser().setTrainValue(trainValue);
    }

    public String getCurrentTerm(String trainContent){
        Document document = Jsoup.parse(trainContent);
        Element element = document.getElementById("xq");
        for (Element selectElement : element.getAllElements()) {
            if(selectElement.attr("selected").equals("selected")){
                return selectElement.text();
            }
        }
        return "null";
    }
}
