package com.zmt.boxin.NetworkThread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.Module.PhysicalTest;
import com.zmt.boxin.Module.PhysicalTestItem;
import com.zmt.boxin.Utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MintaoZhu on 2016/12/6.
 */
public class PhysicalTestItemThread extends Thread {

    private App app;
    private String url;
    private String meaScoreId;
    private Handler handler;
    public  static final String PHYSICAL_TEST_ITEM_OK = "physical test item is ok";
    public  static final String PHYSICAL_TEST_ITEM_NULL = "physical test item is null";

    public PhysicalTestItemThread(App app, String url, String meaScoreId, Handler handler) {
        this.app = app;
        this.url = url;
        this.meaScoreId = meaScoreId;
        this.handler = handler;
    }

    @Override
    public void run() {
        OkHttpUtils okHttpUtils = new OkHttpUtils(url);
        String result = okHttpUtils.getPhysicalTest("meaScoreId", meaScoreId);
        Message msg = handler.obtainMessage();
        switch (result){
            case "error" :
            case "fail" :
                msg.obj = result;
                handler.sendMessage(msg);
                break;
            default :
                boolean physicalTestItem = setPhysicalTestItem(result);
                if(physicalTestItem){
                    msg.obj = PHYSICAL_TEST_ITEM_OK;
                } else {
                    msg.obj = PHYSICAL_TEST_ITEM_NULL;
                }
                handler.sendMessage(msg);
                break;
        }
    }

    public boolean setPhysicalTestItem(String content){
        try {
            app.getUser().getPhysicalTestItem().clear();
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if(jsonArray.length() != 0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    PhysicalTestItem physicalTestItem = new PhysicalTestItem();
                    physicalTestItem.setExamName(object.getString("examName")).setExamUnit(object.getString("examunit"))
                            .setActualScore(object.getString("actualScore")).setScore(object.getString("score"))
                            .setRank(object.getString("rank")).setPlusRank(object.getString("plusRank"))
                            .setMeaStatus(object.getString("meaStatus"));
                    app.getUser().getPhysicalTestItem().add(physicalTestItem);
                }
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            Log.e("json error ", e.toString());
            return false;
        }
    }
}
