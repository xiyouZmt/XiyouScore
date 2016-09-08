package com.zmt.boxin.NetworkThread;

import android.os.Handler;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;

/**
 * Created by Dangelo on 2016/9/4.
 */
public class IdentifyThread implements Runnable {

    private String url;
    private Handler handler;
    private App app;

    public IdentifyThread(String url, Handler handler, App app) {
        this.url = url;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url);
        String result = okHttpUtils.getCheckCode();
        switch (result){
            case "error" :
                handler.sendEmptyMessage(0x333);
                break;
            case "can not find cookie" :
                handler.sendEmptyMessage(0x222);
                break;
            default :
                handler.sendEmptyMessage(0x001);
                app.getUser().setCookie(result);
                break;
        }
    }
}
