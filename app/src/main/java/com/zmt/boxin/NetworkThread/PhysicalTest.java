package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Utils.OkHttpUtils;

/**
 * Created by MintaoZhu on 2016/12/6.
 */
public class PhysicalTest extends Thread {

    private String url;
    private String number;
    private Handler handler;

    public PhysicalTest(String url, String number, Handler handler) {
        this.url = url;
        this.number = number;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, number);
        String result = okHttpUtils.getPhysicalTest();
        Message msg = handler.obtainMessage();
        switch (result){
            case "error" :
            case "fail" :
                msg.obj = result;
                handler.sendMessage(msg);
                break;
            default :
                msg.obj = "physical";
                handler.sendMessage(msg);
                break;
        }
    }
}
