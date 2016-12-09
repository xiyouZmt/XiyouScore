package com.zmt.boxin.Utils;

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
    private String name;
    private String number;
    private String password;
    private String checkCode;
    private String cookie;

    public OkHttpUtils(String url, String number, String password, String checkCode, String cookie) {
        this.url = url;
        this.number = number;
        this.password = password;
        this.checkCode = checkCode;
        this.cookie = cookie;
    }

    public OkHttpUtils(String url, String cookie, String name) {
        this.url = url;
        this.cookie = cookie;
        this.name = name;
    }

    public OkHttpUtils(String url, String cookie) {
        this.url = url;
        this.cookie = cookie;
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
        builder.add("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz61IGQDPAm6cyppI+uTzQcI8sEH6Q==")
                .add("txtUserName", number)
                .add("TextBox2", password)
                .add("txtSecretCode", checkCode)
                .add("RadioButtonList1", "学生")
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "");
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie)
                .addHeader("Referer", "http://222.24.19.201").post(builder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 302){
                return cookie;
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
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
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
                .addHeader("Cookie", cookie).addHeader("Referer", url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
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

    public String getTrainPlan(String trainValue, String term){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("__EVENTTARGET", "dpDBGrid:txtPageSize").add("__EVENTARGUMENT", "")
                .add("__VIEWSTATE", trainValue).add("xq", term).add("kcxz", "%C8%AB%B2%BF")
                .add("Button1", "%BF%C9%CC%E6%BB%BB%BF%CE%B3%CC")
                .add("dpDBGrid:txtChoosePage", "1").add("dpDBGrid:txtPageSize", "20");
        Request request = new Request.Builder().url(url).addHeader("Referer", url)
                .addHeader("Cookie", cookie).post(builder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                if(response.header("Content-Length").equals("276")){
                    return "no evaluate";
                }
                return response.body().string();
            } else {
                return "error";
            }
        } catch (IOException e) {
            Log.e("get raining plan error", e.toString());
            return "fail";
        }
    }

    public String getScoreByPost(String year, String term, String scoreValue){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        /**
         * 请求体
         */
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("__EVENTTARGET", "").add("__EVENTARGUMENT", "")
                .add("__VIEWSTATE", scoreValue).add("hidLanguage", "")
                .add("ddlXN", year).add("ddlXQ", term).add("ddl_kcxz", "")
                .add("btn_xq", "%D1%A7%C6%DA%B3%C9%BC%A8");
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie)
                .addHeader("Referer", url).post(builder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
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
                return content.toString();
            } else {
                return "error";
            }
        } catch (IOException e) {
            Log.e("getCourses error!--->", e.toString());
            return "fail";
        }
    }

    public String getPhysicalTest(String key, String value){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add(key, value);
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            } else {
                return "error";
            }
        } catch (IOException e) {
            Log.e("get physicsTest error", e.toString());
            return "fail";
        }
    }

    public String saveImage(){
        String rootPath = Environment.getExternalStorageDirectory() + "/";
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        okHttpClient.setFollowSslRedirects(false);
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).addHeader("Referer",url).build();
        try {
            File file = new File(rootPath + "/Boxin/Images");
            if(!file.exists()){
                file.mkdirs();
            }
            File file1 = new File(file.getPath() + '/' + name + ".png");
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
