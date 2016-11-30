package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class PersonalMessage implements Runnable{

    private String url;
    private Handler handler;
    private App app;
    private String courses;

    public PersonalMessage(String url, Handler handler, App app, String courses) {
        this.url = url;
        this.handler = handler;
        this.app = app;
        this.courses = courses;
    }


    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        String result = okHttpUtils.getMessageByGet();
        Message msg = new Message();
        switch (result){
            case "error" :
            case "fail" :
                msg.obj = result;
                handler.sendMessage(msg);
                break;
            default :
                /**
                 * 设置个人信息
                 */
                Document document = Jsoup.parse(result);
                Element number = document.getElementById("xh");         //学号
                Element name = document.getElementById("xm");           //姓名
                Element sex = document.getElementById("lbl_xb");        //性别
                Element colleague = document.getElementById("lbl_xy");  //学院
                Element major = document.getElementById("lbl_zymc");    //专业
                Element classes = document.getElementById("lbl_xzb");   //班级
                Element imageUrl = document.getElementById("xszp");     //图片
                String imageSuffix = imageUrl.attr("src");
                app.getUser().setNumber(number.text());
                app.getUser().setName(name.text());
                app.getUser().setSex(sex.text());
                app.getUser().setColleague(colleague.text());
                app.getUser().setMajor(major.text());
                app.getUser().setClasses(classes.text());
                app.getUser().setImageUrl(new RequestUrl().getIP() + imageSuffix);
                msg.obj = courses;
                handler.sendMessage(msg);
                break;
        }
    }
}