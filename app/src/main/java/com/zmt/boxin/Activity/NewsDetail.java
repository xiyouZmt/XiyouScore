package com.zmt.boxin.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.zmt.boxin.Module.News;
import com.zmt.boxin.NetworkThread.GetNewsData;
import com.zmt.boxin.NetworkThread.HtmlHttpImageGetter;
import com.zmt.boxin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetail extends AppCompatActivity {

    public static String TAG = "NEWS";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.news_content)
    TextView news_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initViews();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String content = msg.obj.toString();
            switch (content) {
                case "error":
                    Snackbar.make(toolbar, "网络连接错误", Snackbar.LENGTH_SHORT).show();
                    break;
                case "fail":
                    Snackbar.make(toolbar, "服务器连接错误", Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    String detail = analyseNews(content);
                    news_detail.setText(Html.fromHtml(detail, new HtmlHttpImageGetter(news_detail), null));
            }
        }
    };

    public void initViews(){
        ButterKnife.bind(this);
        title.setText("");
        News news = (News) getIntent().getSerializableExtra(TAG);
        toolbar.setTitle(news.getTitle());
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        GetNewsData getNewsData = new GetNewsData(news.getUrl(), handler);
        Thread thread = new Thread(getNewsData, "GetNewsData");
        thread.start();
    }

    public String analyseNews(String content){
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("vsb_content_1031");
        return element.toString().replaceAll("\\.\\./\\.\\./", "http://news.xupt.edu.cn/");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
