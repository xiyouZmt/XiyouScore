package com.zmt.boxin.Utils;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dangelo on 2016/7/19.
 */
public class OkHttpUtils {

    private String url;
    private String value1;
    private String value2;
    private String value3;
    private String value4;

    public OkHttpUtils(String url, String value1, String value2, String value3, String value4) {
        this.url = url;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public OkHttpUtils(String url, String value1, String value2, String value3) {
        this.url = url;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public OkHttpUtils(String url, String value1, String value2) {
        this.url = url;
        this.value1 = value1;
        this.value2 = value2;
    }

    public OkHttpUtils(String url){
        this.url = url;
    }

    public String getCheckCode(){
        String rootPath = Environment.getExternalStorageDirectory() + "/";
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.e("state--->", String.valueOf(response.code()));
            if(response.code() == 200){
                /**
                 * 读取验证码图片
                 */
                File file = new File(rootPath + "/Boxin/Images");
                if(!file.exists()){
                    file.mkdirs();
                }
                File file1 = new File(file.getPath() + "/checkCode.png");
                if(file1.exists()){
                    file1.delete();
                }
                InputStream is = response.body().byteStream();
                OutputStream os = new FileOutputStream(file1);
                byte [] b = new byte[1024];
                int c;
                while ((c = is.read(b)) > 0){
                    os.write(b, 0, c);
                }
                is.close();
                os.close();
                /**
                 * 获取cookie
                 */
                Log.e("headers", response.headers().toString());
                if(response.header("Set-Cookie") != null){
                    String cookie =  response.header("Set-Cookie");
                    return cookie.substring(0, cookie.indexOf(';'));
                } else {
                    return "can not find cookie";
                }
            } else {
                return "error";
            }
        } catch (IOException e) {
            Log.e("get CheckCode error", e.toString());
            return "error";
        }
    }

    public String getDataByPost(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        /**
         * 请求体
         */
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz4EPWKUJ7QVy9jt5geaO9kcCdS0zQ==")
                .add("txtUserName", value1)
                .add("TextBox2", value2)
                .add("txtSecretCode", value3)
                .add("RadioButtonList1", "学生")
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "");
        Request request = new Request.Builder().url(url).addHeader("Cookie", value4)
                .addHeader("Referer", "http://222.24.19.201").post(builder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.e("state--->", String.valueOf(response.code()));
            if(response.code() == 302){
//                /**
//                 * 通过Set-Cookie获取Cookie
//                 */
//                Log.e("response--->", response.headers().toString());
//                if(response.header("Set-Cookie") != null) {
//                    String set_cookie = response.header("Set-Cookie");
//                    if(set_cookie.indexOf(';') > 0){
//                        return set_cookie.substring(0, set_cookie.indexOf(';'));
//                    } else {
//                        return set_cookie;
//                    }
//                } else {
//                    return "can not find cookie!";
//                }
                return value4;
            } else {
                return "error";
            }
        } catch (Exception e) {
            Log.e("getDataByGet error", e.toString());
            return "fail";
        }
    }

    public String getHomeByGet(){
        StringBuilder content = new StringBuilder();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).addHeader(value1, value2).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.e("getHome--->", String.valueOf(response.code()));
            if(response.code() == 200){
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null){
                    content.append(line);
                }
                reader.close();
                return content.toString();
            } else {
                return "fail";
            }
        } catch (IOException e) {
            Log.e("getHTML error", e.toString());
            return "fail";
        }
    }

    public String getMessageByGet(){
        StringBuilder content = new StringBuilder();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        okHttpClient.setFollowSslRedirects(false);
        Request request = new Request.Builder().url(url)
                .addHeader(value1, value2).addHeader("Referer",url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                Log.e("headers--->", response.headers().toString());
                if(response.header("Content-Length").equals("276")){
                    return "no evaluate";
                }
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null){
                    content.append(line);
                }
                reader.close();
                return content.toString();
            } else {
                return "error";
            }
        } catch (IOException e) {
            Log.e("getCourses error!--->", e.toString());
            return "fail";
        }
    }

    public String getScoreByPost(Bundle termBundle, Bundle scoreBundle){
        int count = 0;
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        for (int i = 0; i < termBundle.size(); i++) {
            for (int j = 2; j > count; j--) {
                Object object;
                String year = "";
                String term = "";
                if((object = termBundle.get("year" + i)) != null){
                    year = object.toString();
                    term = String.valueOf(j);
                }
                /**
                 * 请求体
                 */
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("__EVENTTARGET", "").add("__EVENTARGUMENT", "")
                        .add("__VIEWSTATE", value3).add("hidLanguage", "")
                        .add("ddlXN", year).add("ddlXQ", term).add("ddl_kcxz", "")
                        .add("btn_xq", "%D1%A7%C6%DA%B3%C9%BC%A8");
                Request request = new Request.Builder().url(url).addHeader(value1, value2)
                        .addHeader("Referer", url).post(builder.build()).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if(response.code() == 200){
                        Log.e("headers--->", response.headers().toString());
                        if(response.header("Content-Length").equals("276")){
                            return "no evaluate";
                        }
                        BufferedReader reader = new BufferedReader(response.body().charStream());
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null){
                            content.append(line);
                        }
                        reader.close();
                        if((object = termBundle.get("year" + i)) != null){
                            scoreBundle.putString(object.toString() + " " + j, content.toString());
                        }
                    } else {
                        return "error";
                    }
                } catch (IOException e) {
                    Log.e("getCourses error!--->", e.toString());
                    return "fail";
                }
            }
        }
        return "score is ok";
    }

    public String saveImage(){
        String rootPath = Environment.getExternalStorageDirectory() + "/";
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        okHttpClient.setFollowSslRedirects(false);
        Request request = new Request.Builder().url(url).addHeader(value1, value2).addHeader("Referer",url).build();
        try {
            File file = new File(rootPath + "/Boxin/Images");
            if(!file.exists()){
                file.mkdirs();
            }
            File file1 = new File(file.getPath() + '/' + value3 + ".png");
            if(!file1.exists()){
                Response response = okHttpClient.newCall(request).execute();
                Log.e("responseCode--->", String.valueOf(response.code()));
                InputStream is = response.body().byteStream();
                OutputStream os = new FileOutputStream(file1);
                byte [] b = new byte[1024 * 10];
                int c;
                while ((c = is.read(b)) > 0){
                    os.write(b, 0, c);
                }
                os.close();
                is.close();
            }
            Log.e("file1.getPath--->", file1.getPath());
            return file1.getPath();
        } catch (IOException e) {
            Log.e("getImageError--->", e.toString());
            return "error";
        }
    }

}
