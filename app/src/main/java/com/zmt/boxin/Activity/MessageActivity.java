package com.zmt.boxin.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.DefaultTrain;
import com.zmt.boxin.NetworkThread.GetTrainPlan;
import com.zmt.boxin.NetworkThread.SaveImage;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.CircleImageView;
import com.zmt.boxin.Utils.RequestUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.QR_code)
    ImageView QR_code;
    @BindView(R.id.training_plan)
    RelativeLayout training_plan;
    @BindView(R.id.settings)
    RelativeLayout settings;
    @BindView(R.id.quitlogin)
    Button quit_login;
    @BindView(R.id.myImage)
    CircleImageView myImage;
    @BindView(R.id.student_name)
    TextView studentName;
    @BindView(R.id.student_number)
    TextView studentNumber;
    @BindView(R.id.student_class)
    TextView studentClass;
    @BindView(R.id.colleague)
    TextView colleague;
    @BindView(R.id.school)
    TextView school;
    private App app;
    private RequestUrl url;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initViews();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            String obj = msg.obj.toString();
            switch (obj){
                case "fail" :
                case "error" :
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "网络连接错误, 请检查网络连接", Snackbar.LENGTH_SHORT).show();
                    break;
                case "no evaluate" :
                    progressDialog.dismiss();
                    Toast.makeText(MessageActivity.this, "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(MessageActivity.this, com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                    break;
                case "success" :
                    progressDialog.dismiss();
                    intent.setClass(MessageActivity.this, TrainPlan.class);
                    intent.putExtra("currentTerm", app.getUser().getCurrentTerm());
                    startActivity(intent);
                    break;
                case "null" :
                    /**
                     * 无法获取当前学期
                     */
                    Snackbar.make(coordinatorLayout, "无法获取当前学期, 请稍候重试", Snackbar.LENGTH_SHORT).show();
                    break;
                default :
                    app.getUser().setCurrentTerm(obj);
                    GetTrainPlan getTrainPlan = new GetTrainPlan(url.getTrainPlan(),
                            app.getUser().getTrainValue(), obj, handler, app);
                    getTrainPlan.start();
                    break;
            }
        }
    };

    @OnClick({R.id.QR_code, R.id.training_plan, R.id.settings, R.id.quitlogin})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.QR_code:
                /**
                 * dialog
                 */
                break;
            case R.id.settings:
                intent.setClass(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.training_plan:
                if(app.getUser().getTrainCoursesList().size() == 0){
                    progressDialog.show();
                    DefaultTrain defaultTrain = new DefaultTrain(url.getTrainPlan(), handler, app);
                    defaultTrain.start();
                } else {
                    intent.setClass(MessageActivity.this, TrainPlan.class);
                    intent.putExtra("currentTerm", app.getUser().getCurrentTerm());
                    startActivity(intent);
                }
                break;
            case R.id.quitlogin:

                break;
        }
    }

    public void initViews() {
        app = (App) getApplication();
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(R.string.message);
        studentName.setText(app.getUser().getName());
        studentNumber.setText("学号: " + app.getUser().getNumber());
        colleague.setText(app.getUser().getColleague());
        studentClass.setText(app.getUser().getClasses());
        Intent intent;
        if((intent = getIntent()) != null){
            String imagePath = intent.getStringExtra("imagePath");
            if(!imagePath.equals("")){
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                myImage.setImageBitmap(bitmap);
            }
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载...");
        progressDialog.setCancelable(false);
        url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
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
