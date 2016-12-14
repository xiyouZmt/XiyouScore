package com.zmt.boxin.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zmt.boxin.Activity.MessageActivity;
import com.zmt.boxin.Activity.ScoreActivity;
import com.zmt.boxin.Activity.SettingActivity;
import com.zmt.boxin.Activity.TrainPlan;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.DefaultTrain;
import com.zmt.boxin.NetworkThread.GetTrainPlan;
import com.zmt.boxin.NetworkThread.SaveImage;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.RequestUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mine extends android.support.v4.app.Fragment {

    private App app;
    private View view;
    private String imagePath = "";
    private RequestUrl url;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    @BindView(R.id.personal) RelativeLayout personal;
    @BindView(R.id.myImage) ImageView myImage;
    @BindView(R.id.student_name) TextView student_name;
    @BindView(R.id.school) TextView school;
    @BindView(R.id.colleague) TextView colleague;
    @BindView(R.id.sex) ImageView sex;
    @BindView(R.id.QR_Code) ImageView QR_code;
    @BindView(R.id.run_note) RelativeLayout run_note;
    @BindView(R.id.score) RelativeLayout score;
    @BindView(R.id.training_plan) RelativeLayout training_plan;
    @BindView(R.id.training_plan_test) RelativeLayout training_plan_test;
    @BindView(R.id.settings) RelativeLayout settings;
    @BindView(R.id.settings_test) RelativeLayout settings_test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initViews();
        return view;
    }

    public final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Intent intent = new Intent();
            String obj = msg.obj.toString();
            switch (obj){
                case "fail" :
                case "error" :
                    progressDialog.dismiss();
                    Snackbar.make(view, "网络连接错误, 请检查网络连接", Snackbar.LENGTH_SHORT).show();
                    break;
                case "no evaluate" :
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(getActivity(), com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                case "image is ok" :
                    Object object;
                    if((object = msg.getData().get("path")) != null){
                        imagePath = object.toString();
                        bitmap = BitmapFactory.decodeFile(object.toString());
                        myImage.setImageBitmap(bitmap);
                    }
                    break;
                case "success" :
                    progressDialog.dismiss();
                    intent.setClass(getActivity(), TrainPlan.class);
                    intent.putExtra("currentTerm", app.getUser().getCurrentTerm());
                    startActivity(intent);
                    break;
                case "null" :
                    /**
                     * 无法获取当前学期
                     */
                    Snackbar.make(view, "无法获取当前学期, 请稍候重试", Snackbar.LENGTH_SHORT).show();
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

    @OnClick({R.id.personal, R.id.QR_Code, R.id.run_note, R.id.score, R.id.training_plan, R.id.training_plan_test, R.id.settings, R.id.settings_test})
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.personal :
                intent.setClass(getActivity(), MessageActivity.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
                break;
            case R.id.QR_Code :
                /**
                 * dialog
                 */
                View QR_Code_Window = getActivity().getLayoutInflater().inflate(R.layout.qr_code, null);
                ImageView user_image = (ImageView) QR_Code_Window.findViewById(R.id.user_image);
                TextView username = (TextView) QR_Code_Window.findViewById(R.id.username);
                TextView user_grade = (TextView) QR_Code_Window.findViewById(R.id.user_grade);
                ImageView user_sex = (ImageView) QR_Code_Window.findViewById(R.id.user_sex);
                ImageView qr_code = (ImageView) QR_Code_Window.findViewById(R.id.qr_code);

                if(bitmap == null){
                    bitmap = BitmapFactory.decodeFile(imagePath);
                }
                user_image.setImageBitmap(bitmap);
                username.setText(app.getUser().getName());
                user_grade.setText(app.getUser().getClasses());
                if(app.getUser().getSex().equals("男")){
                    user_sex.setBackgroundResource(R.mipmap.male);
                } else {
                    user_sex.setBackgroundResource(R.mipmap.female);
                }
                Bitmap qrCodeBitmap = EncodingUtils.createQRCode(app.getUser().getName(), 700, 700, null);
                qr_code.setImageBitmap(qrCodeBitmap);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setView(QR_Code_Window).create().show();
                break;
            case R.id.run_note :
                break;
            case R.id.score :
                intent.setClass(getActivity(), ScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.training_plan:
            case R.id.training_plan_test :
                if(app.getUser().getTrainCoursesList().size() == 0){
                    progressDialog.show();
                    DefaultTrain defaultTrain = new DefaultTrain(url.getTrainPlan(), handler, app);
                    defaultTrain.start();
                } else {
                    intent.setClass(getActivity(), TrainPlan.class);
                    intent.putExtra("currentTerm", app.getUser().getCurrentTerm());
                    startActivity(intent);
                }
                break;
            case R.id.settings :
            case R.id.settings_test :
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void initViews(){
        app = (App) getActivity().getApplication();
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载...");
        progressDialog.setCancelable(false);
        student_name.setText(app.getUser().getName());
        colleague.setText(app.getUser().getColleague());
        if(app.getUser().getSex().equals("男")){
            sex.setBackgroundResource(R.mipmap.male);
        } else {
            sex.setBackgroundResource(R.mipmap.female);
        }
        /**
         * 获取头像
         */
        SaveImage saveImage = new SaveImage(app, handler);
        Thread t = new Thread(saveImage, "SaveImage");
        t.start();
        url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
    }
}
