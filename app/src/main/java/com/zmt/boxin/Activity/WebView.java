package com.zmt.boxin.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebViewClient;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.RequestUrl;

import java.util.HashMap;
import java.util.Map;

public class WebView extends AppCompatActivity {

    private Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        App app = (App) getApplication();
        final String cookie = app.getUser().getCookie();
        final android.webkit.WebView webView;
        webView = (android.webkit.WebView) findViewById(R.id.webView);
        RequestUrl url = new RequestUrl(app.getUser().getNumber());
        map.put("Cookie", cookie);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url.getHomeUrl(), map);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(android.webkit.WebView webView, String url) {
                webView.loadUrl(url, map);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
