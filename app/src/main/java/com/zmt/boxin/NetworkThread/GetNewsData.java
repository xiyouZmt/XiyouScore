package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Utils.OkHttpUtils;

/**
 * Created by zmt on 2017/1/15.
 */

public class GetNewsData implements Runnable {

    private String url;
    private Handler handler;

    public GetNewsData(String url, Handler handler) {
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url);
        String result = okHttpUtils.getNewsData();
        Message msg = Message.obtain();
        msg.obj = result;
        handler.sendMessage(msg);
    }
}
