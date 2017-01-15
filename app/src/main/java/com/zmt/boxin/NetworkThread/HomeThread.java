package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.util.Log;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Dangelo on 2016/7/29.
 */
public class HomeThread implements Runnable{

    private String url;
    private Handler handler;
    private App app;

    public HomeThread(String url, Handler handler, App app) {
        this.url = url;
        this.handler = handler;
        this.app = app;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
        Log.e("url--->", url);
        Log.e("Cookie--->", app.getUser().getCookie());
        String result = okHttpUtils.getHomeByGet();
        switch (result){
            case "fail" :
                handler.sendEmptyMessage(0x333);
                break;
            default:
                Document document = Jsoup.parse(result);
                Element element = document.getElementById("xhxm");
                String name = element.text().substring(0, element.text().indexOf('同'));
                app.getUser().setName(name);
                Log.e("name--->", name);
                /**
                 * 获取课表页面
                 */
                RequestUrl url = new RequestUrl(name, app.getUser().getNumber());
                CoursesThread coursesThread = new CoursesThread(url.getCoursesUrl(), handler, app);
                Thread t = new Thread(coursesThread, "CoursesThread");
                t.start();

//                /**
//                 * 获取个人信息页面
//                 */
//                RequestUrl url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
//                PersonalMessage pThread = new PersonalMessage(url.getMessageUrl(), handler, app, );
//                Thread pc = new Thread(pThread, "PersonalMessage");
//                pc.start();
                break;
        }
    }
}
