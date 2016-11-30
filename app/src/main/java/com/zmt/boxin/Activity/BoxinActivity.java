package com.zmt.boxin.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zmt.boxin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoxinActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.choose) Button chooseJob;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxin);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText("博信科技");
    }

    @OnClick(R.id.choose)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.choose :
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("选择职位").setCancelable(false)
                        .setSingleChoiceItems(R.array.jobs, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pos = which;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = null;
                        switch (pos){
                            case 0 :
                                str = "产品运营";
                                break;
                            case 1 :
                                str = "前段设计";
                                break;
                            case 2 :
                                str= "后台开发";
                                break;
                            case 3 :
                                str = "视觉设计";
                                break;
                            case 4 :
                                str = "产品设计";
                                break;
                            case 5 :
                                str = "客户端开发";
                                break;
                        }
                        chooseJob.setText(str);
                    }
                }).create().show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boxin, menu);
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
