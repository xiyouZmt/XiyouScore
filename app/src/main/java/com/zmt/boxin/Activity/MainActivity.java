package com.zmt.boxin.Activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.Fragment.Courses;
import com.zmt.boxin.Fragment.HomeFragment;
import com.zmt.boxin.Fragment.Mine;
import com.zmt.boxin.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AutoLayoutActivity {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.home) LinearLayout linear_home;
    @BindView(R.id.courses) LinearLayout linear_courses;
    @BindView(R.id.mine) LinearLayout linear_mine;
    @BindView(R.id.image_home) ImageView image_home;
    @BindView(R.id.image_courses) ImageView image_courses;
    @BindView(R.id.image_mine) ImageView image_mine;
    @BindView(R.id.text_home) TextView text_home;
    @BindView(R.id.text_courses) TextView text_courses;
    @BindView(R.id.text_mine) TextView text_mine;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.container) CoordinatorLayout coordinatorLayout;
    private App app;
    private List<Fragment> fragmentList;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());

        final int start = 100;
        final int deltaX = 100;
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1).setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = valueAnimator.getAnimatedFraction();
                title.scrollTo(start + (int)(fraction * deltaX), 0);
            }
        });
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            initMenu();
            setChoose(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void initMenu(){
        image_home.setBackgroundResource(R.mipmap.home);
        image_courses.setBackgroundResource(R.mipmap.courses);
//        image_run.setBackgroundResource(R.mipmap.run);
//        image_make_run.setBackgroundResource(R.mipmap.make_run);
//        image_choose.setBackgroundResource(R.mipmap.choose);
        image_mine.setBackgroundResource(R.mipmap.mine);
        text_home.setTextColor(0xff757575);
        text_courses.setTextColor(0xff757575);
//        text_run.setTextColor(0xff757575);
//        text_make_run.setTextColor(0xff757575);
//        text_choose.setTextColor(0xff757575);
        text_mine.setTextColor(0xff757575);
    }

    public void setChoose(int id){
        switch (id){
            case 0:
            case R.id.home :
                title.setText(R.string.app_name);
                image_home.setBackgroundResource(R.mipmap.home_pressed);
                text_home.setTextColor(0xff03a9f4);
                viewPager.setCurrentItem(0);
                break;
            case 1 :
            case R.id.courses :
                title.setText(R.string.courses);
                image_courses.setBackgroundResource(R.mipmap.courses_pressed);
                text_courses.setTextColor(0xff03a9f4);
                viewPager.setCurrentItem(1);
                break;
//            case 2 :
//            case R.id.make_run :
//                title.setText(R.string.make_run);
//                image_make_run.setBackgroundResource(R.mipmap.make_run_pressed);
//                text_make_run.setTextColor(0xff03a9f4);
//                viewPager.setCurrentItem(2);
////                Intent intent = new Intent();
////                intent.setClass(this, RunActivity.class);
////                startActivity(intent);
//                break;
//            case 3 :
//            case R.id.choose :
//                title.setText(R.string.choose);
//                image_choose.setBackgroundResource(R.mipmap.choose_pressed);
//                text_choose.setTextColor(0xff03a9f4);
//                viewPager.setCurrentItem(3);
//                break;
            case 2:
            case R.id.mine :
                title.setText(R.string.mine);
                image_mine.setBackgroundResource(R.mipmap.mine_pressed);
                text_mine.setTextColor(0xff03a9f4);
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @OnClick({R.id.home, R.id.courses, R.id.mine})
    public void onClick(View v){
        initMenu();
        setChoose(v.getId());
    }

    public void initViews(){
        ButterKnife.bind(this);
        app = (App)getApplication();
        fragmentList = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        Courses courses = new Courses();
        Mine mine = new Mine();
        fragmentList.add(homeFragment);
        fragmentList.add(courses);
        fragmentList.add(mine);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                switch (menuItem.getItemId()) {
                    case R.id.action_about:
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.xiyoumobile.com"));
                        Snackbar.make(coordinatorLayout, R.string.about, Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        intent.setClass(MainActivity.this, SettingActivity.class);
                        Snackbar.make(coordinatorLayout, R.string.action_settings, Snackbar.LENGTH_SHORT).show();
                        break;
                }
                startActivity(intent);
                return true;
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - exitTime > 2000){
                Snackbar.make(coordinatorLayout, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else  {
                finish();
                app.getUser().getTermList().clear();
                app.getUser().getScoreList().clear();
                app.getUser().getTrainCoursesList().clear();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
