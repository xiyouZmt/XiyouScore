package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Module.PhysicalTest;
import com.zmt.boxin.Utils.OkHttpUtils;
import com.zmt.boxin.Utils.RequestUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MintaoZhu on 2016/12/6.
 */
public class PhysicalTestThread extends Thread {

    private App app;
    private String url;
    private String number;
    private Handler handler;
    public  static final String PHYSICAL_TEST_OK = "physical test is ok";
    public  static final String PHYSICAL_TEST_NULL = "physical test is null";

    public PhysicalTestThread(App app, String url, String number, Handler handler) {
        this.app = app;
        this.url = url;
        this.number = number;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url);
        String result = okHttpUtils.getPhysicalTest("stuNum", number);
        Message msg = handler.obtainMessage();
        switch (result){
            case "error" :
            case "fail" :
                msg.obj = result;
                handler.sendMessage(msg);
                break;
            default :
                boolean physicalTestResult = setPhysicalTest(result);
                if(!physicalTestResult){
                    msg.obj = PHYSICAL_TEST_NULL;
                    handler.sendMessage(msg);
                }
                break;
        }
    }

    public boolean setPhysicalTest(String content){
        try {
            app.getUser().getPhysicalTest().clear();
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if(jsonArray.length() == 0){
                return false;
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    PhysicalTest physicalTest = new PhysicalTest();
                    physicalTest.setMeaScoreId(object.getString("meaScoreId")).setYear(object.getString("year"));
                    app.getUser().getPhysicalTest().add(physicalTest);
                }
                RequestUrl url = new RequestUrl();
                String meaScoreId = app.getUser().getPhysicalTest().get(0).getMeaScoreId();
                PhysicalTestItemThread itemThread = new PhysicalTestItemThread(
                        app, url.getPhysicalTestItem(), meaScoreId, handler);
                itemThread.start();
                return true;
            }
        } catch (JSONException e) {
            Log.e("json error ", e.toString());
            return false;
        }
    }
}
