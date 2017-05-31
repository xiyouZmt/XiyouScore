package com.zmt.boxin.NetworkThread;

import android.os.Handler;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class GetSession implements Runnable{

    private String url;
    private String number;
    private String password;
    private String checkCode;
    private String viewState;
    private Handler handler;
    private App app;

    public GetSession(String url, String number, String password, String checkCode, String viewState, Handler handler, App app) {
        this.url = url;
        this.number = number;
        this.password = password;
        this.checkCode = checkCode;
        this.viewState = viewState;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, number, password, checkCode, viewState,  app.getUser().getCookie());
        String result = okHttpUtils.getDataByPost();
        switch (result){
            case "error" :
                handler.sendEmptyMessage(0x111);
                break;
            case "can not find cookie" :
                handler.sendEmptyMessage(0x222);
                break;
            case "fail" :
                handler.sendEmptyMessage(0x333);
                break;
            default :
                app.getUser().setCookie(result);
                app.getUser().setNumber(number);
                RequestUrl url = new RequestUrl(number);
                HomeThread homeThread = new HomeThread(url.getHomeUrl(), handler, app);
                Thread t = new Thread(homeThread, "htmlThread");
                t.start();
                break;
        }
    }
}
