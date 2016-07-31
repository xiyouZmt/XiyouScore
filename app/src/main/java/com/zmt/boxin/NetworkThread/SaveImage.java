package com.zmt.boxin.NetworkThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class SaveImage implements Runnable{

    private App app;
    private Handler handler;

    public SaveImage(App app, Handler handler) {
        this.app = app;
        this.handler = handler;
    }

    @Override
    public void run() {
        RequestUrl url = new RequestUrl(app.getUser().getNumber());
        OkHttpUtils okHttpUtils = new OkHttpUtils(url.getImageUrl(),
                "Cookie", app.getUser().getCookie(), app.getUser().getName());
        String result = okHttpUtils.saveImage();
        Message msg = new Message();
        switch (result){
            case "error" :
            case "fail" :
                msg.obj = result;
                handler.sendMessage(msg);
                break;
            default :
                msg.obj = "image is ok";
                Bundle bundle = new Bundle();
                bundle.putString("path", result);
                msg.setData(bundle);
                handler.sendMessage(msg);
                break;
        }
    }
}
