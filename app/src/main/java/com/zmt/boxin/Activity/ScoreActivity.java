package com.zmt.boxin.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zmt.boxin.Adapter.TabAdapter;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.Fragment.ScoreFragment.CourseScore;
import com.zmt.boxin.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.scoreTab) TabLayout scoreTab;
    @BindView(R.id.viewPager) ViewPager viewPager;
    private App app;
    private int position;
    private int tempPos;
    private String year;
    private int term;
    private String [] terms;

    public static final String TERM = "term";
    public static final String YEAR = "year";
    public static final String SCORE_TYPE = "scoreType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initViews();
    }

    public void initViews() {
        ButterKnife.bind(this);
        app = (App) getApplication();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        List<Fragment> scoreFragmentList = new ArrayList<>();
        scoreTab.setTabMode(TabLayout.MODE_FIXED);
        String [] scoreName = getResources().getStringArray(R.array.score);

        for (String aScoreName : scoreName) {
            CourseScore cultureCourse = new CourseScore();
            scoreTab.addTab(scoreTab.newTab().setText(aScoreName));
            Bundle bundle = new Bundle();
            bundle.putString(SCORE_TYPE, aScoreName);
            cultureCourse.setArguments(bundle);
            scoreFragmentList.add(cultureCourse);
        }

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager()
                , Arrays.asList(scoreName), scoreFragmentList);
        viewPager.setAdapter(tabAdapter);
        scoreTab.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.chooseTerm :
                int n = 0;
                final List<String> termList = app.getUser().getTermList();
                terms = new String[termList.size() * 2];
                for (int i = 0; i < termList.size(); i++) {
                    for (int j = 2; j > 0; j--) {
                        terms[n] = termList.get(i) + "学年第" + j + "学期";
                        n++;
                    }
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("选择学期").setSingleChoiceItems(terms, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempPos = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("tempPos", tempPos + "");
                        Log.e("position", position + "");
                        if(tempPos != position){
                            position = tempPos;
                            Bundle bundle = new Bundle();
                            if(position % 2 == 0){
                                term = 2;
                            } else {
                                term = 1;
                            }
                            bundle.putInt(TERM, term);
                            int yearPos = (position) / 2;
                            year = termList.get(yearPos);
                            bundle.putString(YEAR, year);
                            EventBus.getDefault().post(bundle);
                        }
                    }
                }).create().show();
                break;
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
