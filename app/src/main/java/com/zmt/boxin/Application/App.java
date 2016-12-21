package com.zmt.boxin.Application;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by Dangelo on 2016/7/5.
 */
public class App extends Application {

    public static User user = new User();

    public void onCreate(){
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    public User getUser(){
        return user;
    }

}
