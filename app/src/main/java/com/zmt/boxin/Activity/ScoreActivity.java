package com.zmt.boxin.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zmt.boxin.Adapter.FailedPassAdapter;
import com.zmt.boxin.Adapter.ScoreAdapter;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.DropDownMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ScoreActivity extends AppCompatActivity {

    private App app;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindViews({R.id.dropDownMenu1, R.id.dropDownMenu2, R.id.dropDownMenu3, R.id.dropDownMenu4,
            R.id.dropDownMenu5, R.id.dropDownMenu6, R.id.dropDownMenu7, R.id.dropDownMenu8})
    DropDownMenu[] dropDownMenu;
    @BindView(R.id.failedPass)
    LinearLayout failedPass;
    @BindView(R.id.failedPassList)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        initViews();
    }

    public void initViews() {
        app = (App) getApplication();
        ButterKnife.bind(this);
        title.setText(R.string.score);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**
         * 成绩详情
         */
        for (int i = 0; i < app.getUser().getTermList().size(); i++) {
            /**
             * 学期
             */
            List<String> termList = new ArrayList<>();
            termList.add(app.getUser().getTermList().get(i));
            /**
             * 成绩列表
             */
            List<View> viewList = new ArrayList<>();
            ListView listView = new ListView(this);
            listView.setDividerHeight(0);
            ScoreAdapter scoreAdapter = new ScoreAdapter(this, app.getUser().getScoreList().get(i));
            listView.setAdapter(scoreAdapter);
            viewList.add(listView);
            dropDownMenu[i].setVisibility(View.VISIBLE);
            dropDownMenu[i].setDropDownMenu(termList, viewList);
        }
        if (app.getUser().getFailedPass().size() != 0) {
            failedPass.setVisibility(View.VISIBLE);
            FailedPassAdapter adapter = new FailedPassAdapter(this, app.getUser().getFailedPass());
            listView.setAdapter(adapter);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
