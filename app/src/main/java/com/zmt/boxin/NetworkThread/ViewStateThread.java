package com.zmt.boxin.NetworkThread;

import android.os.Handler;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by zmt on 2017/5/31.
 */

public class ViewStateThread implements Runnable {

    private String viewStateUrl;
    private String number;
    private String pwd;
    private String checkCode;
    private App app;
    private Handler handler;

    public ViewStateThread(String viewStateUrl, String number, String pwd, String checkCode, App app, Handler handler) {
        this.viewStateUrl = viewStateUrl;
        this.number = number;
        this.pwd = pwd;
        this.checkCode = checkCode;
        this.app = app;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(viewStateUrl);
        String result = okHttpUtils.getViewState();
        switch (result){
            case "failed" :
                handler.sendEmptyMessage(0x333);
                break;
            case "error" :
                handler.sendEmptyMessage(0x222);
                break;
            default :
                String viewState = analyseViewState(result);
                app.getUser().set_VIEWSTATE(viewState);
                RequestUrl url = new RequestUrl();
                GetSession thread = new GetSession(url.getCookieUrl(), number, pwd, checkCode, viewState, handler, app);
                Thread t = new Thread(thread, "NetWorkThread");
                t.start();
                break;
        }
    }

    private String analyseViewState(String html){
        if(html != null){
            Document document = Jsoup.parse(html);
            Element element = document.getElementById("form1");
            return element.getElementsByTag("input").attr("value");
        }
        return "";
    }
}
