package com.zmt.boxin.Utils;

import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HttpEngine;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okio.BufferedSink;

/**
 * Created by Dangelo on 2016/7/19.
 */
public class OkHttpUtils {

    private String url;
    private String value1;
    private String value2;
    private String name;

    public OkHttpUtils(String url, String value1, String value2, String name) {
        this.url = url;
        this.value1 = value1;
        this.value2 = value2;
        this.name = name;
    }

    public OkHttpUtils(String url, String value1, String value2) {
        this.url = url;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getDataByPost(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        /**
         * 请求体
         */
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("__VIEWSTATE", "dDwxMTE4MjQwNDc1Ozs+ombGLJflIyczODVOjorgMB6XZe8=")
                .add("TextBox1", value1).add("TextBox2", value2)
                .add("RadioButtonList1","%D1%A7%C9%FA").add("Button1", "+%B5%C7%C2%BC+");
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.e("state--->", String.valueOf(response.code()));
            if(response.code() == 302){
                /**
                 * 通过Set-Cookie获取Cookie
                 */
                Log.e("response--->", response.headers().toString());
                if(response.header("Set-Cookie") != null) {
                    String set_cookie = response.header("Set-Cookie");
                    if(set_cookie.indexOf(';') > 0){
                        return set_cookie.substring(0, set_cookie.indexOf(';'));
                    } else {
                        return set_cookie;
                    }
                } else {
                    return "can not find cookie!";
                }
            } else {
                return "error";
            }
        } catch (Exception e) {
            Log.e("getDataByGet error", e.toString());
            return "fail";
        }
    }


    public String getMessageByGet(){
        StringBuilder content = new StringBuilder();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        okHttpClient.setFollowSslRedirects(false);
        Request request = new Request.Builder().url(url).addHeader(value1, value2).addHeader("Referer",url).build();
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
