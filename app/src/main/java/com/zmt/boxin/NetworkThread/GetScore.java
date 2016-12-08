package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Utils.OkHttpUtils;

/**
 * Created by Dangelo on 2016/7/30.
 */
public class GetScore implements Runnable {

    private App app;
    private String url;
    private String year;
    private String term;
    private Handler handler;

    public GetScore(App app, String url, String year, String term, Handler handler) {
        this.app = app;
        this.url = url;
        this.year = year;
        this.term = term;
        this.handler = handler;
    }

    @Override
    public void run() {
//        OkHttpUtils okHttpUtils = new OkHttpUtils(url, app.getUser().getCookie());
//        Message msg = new Message();
//        if(year != null){
//            String content = okHttpUtils.getScoreByPost(year, term, app.getUser().getScoreValue());
//            switch (content){
//                case "error" :
//                case "fail" :
//                case "no evaluate" :
//                    msg.obj = content;
//                    handler.sendMessage(msg);
//                    break;
//                default :
//                    /**
//                     * 默认获取当前学年第二学期成绩，如果为空获取第一学期，再为空则成绩为空(目测大一同学)
//                     */
//                    boolean result = ScoreThread.setScore(content, app);
//                    if(result){
//                        msg.obj = year;
//                        msg.what = Integer.parseInt(term);
//                        handler.sendMessage(msg);
//                    } else {
//                        if(term.equals("2")){
//                            term = "1";
//                            run();
//                        } else {
//                            msg.obj = "null";
//                            handler.sendMessage(msg);
//                        }
//                    }
//                    break;
//            }
//        } else {
//            msg.obj = "null";
//            handler.sendMessage(msg);
//        }
    }
}