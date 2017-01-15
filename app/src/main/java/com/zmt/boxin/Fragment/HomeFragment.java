package com.zmt.boxin.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zmt.boxin.Adapter.NewsAdapter;
import com.zmt.boxin.Module.News;
import com.zmt.boxin.NetworkThread.GetNewsData;
import com.zmt.boxin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<View> carousel_figure;
    private ImageView [] imageViews;
    private View view;
    private int position = 0;
    private int sleepTime = 3000;
    private boolean isAlive = true;
    private boolean isContinue = true;
    private Carousel carousel;
    private List<News> newsList;
    private LinearLayoutManager manager;
    private NewsAdapter adapter;
    private String newsUrl;
    private String nextPage;
    private String url = "http://www.xiyou.edu.cn/index/xy21";
    private boolean loadMore = false;
    private int current = 0;
    private int num ;      //总页数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater .inflate(R.layout.fragment_home, container, false);
        initViews();
        initNews();
        return view;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String content = msg.obj.toString();
            switch (content){
                case "error" :
                    Snackbar.make(view, "网络连接错误", Snackbar.LENGTH_SHORT).show();
                    break;
                case "fail" :
                    Snackbar.make(view, "服务器连接错误", Snackbar.LENGTH_SHORT).show();
                    break;
                case "carousel" :
                    viewPager.setCurrentItem(msg.what);
                    break;
                default :
                    analyseData(content);
                    if(adapter == null){
                        adapter = new NewsAdapter(newsList, getContext());
                        recyclerView.setAdapter(adapter);
                    } else {
                        if(loadMore){
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new NewsAdapter(newsList, getContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    public void analyseData(String content){
        if(content != null){
            int x = 0;
            Document document = Jsoup.parse(content);
            if (!loadMore) {
                newsList.clear();
                Element element = document.getElementById("fanye3942");
                String text = element.text();
                num = Integer.parseInt(text.substring(text.lastIndexOf('/') + 1, text.length()-1));
                Log.w("num--->", String.valueOf(num));
            } else {
                newsList.remove(newsList.size() - 1);
            }
            Elements elements = document.getElementsByClass("c3942");
            while (true) {
                if(x == elements.size()){
                    break;
                }
                News news = new News();
                news.setTitle(elements.get(x).attr("title"));
                news.setUrl(elements.get(x).attr("href"));
                if (!loadMore || x >= 1) {
                    newsList.add(news);
                    if (x >= 18) {
                        break;
                    }
                }
                x++;
            }
            if (num > 1) {
                nextPage = url + "/" + --num + ".htm";
            }
            newsList.add(null);
        }
    }

    public void initNews(){
        newsList = new ArrayList<>();
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        newsUrl = url + ".htm";
        GetNewsData getNewsData = new GetNewsData(newsUrl, handler);
        Thread t = new Thread(getNewsData, "GetNewsData");
        t.start();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore = false;
                current = 0;
                GetNewsData getNewsData = new GetNewsData(newsUrl, handler);
                Thread t = new Thread(getNewsData, "GetNewsData");
                t.start();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(current >= num){
                    if(newsList.size() != 0){
                        newsList.remove(newsList.size() - 1);
                        adapter.notifyDataSetChanged();
                        Snackbar.make(view, "没有更多啦~", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    int itemCount = manager.getItemCount();
                    int lastItemCount = manager.findLastVisibleItemPosition();
                    if(itemCount <= (lastItemCount + 1)) {
                        /**
                         * reach the bottom and load more
                         */
                        loadMore = true;
                        current ++;
                        GetNewsData getNewsList = new GetNewsData(nextPage, handler);
                        Thread thread = new Thread(getNewsList, "GetNewsData");
                        thread.start();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    public void initViews(){
        ButterKnife.bind(this, view);
        carousel_figure = new ArrayList<>();
        /**
         * 轮播图
         */
        int [] images = {R.mipmap.home_image1, R.mipmap.home_image2, R.mipmap.home_image3};
        for (int image : images) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(image);
            carousel_figure.add(imageView);
        }
        viewPager.setAdapter(new AdvertiseAdapter());
        viewPager.addOnPageChangeListener(new PointListener());
        /**
         * 轮播图下方小圆点
         */
        ViewGroup advertisePoint = (ViewGroup) view.findViewById(R.id.homeAdvertisePoint);
        imageViews = new ImageView[images.length];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new ImageView(getActivity());
            if(i == 0){
                imageViews[i].setBackgroundResource(R.mipmap.imagepager_press);
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.imagepager_normal);
            }
            layoutParams.bottomMargin = 10;
            layoutParams.rightMargin = 20;
            imageViews[i].setLayoutParams(layoutParams);
            advertisePoint.addView(imageViews[i]);
        }
        if(carousel == null){
            carousel = new Carousel();
            carousel.start();
        }
        /**
         * 拖住时停止轮播
         */
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN :
                    case MotionEvent.ACTION_MOVE :
                         isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP :
                        isContinue = true;
                        break;
                    default :
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
    }

    public class AdvertiseAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return carousel_figure.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(carousel_figure.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(carousel_figure.get(position));
            return carousel_figure.get(position);
        }
    }

    public class PointListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int pos) {
            position = pos;
            imageViews[pos].setBackgroundResource(R.mipmap.imagepager_press);
            for (int j = 0; j < imageViews.length; j++) {
                if (j != pos) {
                    imageViews[j].setBackgroundResource(R.mipmap.imagepager_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class Carousel extends Thread{
        @Override
        public void run() {
            /**
             * 轮播图片
             */
            while (isAlive){
                if(isContinue){
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Log.e("InterruptedException", e.toString());
                    }
                    position = (position + 1) % imageViews.length;
                    Message msg = Message.obtain();
                    msg.obj = "carousel" ;
                    msg.what = position;
                    handler.sendMessage(msg);
                }
            }
        }
    }
}
