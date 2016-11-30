package com.zmt.boxin.NetworkThread;

import android.os.Handler;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class CoursesThread implements Runnable{

    private String url;
    private Handler handler;
    private App app;

    public CoursesThread(String url, Handler handler, App app) {
        this.url = url;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        String content = okHttpUtils.getMessageByGet();
        switch (content){
            case "error" :
                handler.sendEmptyMessage(0x222);
                break;
            case "fail" :
                handler.sendEmptyMessage(0x333);
                break;
            case "no evaluate" :
                handler.sendEmptyMessage(0x444);
                break;
            default :
                /**
                 * 获取个人信息页面
                 */
                RequestUrl url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
                PersonalMessage pThread = new PersonalMessage(url.getMessageUrl(), handler, app, content);
                Thread pc = new Thread(pThread, "PersonalMessage");
                pc.start();
                break;
        }
    }
}
