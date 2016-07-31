package com.zmt.boxin.NetworkThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Dangelo on 2016/7/30.
 */
public class ScoreThread implements Runnable{

    private String url;
    private Handler handler;
    private App app;

    public ScoreThread(String url, Handler handler, App app) {
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
                msg.obj = "term is ok";
                Bundle bundle = getTerm(content);
                msg.setData(bundle);
                handler.sendMessage(msg);
                break;
        }
    }

    public Bundle getTerm(String content){
        int i = 0;
        Bundle bundle = new Bundle();
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("ddlXN");
        if(element != null){
            for (Element element1 : element.getAllElements()) {
                if(!element1.text().equals("") && element1.tag().toString().equals("option")){
                    bundle.putString("term" + i, element1.text());
                    i++;
                }
            }
            return bundle;
        }
        return null;
    }

}
