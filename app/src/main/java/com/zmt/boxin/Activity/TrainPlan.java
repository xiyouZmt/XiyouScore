package com.zmt.boxin.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zmt.boxin.Adapter.TrainAdapter;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.GetTrainPlan;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.RequestUrl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainPlan extends AppCompatActivity {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.currentTerm) TextView currentTerm;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private App app;
    private Intent intent;
    private RequestUrl url;
    private TrainAdapter adapter;
    private ProgressDialog progressDialog;
    private int position;
    private int tempPos;
    private String [] termsArray;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_plan);
        initViews();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            String obj = msg.obj.toString();
            switch (obj){
                case "fail" :
                case "error" :
                    progressDialog.dismiss();
                    Snackbar.make(recyclerView, "网络连接错误, 请检查网络连接", Snackbar.LENGTH_SHORT).show();
                    break;
                case "no evaluate" :
                    progressDialog.dismiss();
                    Toast.makeText(TrainPlan.this, "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(TrainPlan.this, com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                    break;
                case "success" :
                    adapter = new TrainAdapter(app.getUser().getTrainCoursesList());
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    public void initViews() {
        ButterKnife.bind(this);
        app = (App) getApplication();
        toolbar.setTitle("");
        title.setText(R.string.train_plan);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new TrainAdapter(app.getUser().getTrainCoursesList());
        recyclerView.setAdapter(adapter);
        if((intent = getIntent()) != null){
            position = Integer.parseInt(intent.getStringExtra("currentTerm"));
        }
        termsArray = getResources().getStringArray(R.array.terms);
        currentTerm.setText(termsArray[position - 1] + getString(R.string.train_plan));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_train_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            case R.id.chooseTerm :
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("选择学期").setSingleChoiceItems(R.array.terms, position - 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which + 1 != position){
                            tempPos = which + 1;
                            update = true;
                        } else {
                            update = false;
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(update && tempPos != position){
                            position = tempPos;
                            currentTerm.setText(termsArray[position - 1] + getString(R.string.train_plan));
                            app.getUser().setCurrentTerm(position + "");
                            progressDialog = new ProgressDialog(TrainPlan.this);
                            progressDialog.setMessage("正在加载...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
                            GetTrainPlan getTrainPlan = new GetTrainPlan(url.getTrainPlan(),
                                    app.getUser().getTrainValue(), position + "", handler, app);
                            getTrainPlan.start();
                            update = false;
                        }
                    }
                }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
